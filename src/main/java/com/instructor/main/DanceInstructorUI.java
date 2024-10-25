package com.instructor.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Test
// TODO: COMPLETE USER INTERFACE BY IMPORTING "DanceInstructorUI.fxml" INTO SCENE BUILDER AND CHANGE IT
public class DanceInstructorUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DanceInstructorUI.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("AI Dance Instructor");
            primaryStage.setScene(new Scene(root, 400, 300));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load FXML fie.");
        }
    }
}
