package com.instructor.main;

import com.instructor.data.PoseDataProcessing;
import com.instructor.data.PoseDataReader;
import com.instructor.evaluation.PoseScoring;
import com.instructor.controller.ApplicationHandler;
import com.instructor.controller.FileCleanup;

import java.util.Scanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        // Initialize FileCleanup to clean up saved filenames
        FileCleanup fileCleanup = new FileCleanup();
        Set<String> userHistory = new HashSet<>(); // Initialize Set to store user history
        userHistory = fileCleanup.getExistingFilenames(); // Store user performance history

        // Instantiate PoseDataReader and PoseDataProcessing and Pose Scoring
        PoseDataReader poseDataReader = new PoseDataReader();
        PoseDataProcessing poseDataProcessing = new PoseDataProcessing();
        PoseScoring poseScoring = new PoseScoring();

        // Map <Keypoints (ex: left_leg), Map<Frame (ex: 1), coordinates (x,y,z)>>
        // Instantiate Hashmap to store user key points
        Map<String, Map<Integer, float[]>> userKeypointsMap = new HashMap<>();
        Map<String, Map<Integer, float[]>> proKeypointsMap = new HashMap<>();

        // Create the entry point for communication
        ApplicationHandler handler = new ApplicationHandler();

        // Create a Scanner object for user input
        Scanner scanner = new Scanner(System.in);

        // User input file path
        String userFile = null;

        // User input options
        System.out.println();
        System.out.println("Please select options below:");
        System.out.println("1: Record video.");
        System.out.println("2: Upload video.");
        System.out.println("3: User history.");
        System.out.println("0: Test.");

        int option = scanner.nextInt();
        scanner.nextLine(); // Skip

        switch (option) {
            case 1: // Capture user video
                System.out.println("Starting video capture from Python...");
                handler.runCapturePoseEstimation();

                // Update user history
                fileCleanup.updateSavedFilenamesFile();

                // Process user video to get data
                userKeypointsMap = poseDataReader.readUserFile();

                // Process user data
                userKeypointsMap = poseDataProcessing.processPoseData(userKeypointsMap);

                // TODO: Decision Tree for classification to get correct pro video

                break;

            case 2: // Get user uploaded video
                System.out.println("Enter the path for the user keypoints file: ");
                userFile = scanner.nextLine(); // Assign value to userFile

                // Ensure a valid .mp4 file path is provided
                while (userFile == null || userFile.isEmpty() || !userFile.endsWith(".mp4")) {
                    System.out.println("No valid user file path provided");
                    System.out.println("Enter the path for the user keypoints file: ");
                    userFile = scanner.nextLine(); // Assign value to userFile
                }

                // Allow user to upload video to be compared
                handler.runUploadPoseEstimation(userFile, "Beginner");

                // Update user history
                fileCleanup.updateSavedFilenamesFile();

                // Process user video to get data
                userKeypointsMap = poseDataReader.readUserFile();

                // Process user data
                userKeypointsMap = poseDataProcessing.processPoseData(userKeypointsMap);

                // TODO: Decision Tree for classification to get correct pro video
                break;
            case 3: // Get user history performance
                // TODO: Change this with actual score later and show ranking
                System.out.println("User history: ");

                // Refresh user history to get the latest data from the file
                userHistory = fileCleanup.getExistingFilenames();

                if (userHistory.isEmpty()) {
                    System.out.println("No saved files found in history");
                } else {
                    for (String filename : userHistory) {
                        System.out.println("- " + filename);
                    }
                }
                break;

            default: // Test
                // TODO: Remove once done testing
                // Load keypoints from files
                userKeypointsMap = poseDataReader
                        .readKeypointsFromFile("./motion_database/ballet_spin/beginner.txt");
                proKeypointsMap = poseDataReader.readKeypointsFromFile("./motion_database/ballet_spin/pro.txt");

                userKeypointsMap = poseDataProcessing.processPoseData(userKeypointsMap);
                proKeypointsMap = poseDataProcessing.processPoseData(proKeypointsMap);

                // Calculate similarity score between user and pro
                float similarityScore = poseDataProcessing.calculateSimilarity(userKeypointsMap, proKeypointsMap);

                // Assume max similarity
                float maxSimilarity = 2.0f; // Replace with actual value

                // Calculate final score
                int finalScore = poseScoring.calculateScore(similarityScore, maxSimilarity);

                // Output similarity score and final score
                System.out.println();
                System.out.println("Similarity Score: " + similarityScore);
                System.out.println("Final Score (out of 100): " + finalScore);

                break;
        }

        scanner.close();
    }
}
