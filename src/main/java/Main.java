package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.layout.StackPane;

public class Main extends Application {

    private static StackPane pane = new StackPane();

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("main/resources/pomtimer.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pane.getChildren().add(root);

        Scene scene = new Scene(pane);

        stage.setTitle("Pomodoro Timer");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.show();

    }

    public static void main(String args[]) {
        launch(args);
        System.exit(1);
    }
}