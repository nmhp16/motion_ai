package com.instructor.data;

import java.util.HashMap;
import java.util.Map;

public class PoseDataProcessing {

	/**
	 * Normalize keypoints so people of different body sizes are compared correctly.
	 *
	 * @param keypoints Map of all keypoints (body parts), frame, coordinates
	 *                  (x,y,z)
	 * @return Map of normalized keypoints for each frame
	 */
	public Map<String, Map<Integer, float[]>> normalizeKeypoints(Map<String, Map<Integer, float[]>> keypoints) {
		Map<String, Map<Integer, float[]>> normalizedKeypoints = new HashMap<>();

		// Iterate over keypoints for each body part
		for (String keypoint : keypoints.keySet()) {
			normalizedKeypoints.put(keypoint, new HashMap<>()); // Initialize frame map
			for (Map.Entry<Integer, float[]> entry : keypoints.get(keypoint).entrySet()) {
				int frame = entry.getKey();
				float[] coords = entry.getValue();

				// Calculate torso length dynamically
				float torsoLength = calculateTorsoLength(keypoints, frame);
				if (torsoLength > 0) {
					normalizedKeypoints.get(keypoint).put(frame, scaleKeypoint(coords, torsoLength));
				}
			}
		}
		return normalizedKeypoints;
	}

	/**
	 * Compare the similarity between user and pro dancer.
	 *
	 * @param userKeypoints Map of user keypoints across frames
	 * @param proKeypoints  Map of pro keypoints across frames
	 * @return Similarity score between the two datasets
	 */
	public float calculateSimilarity(Map<String, Map<Integer, float[]>> userKeypoints,
			Map<String, Map<Integer, float[]>> proKeypoints) {
		float totalDistance = 0;
		int matchingFrames = 0;

		// Iterate over keypoints (e.g., left_leg, right_arm)
		for (String keypoint : userKeypoints.keySet()) {
			Map<Integer, float[]> userFrames = userKeypoints.getOrDefault(keypoint, new HashMap<>());
			Map<Integer, float[]> proFrames = proKeypoints.getOrDefault(keypoint, new HashMap<>());

			for (Integer frame : userFrames.keySet()) {
				float[] userCoords = userFrames.get(frame);
				float[] proCoords = proFrames.get(frame);

				if (userCoords != null && proCoords != null) {
					float distance = calculateDistance(userCoords, proCoords);

					// Consider as a match if the distance is within a certain tolerance
					totalDistance += distance;
					matchingFrames++;
				}
			}
		}

		// Return an inverse score or convert it to a 0-100 scale
		if (matchingFrames > 0) {
			return totalDistance / matchingFrames; // Lower score means more similarity
		} else {
			return Float.MAX_VALUE; // No matching frames, return high "distance"
		}
	}

	/**
	 * Calculate the Euclidean distance between two 3D points.
	 *
	 * @param point1 Coordinates of the first point (x, y, z)
	 * @param point2 Coordinates of the second point (x, y, z)
	 * @return Euclidean distance between the two points
	 */
	public static float calculateDistance(float[] point1, float[] point2) {
		if (point1 == null || point2 == null) {
			throw new IllegalArgumentException("Null points provided for distance calculation.");
		}
		return (float) Math.sqrt(Math.pow(point1[0] - point2[0], 2)
				+ Math.pow(point1[1] - point2[1], 2)
				+ Math.pow(point1[2] - point2[2], 2));
	}

	/**
	 * Scale the keypoint coordinates by a reference length (e.g., torso length).
	 *
	 * @param keypoint Coordinates (x, y, z)
	 * @param scale    Scaling factor (reference length)
	 * @return Scaled keypoint coordinates
	 */
	public float[] scaleKeypoint(float[] keypoint, float scale) {
		if (scale == 0) {
			return keypoint; // Avoid division by zero
		}
		return new float[] { keypoint[0] / scale, keypoint[1] / scale, keypoint[2] / scale };
	}

	/**
	 * Calculate the angle between three keypoints (joint angles).
	 *
	 * @param p1 First keypoint
	 * @param p2 Second keypoint
	 * @param p3 Third keypoint
	 * @return Angle in radians
	 */
	public static double calculateAngle(float[] p1, float[] p2, float[] p3) {
		float a = calculateDistance(p2, p3);
		float b = calculateDistance(p1, p3);
		float c = calculateDistance(p1, p2);

		return Math.acos((b * b + c * c - a * a) / (2 * b * c));
	}

	/**
	 * Calculate torso length between shoulder and hip for a specific frame.
	 *
	 * @param keypoints Map of keypoints across frames
	 * @param frame     Frame number
	 * @return Torso length or 0 if not found
	 */
	private float calculateTorsoLength(Map<String, Map<Integer, float[]>> keypoints, int frame) {
		float[] shoulderLeft = keypoints.getOrDefault("shoulder_left", new HashMap<>()).get(frame);
		float[] hipLeft = keypoints.getOrDefault("hip_left", new HashMap<>()).get(frame);

		if (shoulderLeft != null && hipLeft != null) {
			return calculateDistance(shoulderLeft, hipLeft);
		}
		return 0;
	}

}
