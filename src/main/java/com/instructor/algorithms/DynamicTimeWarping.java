package com.instructor.algorithms;

import java.util.Map;
import java.util.HashMap;

import com.instructor.data.PoseDataProcessing;

public class DynamicTimeWarping {
    /**
     * Calculate the DTW distance between 2 sequences of keypoints
     * 
     * @param userKeypoints Map of user keypoints across frames
     * @param proKeypoints  Map of pro keypoints across frames
     * @return DTW distance (lower is better)
     */
    public static float totalDtw(Map<String, Map<Integer, float[]>> userKeypoints,
            Map<String, Map<Integer, float[]>> proKeypoints) {
        float totalDtwDistance = 0;
        int partCount = 0;

        // Loop through each body part
        for (String keypoint : userKeypoints.keySet()) {
            Map<Integer, float[]> userPartData = userKeypoints.getOrDefault(keypoint, new HashMap<>());
            Map<Integer, float[]> proPartData = proKeypoints.getOrDefault(keypoint, new HashMap<>());

            if (userPartData != null && proPartData != null) {
                float dtwDistance = dtw(userPartData, proPartData); // Call the individual body part dtw method
                totalDtwDistance += dtwDistance;
                partCount++;
            }
        }

        // Return DTW similarity by averaging the dtwDistance (lower is better)
        if (partCount > 0) {
            return totalDtwDistance / partCount; // Lower similarity mean higher score
        } else {
            return Float.MAX_VALUE; // No matching frames, return high "distance"
        }
    }

    /**
     * Calculate the DTW distance between 2 sequences of keypoints for specific body
     * part.
     * 
     * @param userPartData Map of user keypoints for a specific body part across
     *                     frames
     * @param proPartData  Map of professional keypoints for a specific body part
     *                     across frames
     * @return DTW distance (lower is better)
     */
    public static float dtw(Map<Integer, float[]> userPartData, Map<Integer, float[]> proPartData) {
        PoseDataProcessing poseDataProcessing = new PoseDataProcessing();

        int n = userPartData.size(); // Number of frames in user pose sequence
        int m = proPartData.size(); // Number of frames in pro pose sequence

        // Initialize the DTW matrix
        float[][] dtwMatrix = new float[n + 1][m + 1];

        // Fill the matrix with high values (infinity) except starting point
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dtwMatrix[i][j] = Float.MAX_VALUE;
            }
        }
        dtwMatrix[0][0] = 0;

        // Calculate DTW cost
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                float[] userCoords = userPartData.get(i - 1);
                float[] proCoords = proPartData.get(j - 1);

                float cost = 0;
                if (userCoords != null && proCoords != null) {
                    cost = poseDataProcessing.calculateDistance(userCoords, proCoords);
                }

                // Add the cost to the optimal path cost to this cell
                dtwMatrix[i][j] = cost + Math.min(
                        Math.min(dtwMatrix[i - 1][j], dtwMatrix[i][j - 1]), // Vertical or Horizontal
                        dtwMatrix[i - 1][j - 1]); // Diagonal
            }
        }
        // Return DTW distance normalized by the length of the path
        return dtwMatrix[n][m] / Math.max(n, m);
    }
}
