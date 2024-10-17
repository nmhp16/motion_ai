package com.instructor.main;

import com.instructor.data.*;
import com.instructor.evaluation.PoseScoring;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Starter {

    /**
     * Method to run the Python script
     */
    private void runPythonScript() {
        try {
            // Define the command to run the Python script
            String pythonScriptPath = "./pose_detection/PoseDetection.py"; // Relative path
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);

            // Set the redirect error stream to true to capture errors
            processBuilder.redirectErrorStream(true);

            // Start the process
            Process process = processBuilder.start();

            // Read the output of the script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();
            System.out.println("Python script exited with code: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main
     * 
     * @param args
     */
    public static void main(String[] args) {
        // Instantiate PoseDataReader and PoseDataProcessing and Pose Scoring
        PoseDataReader poseDataReader = new PoseDataReader();
        PoseDataProcessing poseDataProcessing = new PoseDataProcessing();
        PoseScoring poseScoring = new PoseScoring();

        // Map <Keypoints (ex: left_leg), Map<Frame (ex: 1), coordinates (x,y,z)>>
        // Instantiate Hashmap to store user key points
        Map<String, Map<Integer, float[]>> userKeypointsMap = new HashMap<>();
        Map<String, Map<Integer, float[]>> proKeypointsMap = new HashMap<>();

        // Create the entry point for communication
        Starter handler = new Starter();

        // TODO: Remove // Once done testing
        // Start video to capture user dance, and generate a .txt file
        // System.out.println("Starting video capture from Python...");
        // handler.runPythonScript();

        // TODO: Remove // Once done testing
        // Read this .txt file to populate userKeypoints
        // userKeypointsMap = poseDataReader.readKeypointsFromFile("userKeypoints.txt");
        
        userKeypointsMap = poseDataReader
                .readKeypointsFromFile("./motion_database/ballet_spin/beginner_ballet_spin.txt");
        proKeypointsMap = poseDataReader.readKeypointsFromFile("./motion_database/ballet_spin/pro_ballet_spin.txt");

        // Normalize the keypoints for both user and pro
        Map<String, Map<Integer, float[]>> normalizedUserKeypoints = poseDataProcessing
                .normalizeKeypoints(userKeypointsMap);
        Map<String, Map<Integer, float[]>> normalizedProKeypoints = poseDataProcessing
                .normalizeKeypoints(proKeypointsMap);

        // Calculate similarity score between user and pro
        float similarityScore = poseDataProcessing.calculateSimilarity(normalizedUserKeypoints, normalizedProKeypoints);

        // Assume max similarity
        float maxSimilarity = 3.0f; // Replace with actual value

        // Calculate final score
        int finalScore = poseScoring.calculateScore(similarityScore, maxSimilarity);

        // Output similarity score and final score
        System.out.println("Similarity Score: " + similarityScore);
        System.out.println("Final Score (out of 100): " + finalScore);

    }
}
