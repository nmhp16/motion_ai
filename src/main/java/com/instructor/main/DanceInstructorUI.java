package com.instructor.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// TODO: Complete UI 
public class DanceInstructorUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create root layout
        BorderPane root = new BorderPane();

        // Create VBox layout to hold buttons and feedback label
        VBox vBox = new VBox(10); // 10 px spacing between elements
        vBox.setPrefSize(200, 200);

        // Create UI elements
        Button startButton = new Button("Start Video Capture");
        Button stopButton = new Button("Stop Video Capture");
        Label feedbackLabel = new Label("Feedback: N/A");

        // Add elements to VBox
        vBox.getChildren().addAll(startButton, stopButton, feedbackLabel);

        // Set VBox to center of BorderPane
        root.setCenter(vBox);

        // Instantiate controller and pass UI components to it
        DanceInstructorUIController controller = new DanceInstructorUIController(startButton, stopButton,
                feedbackLabel);

        // Set up the stage
        primaryStage.setTitle("AI Dance Instructor");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }
}
