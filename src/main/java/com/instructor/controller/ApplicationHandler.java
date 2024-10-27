package com.instructor.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.List;
import java.util.ArrayList;

public class ApplicationHandler {

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

}
