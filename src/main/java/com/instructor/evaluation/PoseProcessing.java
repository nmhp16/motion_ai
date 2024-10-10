package com.instructor.evaluation;

import java.util.Map;

public class PoseProcessing {
	
	public Map<String, float[]> normalizeKeypoints(Map<String, Map<Integer, float[]>> keypoints) {

	}

	public static float calculateSimilarity(Map<String, Map<Integer, float[]>> userKeypoints, Map<String, Map<Integer, float[]>> proKeypoints) {

    }
	
	// Calculate Euclidean distance between 2 points 
	public static float calculateDistance(float[] point1, float[] point2) {
        
    }
	
	public float[] scaleKeypoint(float[] keypoint, float scale) {

		
	}
	
	// Calculate the angle between 3 keypoints (joint angles)
	public static double calculateAngle(float[] p1, float[] p2, float[] p3) {

	}
}
