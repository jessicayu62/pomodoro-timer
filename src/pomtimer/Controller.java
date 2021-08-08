package pomtimer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import pomtimer.model.IPomodoroTimer;
import pomtimer.model.ITask;
import pomtimer.model.PomodoroTimer;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    @FXML
    public Button addTaskButton;
    @FXML
    public Label currentTaskLabel;
    @FXML
    private BorderPane background;
    @FXML
    public Label pomodoroNumLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private Spinner<Integer> pomSpinner;
    @FXML
    private TextField pomTaskText;
    @FXML
    private ListView<HBox> taskList = new ListView<>();


    private IPomodoroTimer model;

    public Controller() {
        model = new PomodoroTimer();
        Timeline updateTimer = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            timerLabel.setText(model.getRemainingTime());
                            if (model.getRemainingTime().equals("00:00")) {
                                model.changeToNextOperation();
                                timerLabel.setText(model.getRemainingTime());
                                pomodoroNumLabel.setText("Pomodoro Number: " + model.getNumPomodoros());
                                setBackgroundColor();
                            }
                        }));
        updateTimer.setCycleCount(Timeline.INDEFINITE);
        updateTimer.play();
    }

    public void pressPomodoroButton(ActionEvent event) {
        model.resetTimer();
        model.goToPomodoro();
        timerLabel.setText(model.getRemainingTime());
        setBackgroundColor();
    }

    public void pressShortBreakButton(ActionEvent event) {
        model.resetTimer();
        model.goToShortBreak();
        timerLabel.setText(model.getRemainingTime());
        setBackgroundColor();
    }

    public void pressLongBreakButton(ActionEvent event) {
        model.resetTimer();
        model.goToLongBreak();
        timerLabel.setText(model.getRemainingTime());
        setBackgroundColor();
    }

    public void pressStartButton(ActionEvent event) {
        model.startTimer();
    }

    public void pressStopButton(ActionEvent event) {
        model.pauseTimer();
    }

    public void pressResetButton(ActionEvent event) {
        model.resetTimer();
    }

    public void pressSkipButton(ActionEvent event) {
        model.skipTimer();
        pomodoroNumLabel.setText("Pomodoro Number: " + model.getNumPomodoros());
        timerLabel.setText(model.getRemainingTime());
        setBackgroundColor();
    }

    public void pressAddTaskButton(ActionEvent event) {
        if (!pomTaskText.getText().equals("")) {
            model.addTask(pomTaskText.getText(), pomSpinner.getValue());
            updateTaskList();
            pomTaskText.setText("");
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
            pomSpinner.setValueFactory(valueFactory);
        }
    }

    public void deleteSelectedTask(ActionEvent event) {
        int lastClickedTaskIndex = taskList.getSelectionModel().getSelectedIndex();
        if (lastClickedTaskIndex >= 0) {
            model.removeTask(lastClickedTaskIndex);
            updateTaskList();
        }
    }

    public void editSelectedTaskName(ActionEvent event) {
        int lastClickedTaskIndex = taskList.getSelectionModel().getSelectedIndex();
        if (lastClickedTaskIndex >= 0) {
            TextInputDialog dialog = new TextInputDialog(model.getTasksList().get(lastClickedTaskIndex).getName());
            dialog.setTitle("Edit Name");
            dialog.setHeaderText("Edit Task Description");
            dialog.setContentText("Enter task:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                String newName = result.get();
                if (!newName.equals("")) {
                    model.editTaskName(newName, lastClickedTaskIndex);
                    updateTaskList();
                }
            }
        }
    }

    public void editSelectedTaskNumPomodoros(ActionEvent event) {
        int lastClickedTaskIndex = taskList.getSelectionModel().getSelectedIndex();
        if (lastClickedTaskIndex >= 0) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Edit # Pomodoros");
            dialog.setHeaderText("Edit Number of Pomodoros");
            Label pomLabel = new Label("Est Pomodoros");
            final Spinner<Integer> dialogPomSpinner = new Spinner<>();
            dialogPomSpinner.setEditable(true);
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, model.getTasksList().get(lastClickedTaskIndex).getNumPomodoros());
            dialogPomSpinner.setValueFactory(valueFactory);
            dialogPomSpinner.setStyle("-fx-cursor: hand;");
            HBox editPomBox = new HBox(10, pomLabel, dialogPomSpinner);
            ButtonType OKButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().setContent(editPomBox);
            dialog.getDialogPane().getButtonTypes().addAll(OKButton, ButtonType.CANCEL);
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == OKButton) {
                    model.editNumPomodoros(dialogPomSpinner.getValue(), lastClickedTaskIndex);
                    updateTaskList();
                }
                return null;
            });
            dialog.showAndWait();
        }
    }

    private void updateTaskList() {
        List<ITask> tasks = model.getTasksList();
        List<HBox> taskItems = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            HBox buttonAndNameBox = new HBox(5);
            //Creating a graphic (image)
            Image img = new Image("pomtimer/img/checkmark.png");
            ImageView checkmarkView = new ImageView(img);
            checkmarkView.setFitHeight(25);
            checkmarkView.setFitWidth(25);
            Button checkmarkButton = new Button("", checkmarkView);

            String taskText = model.getTasksList().get(i).getName();
            String originalText = model.getTasksList().get(i).getName();
            if (taskText.length() >= 29) {
                taskText = getTextWithRevisedSpacing(originalText);
            }
            Text taskName = new Text(taskText);

            int finalI = i;
            EventHandler<ActionEvent> buttonHandler = event -> {
                model.checkTask(finalI);

                if (model.getTasksList().get(finalI).isCompleted()) {
                    checkmarkView.setOpacity(0.75);
                    taskName.setStrikethrough(true);
                } else {
                    checkmarkView.setOpacity(0.25);
                    taskName.setStrikethrough(false);
                }
                checkmarkButton.setStyle("-fx-background-color: transparent;");
                currentTaskLabel.setText("Working on: " + model.getCurrentTask());
            };
            checkmarkButton.setOnAction(buttonHandler);

            checkmarkButton.setStyle("-fx-background-color: transparent;");
            if (model.getTasksList().get(finalI).isCompleted()) {
                checkmarkView.setOpacity(0.75);
                taskName.setStrikethrough(true);
            } else {
                checkmarkView.setOpacity(0.25);
                taskName.setStrikethrough(false);
            }

            buttonAndNameBox.getChildren().addAll(checkmarkButton, taskName);
            buttonAndNameBox.setPrefWidth(taskText.length() + 200);
            buttonAndNameBox.setAlignment(Pos.CENTER_LEFT);

            Label taskPomNum = new Label(String.valueOf(model.getTasksList().get(i).getNumPomodoros()));
            double spacing = 170 - taskText.length();
            HBox taskBox = new HBox(spacing, buttonAndNameBox, taskPomNum);
            taskBox.setStyle("-fx-cursor: hand");
            taskItems.add(taskBox);
            taskBox.setAlignment(Pos.CENTER_LEFT);

        }
        ObservableList<HBox> taskHBoxItems = FXCollections.observableList(taskItems);
        taskList.setItems(taskHBoxItems);

        currentTaskLabel.setText("Working on: " + model.getCurrentTask());
    }

    private String getTextWithRevisedSpacing(String originalText) {
        int j = 0;
        String taskText = "";
        while (originalText.length() > taskText.length()) {
            if (originalText.length() - taskText.length() < 29) {
                taskText += originalText.substring(j);
            } else {
                String currText = originalText.substring(j, j + 29);
                int lastInd = currText.lastIndexOf(" ");
                if (lastInd == -1) {
                    String row = originalText.substring(j, j + 29);
                    taskText += row + "\n";
                    j += 29;
                } else {
                    taskText += originalText.substring(j, j + lastInd + 1) + "\n";
                    j = j + lastInd + 1;
                }
            }
        }
        return taskText;
    }

    private void setBackgroundColor() {
        if (model.isOnPomodoro()) {
            background.setStyle("-fx-background-color: #F08A8A;");
        } else if (model.isOnShortBreak()) {
            background.setStyle("-fx-background-color: #93D7A7;");
        } else {
            background.setStyle("-fx-background-color: #93CED7;");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        pomSpinner.setValueFactory(valueFactory);

        timerLabel.setText(model.getRemainingTime());
        pomodoroNumLabel.setText("Pomodoro Number: " + model.getNumPomodoros());

        background.setStyle("-fx-background-color: #F08A8A;");
    }

}
