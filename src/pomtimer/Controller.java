package pomtimer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import pomtimer.model.IPomodoroTimer;
import pomtimer.model.PomodoroTimer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Spinner<Integer> pomSpinner;
    @FXML
    private TextField pomTaskText;
    @FXML
    private ListView<String> taskList = new ListView<>();

    private IPomodoroTimer model;

    public Controller() {
        model = new PomodoroTimer();
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

    }
}
