package com.instructor.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ApplicationHandler {

        /**
         * Method to capture user video for pose estimation
         */
        public boolean runCapturePoseEstimation() {
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

                        // Return true if exit code is 0, otherwise false
                        return exitCode == 0;

                } catch (Exception e) {
                        e.printStackTrace();
                        return false; // Return false in case of an exception
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
         * Generates feedback by invoking a Python script that compares dance frame
         * sets.
         *
         * @param prompt The input prompt containing details for the comparison.
         * @return A string containing the feedback result from the Python script.
         *         Returns null if an error occurs during script execution.
         */
        public String generateFeedbackAPI(String prompt) {
                // The prompt to send to the python script
                String base = "Provide feedback based on the comparison for the user to improve their 3D motion alignment. Focus on the following: "
                                + "Analyze the differences between the user and pro keypoints (x, y, z coordinates)."
                                + "Offer specific guidance on how to adjust the user's motion in terms of body part positions, alignment, and timing."
                                + "Suggest improvements in terms of spatial coordination to match the pro's movement."
                                + prompt
                                + ".";

                // The python script path
                String pythonScriptPath = "./web_call/AI_Call.py";

                try {
                        // Build command to execute Python script
                        ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath, base);

                        // Start the process
                        Process process = processBuilder.start();

                        // Read the output of the script
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                        StringBuilder feedback = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                                feedback.append(line).append("\n");
                        }

                        // wait for the process to finish
                        process.waitFor();

                        return feedback.toString(); // Return feedback

                } catch (Exception e) {
                        e.printStackTrace();
                }
                return null;
        }
}
