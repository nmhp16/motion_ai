package com.danceinstructor.connector;

import com.danceinstructor.service.PoseDetectionService;

import py4j.GatewayServer;

public class JavaApp {
    public static void main(String[] args) {
        // Start the Py4J Gateway
        GatewayServer gatewayServer = new GatewayServer();
        gatewayServer.start();
        System.out.println("Gateway Server Started!");

        // Access the Python pose detection service
        PoseDetectionService pythonService = (PoseDetectionService) gatewayServer.getPythonServerEntryPoint(PoseDetectionService.class);

        // Pass the image path to Python and get pose detection results
        String imagePath = "path/to/your/image.jpg";  // Replace with actual image path
        String result = pythonService.detect_pose(imagePath);

        // Print the result
        System.out.println(result);

        // Shut down the Gateway
        gatewayServer.shutdown();
    }
}
