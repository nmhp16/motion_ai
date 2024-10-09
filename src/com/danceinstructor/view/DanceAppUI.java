package com.danceinstructor.view;

import com.danceinstructor.controller.DanceController;
import com.danceinstructor.model.User;
import com.danceinstructor.model.Feedback;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DanceAppUI extends Application {
    private DanceController controller = new DanceController();
    private User user = new User();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AI Dance Instructor");

        Label promptLabel = new Label("Enter Dance Move:");
        TextField moveInput = new TextField();
        Button analyzeButton = new Button("Analyze");
        Label feedbackLabel = new Label();

        analyzeButton.setOnAction(event -> {
            String move = moveInput.getText();
            Feedback feedback = controller.analyzeDanceMove(user, move);
            feedbackLabel.setText(feedback.getFeedbackText());
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(promptLabel, moveInput, analyzeButton, feedbackLabel);
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
