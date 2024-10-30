package com.instructor.main;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DanceInstructorUIController {
	private Button startButton;
	private Button stopButton;
	private Label feedbackLabel;

	public DanceInstructorUIController(Button startButton, Button stopButton, Label feedbackLabel) {
		this.startButton = startButton;
		this.stopButton = stopButton;
		this.feedbackLabel = feedbackLabel;

		setupEventHandlers();
	}

	// Set up button event handlers
	private void setupEventHandlers() {
		startButton.setOnAction(event -> startVideoCapture());
		stopButton.setOnAction(event -> stopVideoCapture());
	}

	// Method to handle start video capture
	private void startVideoCapture() {
		feedbackLabel.setText("Video Capture Started.");
	}

	// Method to handle stop video capture
	private void stopVideoCapture() {
		feedbackLabel.setText("Video Capture Stopped.");
	}

	// Method to display feedback
	public void displayFeedback(String feedback) {
		feedbackLabel.setText("Feedback: " + feedback);
	}
}
