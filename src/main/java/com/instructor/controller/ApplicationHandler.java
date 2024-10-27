package com.instructor.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.FileReader;
import java.io.IOException;

import com.instructor.data.PoseDataProcessing;
import com.instructor.data.PoseDataReader;

public class ApplicationHandler {
        private PoseDataReader poseDataReader;
        private PoseDataProcessing poseDataProcessing;

        public ApplicationHandler() {
                // Initialize PoseDataReader
                this.poseDataReader = new PoseDataReader();
                this.poseDataProcessing = new PoseDataProcessing();
        }

        /**
         * Method to capture user video for pose estimation
         */
        public void runCapturePoseEstimation() {
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
         * Method to upload video for pose estimation
         * 
         * @param videoPath Path to video
         * @param videoType Type of video ("beginner" or "pro")
         */
        public void runUploadPoseEstimation(String videoPath, String videoType) {
                List<String> command = new ArrayList<>();
                command.add("python"); // Depending on setup
                command.add("./pose_detection/PoseUpload.py"); // Path to PoseUpload.py
                command.add("--video");
                command.add(videoPath);
                command.add("--type");
                command.add(videoType);

                try {
                        ProcessBuilder pb = new ProcessBuilder(command);
                        pb.redirectErrorStream(true); // Merge error and output streams
                        Process process = pb.start();

                        // Read the output of the Python script
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                                System.out.println(line); // Print the Python output to Java console
                        }

                        // Wait for the process to complete
                        int exitCode = process.waitFor();

                        if (exitCode == 0) {
                                System.out.println("Pose estimation completed successfully.");
                        } else {
                                System.out.println("Pose estimation encountered an error. Exit code: " + exitCode);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        /**
         * Method to get the most recent file created by user from file name list
         * 
         * @param filePath Location of txt file containing user generated file
         * @return Most recent file name
         */
        public String readLastSavedFileName(String filePath) {
                String lastLine = null;

                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                        String currentLine;

                        while ((currentLine = reader.readLine()) != null) {
                                lastLine = currentLine; // Keep updating lastLine until end of file
                        }
                } catch (IOException e) {
                        System.err.println("Error reading the last saved file name: " + e.getMessage());
                }
                return lastLine;
        }

        /**
         * Method to process the user file for pose estimation
         * 
         * @return Updated map of user keypoints
         */
        public Map<String, Map<Integer, float[]>> processUserFile() {
                Map<String, Map<Integer, float[]>> userKeypointsMap = new HashMap<>(); // Initialize map to store user
                                                                                       // data

                // Read the last saved filename from last_saved_filename.txt
                String keypointsFileName = readLastSavedFileName("last_saved_filename.txt");

                if (keypointsFileName != null) {
                        String filePath = "keypointsFilename";

                        // Use the file name to read keypoints from the newly created file
                        userKeypointsMap = poseDataReader.readKeypointsFromFile(filePath);

                        // Process the keypoints map as needed
                        System.out.println("Successfully read keypoints from: " + filePath);
                } else {
                        System.out.println("Failed to read the keypoints file name.");
                }

                return userKeypointsMap; // Return the populated map
        }

        /**
         * Method to process pose data from given map
         * 
         * @param keypointsMap Map to be process
         * @return Processed map
         */
        public Map<String, Map<Integer, float[]>> processPoseData(Map<String, Map<Integer, float[]>> keypointsMap) {
                Map<String, Map<Integer, float[]>> processedMap; // Initialize keypoints map to store processed data

                // Clean missing keypoints for both user and pro datasets
                processedMap = poseDataProcessing
                                .cleanMissingKeypoints(keypointsMap);

                // Smooth the cleaned keypoints with defined window size
                int windowSize = 5;

                processedMap = poseDataProcessing
                                .smoothKeypoints(processedMap, windowSize);

                // Normalize the cleaned keypoints for both user and pro
                processedMap = poseDataProcessing
                                .normalizeKeypoints(processedMap);

                // TODO: Calculate DTW Score

                return processedMap;
        }
}
