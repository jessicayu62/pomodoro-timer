package main.java.controllers;

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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.java.model.IPomodoroTimer;
import main.java.model.ITask;
import main.java.model.PomodoroTimer;

import java.net.URL;
import java.util.*;

/**
 * The controller for the PomodoroTimer class in which the user's interactions are communicated from this controller
 * class to the model. Prepares the view based on user interaction.
 */
public class Controller implements Initializable {
    @FXML
    public Button addTaskButton;
    @FXML
    public Label currentTaskLabel;
    @FXML
    public Button deleteButton;
    @FXML
    public Button editNameButton;
    @FXML
    public Button editNumPomButton;
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

    private final IPomodoroTimer model;
    boolean isRunning;

    public Controller() {
        model = new PomodoroTimer();
        isRunning = false;
        Timeline updateTimer = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            timerLabel.setText(model.getRemainingTime());
                            if (model.getRemainingTime().equals("00:00")) {
                                playTimerSound();
                                this.isRunning = false;
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
        if (isRunning) {
            boolean result = resultFromConfirmDialog("Confirm Switch", "Click OK to continue switching.");
            if (!result) {
                return;
            }
        }
        this.isRunning = false;
        model.resetTimer();
        model.goToPomodoro();
        timerLabel.setText(model.getRemainingTime());
        setBackgroundColor();
        resetButtons();
    }

    public void pressShortBreakButton(ActionEvent event) {
        if (isRunning) {
            boolean result = resultFromConfirmDialog("Confirm Switch", "Click OK to continue switching.");
            if (!result) {
                return;
            }
        }
        this.isRunning = false;
        model.resetTimer();
        model.goToShortBreak();
        timerLabel.setText(model.getRemainingTime());
        setBackgroundColor();
        resetButtons();
    }

    public void pressLongBreakButton(ActionEvent event) {
        if (isRunning) {
            boolean result = resultFromConfirmDialog("Confirm Switch", "Click OK to continue switching.");
            if (!result) {
                return;
            }
        }
        this.isRunning = false;
        model.resetTimer();
        model.goToLongBreak();
        timerLabel.setText(model.getRemainingTime());
        setBackgroundColor();
        resetButtons();
    }

    public void pressStartButton(ActionEvent event) {
        this.isRunning = true;
        model.startTimer();
        resetButtons();
    }

    public void pressStopButton(ActionEvent event) {
        this.isRunning = false;
        model.pauseTimer();
        resetButtons();
    }

    public void pressResetButton(ActionEvent event) {
        this.isRunning = false;
        model.resetTimer();
        resetButtons();
    }

    public void pressSkipButton(ActionEvent event) {
        if (this.isRunning) {
            boolean result = resultFromConfirmDialog("Confirm Skipping Round",
                    "Click OK to continue finishing the round early.");
            if (!result) {
                return;
            }
        }
        this.isRunning = false;
        model.skipTimer();
        pomodoroNumLabel.setText("Pomodoro Number: " + model.getNumPomodoros());
        timerLabel.setText(model.getRemainingTime());
        setBackgroundColor();
        resetButtons();
    }

    private boolean resultFromConfirmDialog(String titleText, String contextText) {
        resetButtons();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titleText);
        alert.setHeaderText("The timer is still running!");
        alert.setGraphic(new ImageView(Objects.requireNonNull(getClass().getClassLoader().getResource("main/resources/img/timer.png")).toString()));
        alert.setContentText(contextText);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public void pressAddTaskButton(ActionEvent event) {
        if (!pomTaskText.getText().equals("")) {
            model.addTask(pomTaskText.getText(), pomSpinner.getValue());
            updateTaskList();
            pomTaskText.setText("");
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
            pomSpinner.setValueFactory(valueFactory);
        }
        resetButtons();
    }

    public void deleteSelectedTask(ActionEvent event) {
        int lastClickedTaskIndex = taskList.getSelectionModel().getSelectedIndex();
        if (lastClickedTaskIndex >= 0) {
            model.removeTask(lastClickedTaskIndex);
            updateTaskList();
        }
        resetButtons();
    }

    public void editSelectedTaskName(ActionEvent event) {
        int lastClickedTaskIndex = taskList.getSelectionModel().getSelectedIndex();
        if (lastClickedTaskIndex >= 0) {
            TextInputDialog dialog = new TextInputDialog(model.getTasksList().get(lastClickedTaskIndex).getName());
            dialog.setTitle("Edit Name");
            dialog.setHeaderText("Edit Task Description");
            dialog.setGraphic(new ImageView(Objects.requireNonNull(getClass().getClassLoader().getResource("main/resources/img/edit.png")).toString()));
            dialog.setContentText("Enter task:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String newName = result.get();
                if (!newName.equals("")) {
                    model.editTaskName(newName, lastClickedTaskIndex);
                    updateTaskList();
                }
            }
        }
        resetButtons();
    }

    public void editSelectedTaskNumPomodoros(ActionEvent event) {
        int lastClickedTaskIndex = taskList.getSelectionModel().getSelectedIndex();
        if (lastClickedTaskIndex >= 0) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Edit # Pomodoros");
            dialog.setHeaderText("Edit Number of Pomodoros");
            dialog.setGraphic(new ImageView(Objects.requireNonNull(getClass().getClassLoader().getResource("main/resources/img/edit.png")).toString()));
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
        resetButtons();
    }

    private void updateTaskList() {
        List<ITask> tasks = model.getTasksList();
        List<HBox> taskItems = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            HBox buttonAndNameBox = new HBox(5);
            //Creating a graphic (image)
            Image img = new Image("main/resources/img/checkmark.png");
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
            taskName.setFont(Font.font("Verdana", 13));

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

            Text taskPomNum = new Text(String.valueOf(model.getTasksList().get(i).getNumPomodoros()));
            taskPomNum.setFont(Font.font("Verdana", 13));
            double spacing = 170 - taskText.length();
            HBox taskBox = new HBox(spacing, buttonAndNameBox, taskPomNum);
            taskBox.setStyle("-fx-cursor: hand");
            taskBox.setOnMouseClicked((e) ->
            {
                int lastClickedIndex = taskList.getSelectionModel().getSelectedIndex();
                deleteButton.setDisable(lastClickedIndex < 0);
                editNameButton.setDisable(lastClickedIndex < 0);
                editNumPomButton.setDisable(lastClickedIndex < 0);
            });
            taskItems.add(taskBox);
            taskBox.setAlignment(Pos.CENTER_LEFT);

        }
        if (model.getTasksList().isEmpty()) {
            Text addTaskText = new Text("");
            ObservableList<HBox> items = FXCollections.observableArrayList(new HBox(addTaskText));
            taskList.setItems(items);
        } else {
            ObservableList<HBox> taskHBoxItems = FXCollections.observableList(taskItems);
            taskList.setItems(taskHBoxItems);
        }

        currentTaskLabel.setText("Working on: " + model.getCurrentTask());
    }

    private String getTextWithRevisedSpacing(String originalText) {
        int j = 0;
        StringBuilder taskText = new StringBuilder();
        while (originalText.length() > taskText.length()) {
            if (originalText.length() - taskText.length() < 29) {
                taskText.append(originalText.substring(j));
            } else {
                String currText = originalText.substring(j, j + 29);
                int lastInd = currText.lastIndexOf(" ");
                if (lastInd == -1) {
                    String row = originalText.substring(j, j + 29);
                    taskText.append(row).append("\n");
                    j += 29;
                } else {
                    taskText.append(originalText, j, j + lastInd + 1).append("\n");
                    j = j + lastInd + 1;
                }
            }
        }
        return taskText.toString();
    }

    private void playTimerSound() {
        String path = Objects.requireNonNull(getClass().getClassLoader().getResource("main/resources/sounds/timerSound.mp3")).toString();
        Media sound = new Media(path);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
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

    private void resetButtons() {
        deleteButton.setDisable(true);
        editNameButton.setDisable(true);
        editNumPomButton.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        pomSpinner.setValueFactory(valueFactory);

        timerLabel.setText(model.getRemainingTime());
        pomodoroNumLabel.setText("Pomodoro Number: " + model.getNumPomodoros());

        pomTaskText.textProperty().addListener((e) -> {
            deleteButton.setDisable(true);
            editNameButton.setDisable(true);
            editNumPomButton.setDisable(true);
            addTaskButton.setDisable(pomTaskText.getText().trim().isEmpty());
        });

        Text addTaskText = new Text("");
        ObservableList<HBox> items = FXCollections.observableArrayList(new HBox(addTaskText));
        taskList.setItems(items);

        background.setStyle("-fx-background-color: #F08A8A;");

        resetButtons();
        addTaskButton.setDisable(true);
    }
}
