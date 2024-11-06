package com.instructor.main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DanceInstructorUI extends Application {
    
    private Scene mainScene;
    private Label recordingLabel = null;

    @Override
    public void start(Stage primaryStage) {
        
        // Main Screen
        BorderPane mainLayout = new BorderPane();

        // Placeholder for Camera Feed
        StackPane cameraPane = new StackPane();
        Rectangle cameraPlaceholder = new Rectangle(400, 300, Color.GRAY); // Placeholder for camera input
        cameraPane.getChildren().add(cameraPlaceholder);

        // Button Controls
        Button startButton = new Button("Start Recording");
        Button stopButton = new Button("Stop Recording");
        Button inputFileButton = new Button("Input File");
        Button doneButton = new Button("Done");

        // Button Vbox
        VBox buttonVbox = new VBox(20);
        buttonVbox.setPadding(new Insets(15));
        buttonVbox.setAlignment(Pos.TOP_CENTER);

        // Button actions
        startButton.setOnAction(e -> {
            if (recordingLabel == null) {
                recordingLabel = new Label("Starting recording...");
                buttonVbox.getChildren().add(recordingLabel);
            }
        });
        stopButton.setOnAction(e -> {
            if (recordingLabel != null) {
                recordingLabel.setText("Recording stopped");
            }
            else {
                recordingLabel = new Label("Recording has not started, please start recording.");
                buttonVbox.getChildren().add(recordingLabel);
            }
        });
        inputFileButton.setOnAction(e -> System.out.println("Inputting file..."));

        // Action for the Done button
        doneButton.setOnAction(e -> showFeedbackScreen(primaryStage));

        // Arrange buttons in HBox
        HBox buttonBox = new HBox(10, startButton, stopButton, inputFileButton, doneButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10));

        // Add elements to main layout
        mainLayout.setCenter(cameraPane);
        mainLayout.setBottom(buttonBox);
        mainLayout.setTop(buttonVbox);

        mainScene = new Scene(mainLayout, 600, 400);
        
        // Set up the stage
        primaryStage.setTitle("Camera Input App");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    // Method to show the feedback screen
    private void showFeedbackScreen(Stage stage) {
        // Feedback screen layout
        VBox feedbackLayout = new VBox(20);
        feedbackLayout.setAlignment(Pos.TOP_LEFT);
        feedbackLayout.setPadding(new Insets(20));
        
        // Label for feedback
        Label feedbackLabel = new Label("Feedback:");

        // Back button
        Button backButton = new Button("Back");

        // Displays main scene
        backButton.setOnAction(e -> {
            stage.setScene(mainScene);
        });
        
        feedbackLayout.getChildren().addAll(backButton,feedbackLabel);

        Scene feedbackScene = new Scene(feedbackLayout, 600, 400);
        stage.setScene(feedbackScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
