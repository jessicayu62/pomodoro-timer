package pomtimer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;
import pomtimer.model.IPomodoroTimer;
import pomtimer.model.PomodoroTimer;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    @FXML
    public Label pomodoroNumLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private Spinner<Integer> pomSpinner;
    @FXML
    private TextField pomTaskText;
    @FXML
    private ListView<String> taskList = new ListView<>();

    private IPomodoroTimer model;

    public Controller() {
        model = new PomodoroTimer();
        Timeline updateTimer = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                timerLabel.setText(model.getRemainingTime());
                            }
                        }));
        updateTimer.setCycleCount(Timeline.INDEFINITE);
        updateTimer.play();
    }

    public void pressPomodoroButton(ActionEvent event) {
        model.goToPomodoro();
    }

    public void pressShortBreakButton(ActionEvent event) {
        model.goToShortBreak();
    }

    public void pressLongBreakButton(ActionEvent event) {
        model.goToLongBreak();
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
    }

    public void pressAddTaskButton(ActionEvent event) {
        if (!pomTaskText.getText().equals("")) {
            model.addTask(pomTaskText.getText(), pomSpinner.getValue());
            updateTaskList();
            pomTaskText.setText("");
        }
    }

    private void updateTaskList() {
        List<String> taskNames = new ArrayList<>();
        for (int i=0; i < model.getTasksList().size(); i++) {
            taskNames.add(model.getTasksList().get(i).getName());
        }
        ObservableList<String> taskItems = FXCollections.observableList(taskNames);
        taskList.setItems(taskItems);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        pomSpinner.setValueFactory(valueFactory);

        timerLabel.setText(model.getRemainingTime());
        pomodoroNumLabel.setText("Pomodoro Number: " + model.getNumPomodoros());
    }
}
