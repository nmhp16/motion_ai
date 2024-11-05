package com.instructor.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

		// Return similarity between user and pro
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
	public float calculateDistance(float[] point1, float[] point2) {
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
	public double calculateAngle(float[] p1, float[] p2, float[] p3) {
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

	/**
	 * Clean missing keypoints by filling them with interpolated values from
	 * neighboring frames
	 * 
	 * @param keypoints Map of all keypoints (body parts), frame, coordinates (x,
	 *                  y, z)
	 * @return Map of cleaned keypoints with interpolated missing values for each
	 *         frame
	 */
	public Map<String, Map<Integer, float[]>> cleanMissingKeypoints(Map<String, Map<Integer, float[]>> keypoints) {
		// Initialize map to store cleaned keypoints with interpolated data
		Map<String, Map<Integer, float[]>> cleanedKeypoints = new HashMap<>();

		// Iterate through each keypoint type in input data
		for (String keypoint : keypoints.keySet()) {
			Map<Integer, float[]> keypointFrames = keypoints.getOrDefault(keypoint, new HashMap<>());
			List<Integer> frames = new ArrayList<>(keypointFrames.keySet());
			Collections.sort(frames); // TODO: Change with merge sort later

			Map<Integer, float[]> interpolatedFrames = new HashMap<>(keypointFrames); // Start with available data

			// Interpolate values for missing frames by filling gap between known frames
			for (int i = 0; i < frames.size() - 1; i++) {
				int startFrame = frames.get(i); // Frame with known coordinates at the start of a gap
				int endFrame = frames.get(i + 1); // Frame with known coordinates at the end of a gap
				float[] startCoords = keypointFrames.get(startFrame); // Coordinates for startFrame
				float[] endCoords = keypointFrames.get(endFrame); // Coordinates for endFrame

				// Validate coordinates before proceeding
				if (areValidCoordinates(startCoords, endCoords)) {
					interpolateMissingFrames(interpolatedFrames, startFrame, endFrame, startCoords, endCoords);
				}
			}
			// Add completed map with interpolated values
			cleanedKeypoints.put(keypoint, interpolatedFrames);
		}
		return cleanedKeypoints; // Return map of keypoints with interpolated data for missing frames
	}

	/**
	 * Helper method to verify if coordinates are valid
	 * 
	 * @param startCoords Starting coordinates array
	 * @param endCoords   Ending coordinates array
	 * @return True if valid, False otherwise
	 */
	private boolean areValidCoordinates(float[] startCoords, float[] endCoords) {
		return startCoords != null && endCoords != null && startCoords.length == 3 && endCoords.length == 3;
	}

	/**
	 * Helper method to linearly interpolate missing frames
	 * 
	 * @param interpolatedFrames Map of frames to be interpolated
	 * @param startFrame         Start frame
	 * @param endFrame           End frame
	 * @param startCoords        Starting coordinates array
	 * @param endCoords          Ending coordinates array
	 */
	private void interpolateMissingFrames(Map<Integer, float[]> interpolatedFrames, int startFrame, int endFrame,
			float[] startCoords, float[] endCoords) {
		// Interpolate for missing frames between start and end frame
		for (int j = startFrame + 1; j < endFrame; j++) {

			// Check if this frame already has coordinates
			if (interpolatedFrames.containsKey(j)) {
				continue; // Skip this frame
			}

			float fraction = (float) (j - startFrame) / (endFrame - startFrame); // Fractional position in the
																					// gap
			float[] interpolatedCoords = new float[3]; // (x, y, z) coordinates

			// Interpolate each coordinate based on fraction
			for (int k = 0; k < 3; k++) {
				interpolatedCoords[k] = startCoords[k] + fraction * (endCoords[k] - startCoords[k]);
			}
			interpolatedFrames.put(j, interpolatedCoords); // Add interpolated coordinates for frame j
		}
	}

	/**
	 * Smooth keypoint data using a simple moving average
	 * 
	 * @param keypoints  Map of keypoints
	 * @param windowSize Number of frames to consider for smoothing
	 * @return Smoothed Keypoints
	 */
	public Map<String, Map<Integer, float[]>> smoothKeypoints(Map<String, Map<Integer, float[]>> keypoints,
			int windowSize) {
		// Initialize map to store smoothed keypoints
		Map<String, Map<Integer, float[]>> smoothedKeypoints = new HashMap<>();

		// Iterate over each keypoint in provided data
		for (String keypoint : keypoints.keySet()) {
			smoothedKeypoints.put(keypoint, new HashMap<>());

			// Get sorted frame keys to ensure the window applies across available frame.
			List<Integer> frames = new ArrayList<>(keypoints.get(keypoint).keySet());
			Collections.sort(frames); // TODO: Can change with merge sort later

			// Apply smoothing for each frame
			for (int i = 0; i < frames.size(); i++) {
				int currentFrame = frames.get(i);
				// Calculate the smoothed coordinates for current frame
				float[] smoothedCoords = movingAverage(keypoints, keypoint, frames, i, windowSize);
				// Store smoothed coordinates
				smoothedKeypoints.get(keypoint).put(currentFrame, smoothedCoords);
			}
		}
		return smoothedKeypoints;
	}

	/**
	 * Helper method to calculate the moving average for a given frame
	 * 
	 * @param keypoints    Map containing all keypoint data
	 * @param keypoint     Specific keypoint being smoothed
	 * @param frames       Sorted lis of frames for this keypoint
	 * @param currentIndex Current index in frame list
	 * @param windowSize   Number of frames in smoothing window
	 * @return The smoothed 3D coordinates for current frame
	 */
	private float[] movingAverage(Map<String, Map<Integer, float[]>> keypoints, String keypoint, List<Integer> frames,
			int currentIndex, int windowSize) {
		int halfWindow = windowSize / 2; // Determine range on either side of current frame
		float[] sum = new float[3]; // Array to hold cumulative sum of coordinates
		int count = 0; // Count of valid frames in window

		// Calculate the starting index of smoothing window, at least > 0
		int start = Math.max(0, currentIndex - halfWindow);

		// Calculate the ending index of smoothing window, at most < last frame index
		int end = Math.min(currentIndex + halfWindow, frames.size() - 1);

		// Calculate the window range around the current frame index.
		for (int i = start; i <= end; i++) {
			int frame = frames.get(i);
			float[] coords = keypoints.get(keypoint).get(frame);

			// Add coordinates to sum if they exist
			if (coords != null) {
				sum[0] += coords[0];
				sum[1] += coords[1];
				sum[2] += coords[2];
				count++;
			}
		}

		// Average the sum to get the smoothed values.
		if (count > 0) {
			sum[0] /= count;
			sum[1] /= count;
			sum[2] /= count;
		}
		return sum; // Return smoothed 3D coordinates
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
		processedMap = cleanMissingKeypoints(keypointsMap);

		// Smooth the cleaned keypoints with defined window size
		int windowSize = 5;

		processedMap = smoothKeypoints(processedMap, windowSize);

		// Normalize the cleaned keypoints for both user and pro
		processedMap = normalizeKeypoints(processedMap);

		// TODO: Calculate DTW Score

		return processedMap;
	}
}
