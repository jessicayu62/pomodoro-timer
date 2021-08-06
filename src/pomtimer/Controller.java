package pomtimer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import pomtimer.model.IPomodoroTimer;
import pomtimer.model.ITask;
import pomtimer.model.PomodoroTimer;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
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
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                timerLabel.setText(model.getRemainingTime());
                                if (model.getRemainingTime().equals("00:00")) {
                                    model.changeToNextOperation();
                                    timerLabel.setText(model.getRemainingTime());
                                    pomodoroNumLabel.setText("Pomodoro Number: " + model.getNumPomodoros());
                                    setBackgroundColor();
                                }
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

    private void updateTaskList() {
//        List<String> taskNames = new ArrayList<>();
//        for (int i = 0; i < model.getTasksList().size(); i++) {
//            taskNames.add(model.getTasksList().get(i).getName());
//        }
//        ObservableList<String> taskItems = FXCollections.observableList(taskNames);
        List<ITask> tasks = model.getTasksList();
        List<HBox> taskItems = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            HBox buttonAndNameBox = new HBox(5);
            Button checkmarkButton = new Button("C");
            String taskText = model.getTasksList().get(i).getName();

            int j = 0;
            if (taskText.length() >= 29) {
                taskText = "";
                while (model.getTasksList().get(i).getName().length() > taskText.length()) {
                    if (model.getTasksList().get(i).getName().length() - taskText.length() < 29) {
                        taskText += model.getTasksList().get(i).getName().substring(j);
                    } else {
                        String currText = model.getTasksList().get(i).getName().substring(j, j + 29);
                        int lastInd = currText.lastIndexOf(" ");
                        taskText += model.getTasksList().get(i).getName().substring(j, j + lastInd + 1) + "\n";
                        j = j + lastInd + 1;
                    }
                }
            }

            Label taskName = new Label(taskText);
            buttonAndNameBox.getChildren().addAll(checkmarkButton, taskName);
            buttonAndNameBox.setPrefWidth(taskText.length() + 200);

            Label taskPomNum = new Label(String.valueOf(model.getTasksList().get(i).getNumPomodoros()));
            double spacing = 170 - taskText.length();
            HBox taskBox = new HBox(spacing, buttonAndNameBox, taskPomNum);
            taskItems.add(taskBox);
        }
        ObservableList<HBox> taskHBoxItems = FXCollections.observableList(taskItems);
        taskList.setItems(taskHBoxItems);
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
