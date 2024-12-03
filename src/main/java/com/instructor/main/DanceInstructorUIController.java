package com.instructor.main;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.instructor.algorithms.DynamicTimeWarping;
import com.instructor.controller.ApplicationHandler;
import com.instructor.evaluation.PoseFeedback;
import com.instructor.evaluation.PoseScoring;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DanceInstructorUIController {
	private Button startButton;
	private Button inputButton;
	private Button doneButton;
	private Stage primaryStage;
	private Button userButton;
	private Button profButton;
	private Button backButton;
	private Scene mainScene;

	public static Map<String, Map<Integer, float[]>> userKeypointsMap = new HashMap<>();
	public static Map<String, Map<Integer, float[]>> proKeypointsMap = new HashMap<>();
	private PoseFeedback poseFeedback = new PoseFeedback();
	private PoseScoring poseScoring = new PoseScoring();
	public static boolean isUserInput = false;
	public static boolean isProInput = false;
	private boolean isPartChosen = false;

	public DanceInstructorUIController(Stage primaryStage, Button startButton, Button inputButton, Button doneButton,
			Button userButton, Button profButton, Button backButton) {
		this.startButton = startButton;
		this.inputButton = inputButton;
		this.doneButton = doneButton;
		this.primaryStage = primaryStage;
		this.userButton = userButton;
		this.profButton = profButton;
		this.backButton = backButton;

		setupEventHandlers();
	}

	// Set up button event handlers
	private void setupEventHandlers() {

		doneButton.setOnAction(event -> {

			if (isUserInput && isProInput) {
				showOption();
			} else {
				if (!isUserInput && !isProInput) {
					// Show alert to input missing file
					showError("Input Missing", "Please input a user video and a professional video.");
				} else if (!isUserInput) {
					// Show alert to input missing file
					showError("Input Missing", "Please input a user video.");
				} else if (!isProInput) {
					// Show alert to input missing file
					showError("Input Missing", "Please input a professional video.");
				}
			}
		});

		// Set up button event handlers for user
		userButton.setOnAction(event -> handleUserButton());
		// Set up button event handlers for pro
		profButton.setOnAction(event -> handleProfButton());

		backButton.setOnAction(event -> {
			showMainScreen();
		});
	}

	/**
	 * Handles the event when the user button is pressed.
	 * Arranges the buttons in an HBox, sets up the action for the input button
	 * to open the file chooser for user input, reads user keypoints from "User.txt"
	 * file, and sets the scene with the arranged buttons.
	 */
	private void handleUserButton() {
		// Arrange buttons in HBox
		HBox buttonBox = new HBox(10, startButton, inputButton, backButton);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setPadding(new Insets(10));

		startButton.setOnAction(event -> startVideoCapture(primaryStage, "beginner"));
		inputButton.setOnAction(e -> openFileChooser(primaryStage, "beginner"));

		primaryStage.setScene(new Scene(buttonBox, 600, 400));
	}

	/**
	 * Handles the event when the professional button is pressed.
	 * Arranges the input and back buttons in an HBox, sets up the action for
	 * the input button to open the file chooser for professional input,
	 * reads professional keypoints from "Pro.txt" file, and sets the scene
	 * with the arranged buttons.
	 */
	private void handleProfButton() {
		// Arrange buttons in HBox
		HBox buttonBox = new HBox(10, startButton, inputButton, backButton);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setPadding(new Insets(10));

		startButton.setOnAction(event -> startVideoCapture(primaryStage, "pro"));
		inputButton.setOnAction(e -> openFileChooser(primaryStage, "pro"));
		primaryStage.setScene(new Scene(buttonBox, 600, 400));
	}

	/**
	 * Starts the video capture process if it has not started yet.
	 * 
	 * If the recording has already started, it will not do anything.
	 * 
	 * @see #stopVideoCapture()
	 */
	private void startVideoCapture(Stage stage, String videoType) {
		ApplicationHandler handler = new ApplicationHandler();
		new Thread(() -> {
			handler.runCapturePoseEstimation(videoType);
		}).start();
	}

	/**
	 * Opens a FileChooser and allows the user to select a file to upload to the
	 * application. Currently, only .mp4 files are supported.
	 * 
	 * @param stage The stage to open the FileChooser on
	 */
	private void openFileChooser(Stage stage, String videoType) {

		// Create a FileChooser
		FileChooser fileChooser = new FileChooser();

		// Allow all file types
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));

		// Open the file chooser and get the selected file
		File selectedFile = fileChooser.showOpenDialog(primaryStage);

		ApplicationHandler handler = new ApplicationHandler();
		if (selectedFile != null) {
			String fileName = selectedFile.getName();
			if (fileName.endsWith(".mp4")) {
				new Thread(() -> {
					handler.runUploadPoseEstimation(selectedFile.getPath(), videoType);
				}).start();
			} else {
				System.out.println("The selected file is invalid. Please upload a .mp4 file.");
			}
		} else {
			System.out.println("The selected file is invalid. Please upload a .mp4 file.");
		}

	}

	/**
	 * Displays a new scene with a label and a back button.
	 * 
	 * The label displays the string "Feedback:".
	 * The back button displays the string "Back" and when clicked, sets the scene
	 * back to the main scene.
	 * 
	 * @param stage The stage to set the new scene on
	 */
	private void showFeedbackScreen(Stage stage, String userInput) {

		// Show ProgressBar
		showProgressBar(stage);

		// Run processing in a separate thread
		new Thread(() -> {

			// Calculate similarity score between user and pro based on total distance
			// difference
			float similarityScore = DynamicTimeWarping.totalDtw(userKeypointsMap, proKeypointsMap);

			// Assume max similarity and calculate total score
			float maxSimilarity = 4.0f; // Replace with actual value

			// DTW Score
			int finalScore = poseScoring.calculateScore(similarityScore, maxSimilarity);

			ApplicationHandler handler = new ApplicationHandler();

			// Feedback screen layout
			VBox feedbackLayout = new VBox(20);
			feedbackLayout.setAlignment(Pos.TOP_LEFT);
			feedbackLayout.setPadding(new Insets(20));

			// Label for feedback
			Label feedbackLabel = new Label("Feedback:");
			TextArea feedbackTextArea = new TextArea();
			feedbackTextArea.setEditable(false);
			feedbackTextArea.setPrefSize(700, 600);

			feedbackTextArea.appendText("Final Score (out of 100): " + finalScore);

			feedbackTextArea.appendText("\n\n");

			feedbackTextArea.appendText(poseFeedback.provideFeedback(finalScore));

			feedbackTextArea.appendText("\n\n");

			if (finalScore < 90) {
				String prompt = poseScoring.generateComparisonPrompt(userKeypointsMap, proKeypointsMap,
						userInput);

				feedbackTextArea.appendText(handler.generateFeedbackAPI(prompt)); // Generate feedback
			} else {
				feedbackTextArea.appendText("No feedback needed.");
			}

			// Back button
			Button backButton = new Button("Back");

			// Displays main scene
			backButton.setOnAction(e -> {
				isPartChosen = false;

				showMainScreen();
			});

			feedbackLayout.getChildren().addAll(backButton, feedbackLabel, feedbackTextArea);

			Scene feedbackScene = new Scene(feedbackLayout, 800, 700);
			Platform.runLater(() -> stage.setScene(feedbackScene));
		}).start();

	}

	/**
	 * Displays a loading screen with a progress bar on the given stage.
	 * 
	 * @param stage The stage on which to set the loading scene.
	 */
	private void showProgressBar(Stage stage) {
		// Show loading screen with progress bar
		ProgressBar progressBar = new ProgressBar();
		progressBar.setPrefWidth(300);
		Label loadingLabel = new Label("Loading, please wait...");
		loadingLabel.setFont(new Font("Georgia", 20));
		VBox loadingLayout = new VBox(20, loadingLabel, progressBar);
		loadingLayout.setAlignment(Pos.CENTER);
		Scene loadingScene = new Scene(loadingLayout, 800, 700);
		stage.setScene(loadingScene);
	}

	/**
	 * Displays the main scene on the primary stage.
	 */
	private void showMainScreen() {

		primaryStage.setScene(mainScene);
	}

	/**
	 * Sets the main scene for the application to the given scene.
	 * 
	 * @param scene The main scene for the application.
	 */
	public void setMainScene(Scene scene) {
		this.mainScene = scene;
	}

	/**
	 * Shows an option screen with a list of valid body parts and a text input
	 * field.
	 * The user can input a body part, and the application will show a feedback
	 * screen
	 * with the corresponding feedback for the input body part.
	 */
	private void showOption() {

		// Define valid body parts
		List<String> validBodyParts = List.of(
				"shoulder_left", "shoulder_right",
				"elbow_left", "elbow_right",
				"wrist_left", "wrist_right",
				"hip_left", "hip_right",
				"knee_left", "knee_right",
				"ankle_left", "ankle_right",
				"heel_left", "heel_right",
				"foot_index_left", "foot_index_right",
				"nose");

		// Create layout with padding and alignment
		VBox optionLayout = new VBox(15);
		optionLayout.setAlignment(Pos.CENTER);
		optionLayout.setPadding(new Insets(20)); // Add padding for cleaner spacing

		// Style the available options label to improve readability
		Label availableOptionsLabel = new Label("Available body parts:");
		availableOptionsLabel.setFont(new Font("Georgia", 20));

		// List body parts in a scrollable area for better user experience
		ListView<String> bodyPartsListView = new ListView<>();
		bodyPartsListView.getItems().addAll(validBodyParts);
		bodyPartsListView.setPrefHeight(150); // Set height to make it scrollable if necessary

		// Label for the text input prompt
		Label label = new Label("Enter body part:");
		label.setFont(new Font("Georgia", 20));

		// TextField for user input with styling
		TextField textField = new TextField();
		textField.setPromptText("e.g. shoulder_left");
		textField.setPrefHeight(30);

		// Submit button with styling
		Button button = new Button("Submit");
		button.setFont(new Font("Georgia", 16));

		// Select body part on double click
		bodyPartsListView.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {
				String selectedPart = bodyPartsListView.getSelectionModel().getSelectedItem();
				textField.setText(selectedPart);
			}
		});

		// Add elements to the layout
		optionLayout.getChildren().addAll(availableOptionsLabel, bodyPartsListView, label, textField, button);

		// Create a new stage
		Stage stage = new Stage();

		// Set button action to submit the input
		button.setOnAction(event -> {
			String userInput = textField.getText().trim(); // Get trimmed input
			isPartChosen = true;

			if (poseScoring.isPartNeeded(userInput) && isPartChosen) {
				stage.close();
				showFeedbackScreen(primaryStage, userInput); // Show feedback with the user input if valid
			} else {
				// Show error if the input is invalid
				showError("Invalid input!", "Please select a valid body part.");
			}
		});

		// Set up and show the scene
		Scene scene = new Scene(optionLayout, 400, 500);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Shows an error dialog with the given title and message.
	 * 
	 * @param title   The title of the error dialog.
	 * @param message The message of the error dialog.
	 */
	private void showError(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(message);
		alert.showAndWait();
	}

}
