package com.instructor.data;

import java.util.HashMap;
import java.util.Map;

// TODO: COMPLETE THESE TEST CODE FOR POSE PROCESSING
public class PoseDataProcessing {
	/**
	 * Normalize keypoint so people of different body size get compared correctly
	 * 
	 * @param keypoints Map of all keypoints, frame, coordinates
	 * @return Map of normalized keypoints
	 */
	public Map<String, float[]> normalizeKeypoints(Map<String, Map<Integer, float[]>> keypoints) {
		Map<String, float[]> normalizedKeypoints = new HashMap<>();

		// Iterate over keypoints for each frame
		for (String keypoint : keypoints.keySet()) {
			for (Map.Entry<Integer, float[]> entry : keypoints.get(keypoint).entrySet()) {
				int frame = entry.getKey();
				float[] coords = entry.getValue();

				// Assuming you want to calculate torso length between shoulder_left and
				// hip_left
				float[] shoulderLeftCoords = keypoints.get("shoulder_left").get(frame);
				float[] hipLeftCoords = keypoints.get("hip_left").get(frame);
				float torsoLength = calculateDistance(shoulderLeftCoords, hipLeftCoords); // Corrected here

				normalizedKeypoints.put(keypoint + "_frame_" + frame, scaleKeypoint(coords, torsoLength));
			}
		}
		return normalizedKeypoints;
	}

	/**
	 * Compare the similarity between user and pro
	 * 
	 * @param userKeypoints Keypoints for user
	 * @param proKeypoints  Keypoints for pro dancer
	 * @return Similarity between the 2 data
	 */
	public static float calculateSimilarity(Map<String, Map<Integer, float[]>> userKeypoints,
			Map<String, Map<Integer, float[]>> proKeypoints) {
		float totalDistance = 0;

		for (String keypoint : userKeypoints.keySet()) {
			Map<Integer, float[]> userFrames = userKeypoints.get(keypoint);
			Map<Integer, float[]> proFrames = proKeypoints.get(keypoint);

			for (Integer frame : userFrames.keySet()) {
				float[] userCoords = userFrames.get(frame);
				float[] proCoords = proFrames.get(frame);
				totalDistance += calculateDistance(userCoords, proCoords);
			}
		}
		return totalDistance; // Return an inverse score or convert to 0-100 scale
	}

	/**
	 * Calculate Euclidean distance between 2 x, y, z of different frame within the
	 * same keypoints
	 * 
	 * @param point1 Starting point
	 * @param point2 End point
	 * @return Euclidean distance between 2 points
	 */
	public static float calculateDistance(float[] point1, float[] point2) {
		return (float) Math.sqrt(Math.pow(point1[0] - point2[0], 2) + Math.pow(point1[1] - point2[1], 2)
				+ Math.pow(point1[2] - point2[2], 2));
	}

	/**
	 * Dividing the keypoints' coordinates be the reference length (torso length)
	 * 
	 * @param keypoint Coordinates x, y, z
	 * @param scale    Reference length
	 * @return Updated x, y, z value
	 */
	public float[] scaleKeypoint(float[] keypoint, float scale) {
		return new float[] { keypoint[0] / scale, keypoint[1] / scale, keypoint[2] / scale };
	}

	/**
	 * Calculate the angle between 3 keypoints (joint angles)
	 * 
	 * @param p1 X
	 * @param p2 Y
	 * @param p3 Z
	 * @return Angle
	 */
	public static double calculateAngle(float[] p1, float[] p2, float[] p3) {
		float a = calculateDistance(p2, p3);
		float b = calculateDistance(p1, p3);
		float c = calculateDistance(p1, p2);

		return Math.acos((b * b + c * c - a * a) / (2 * b * c));
	}
}
