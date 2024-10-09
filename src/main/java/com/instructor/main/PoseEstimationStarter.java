package com.instructor.main;

import com.instructor.data.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class PoseEstimationStarter {

    // Method to run the Python script
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
    

    public static void main(String[] args) {
        // Create the entry point for communication
        PoseEstimationStarter handler = new PoseEstimationStarter();
        
        System.out.println("Starting video capture from Python...");
        handler.runPythonScript();
        
        // Instantiate PoseDataReader and call displayPoseData()
        PoseDataReader poseDataReader = new PoseDataReader();
        poseDataReader.displayPoseData("keypoints_data.txt");
    }
}
