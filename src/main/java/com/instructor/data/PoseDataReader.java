package com.instructor.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class PoseDataReader {

	// Method to read keypoints data from a text file, takes a file path as an argument
    public Map<String, Map<Integer, float[]>> readKeypointsFromFile(String filePath) {
        Map<String, Map<Integer, float[]>> keypointsMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentKeypoint = null;
            
            while ((line = reader.readLine()) != null) {
                // Remove leading/trailing whitespace
                line = line.trim();
                
                // Check if the line indicates a new keypoint
                if (line.endsWith(":")) {
                    // Get keypoint name
                    currentKeypoint = line.substring(0, line.length() - 1).trim();
                    // Initialize a Map for the frames
                    keypointsMap.put(currentKeypoint, new HashMap<>());  
                    
                } else if (currentKeypoint != null) {
                    // Line should be in the format: Frame x: x=val, y=val, z=val
                    String[] frameParts = line.split(":");
                    
                    if (frameParts.length > 1) {
                        String frameInfo = frameParts[0].trim();
                        // Get the frame number
                        int frameNumber = Integer.parseInt(frameInfo.split(" ")[1]);
                        
                        String[] coordinates = frameParts[1].trim().split(",");
                        float[] values = new float[3];
                        
                        for (int i = 0; i < coordinates.length; i++) {
                            String[] coord = coordinates[i].trim().split("=");
                            values[i] = Float.parseFloat(coord[1].trim());
                        }
                        // Add values to the current keypoints Map
                        keypointsMap.get(currentKeypoint).put(frameNumber, values);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keypointsMap;
    }
    
    
    // Method to display keypoints data
    public void displayPoseData(String filePath) {
        Map<String, Map<Integer, float[]>> keypointsData = readKeypointsFromFile(filePath);
        System.out.println("Received Pose Data:");
        
        for (String keypoint : keypointsData.keySet()) {
            System.out.println(keypoint + ":");
            Map<Integer, float[]> frames = keypointsData.get(keypoint);
            
            for (Integer frame : frames.keySet()) {
                float[] coordinates = frames.get(frame);
                System.out.printf("  Frame %d: x=%.4f, y=%.4f, z=%.4f%n", frame, coordinates[0], coordinates[1], coordinates[2]);
            }
        }
    }
}
