package com.instructor.main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

// TODO: MAKE SURE TO IMPLEMENT METHOD FOR UI HERE
public class DanceInstructorUIController {
	@FXML private Button startButton;
	@FXML private Button stopButton;
	@FXML private Label feedbackLabel;
	
	// Method to handle start video capture
	@FXML
	public void startVideoCapture() {
		feedbackLabel.setText("Video Capture Started.");
	}
	
	// Method to handle stop video capture
	@FXML
	public void stopVideoCapture() {
		feedbackLabel.setText("Video Capture Stopped.");
	}
	
	// Method to display feedback
	public void displayFeedback(String feedback) {
		feedbackLabel.setText("Feedback: " + feedback);
	}
}
