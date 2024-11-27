package com.instructor.main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DanceInstructorUI extends Application {

    private Scene mainScene;
    private Button startButton;
    private Button inputFileButton;
    private Button doneButton;
    private Button userButton;
    private Button profButton;
    private Button backButton;

    @Override
    public void start(Stage primaryStage) {

        // Main Screen
        BorderPane mainLayout = new BorderPane();

        // Button Controls
        startButton = new Button("Start Recording");
        startButton.setPrefWidth(180);
        startButton.setFont(new Font("Georgia", 20));

        inputFileButton = new Button("Input File");
        inputFileButton.setPrefWidth(180);
        inputFileButton.setFont(new Font("Georgia", 20));

        doneButton = new Button("Done");
        doneButton.setPrefWidth(180);
        doneButton.setFont(new Font("Georgia", 20));

        userButton = new Button("User");
        userButton.setPrefWidth(180);
        userButton.setFont(new Font("Georgia", 20));

        profButton = new Button("Pro");
        profButton.setPrefWidth(180);
        profButton.setFont(new Font("Georgia", 20));

        backButton = new Button("Back");
        backButton.setPrefWidth(180);
        backButton.setFont(new Font("Georgia", 20));

        // Event Handlers
        DanceInstructorUIController controller = new DanceInstructorUIController(primaryStage, startButton,
                inputFileButton, doneButton, userButton, profButton, backButton);

        // VBox for instructions
        VBox instructionsVbox = new VBox(20);
        instructionsVbox.setPadding(new Insets(15));
        instructionsVbox.setAlignment(Pos.CENTER);

        // Create instructions label at top of screen
        Label instructionsLabel = new Label("Please input a video file to compare.");
        instructionsLabel.setFont(new Font("Georgia", 30));

        HBox buttonBox = new HBox(10, userButton, profButton);
        buttonBox.setAlignment(Pos.CENTER);

        instructionsVbox.getChildren().addAll(instructionsLabel, buttonBox, doneButton);

        // Add elements to main layout
        mainLayout.setCenter(instructionsVbox);

        mainScene = new Scene(mainLayout, 600, 400);

        controller.setMainScene(mainScene);

        // Set up the stage
        primaryStage.setTitle("Camera Input App");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}