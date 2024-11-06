package com.instructor.algorithms;

import java.util.Map;
import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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

    /**
     * Method to check for the aligned frame using Dynamic Time Warping
     * Calculates optimal alignment path between user and pro
     * keypoints for specific body part across frames
     * 
     * @param userPartData Map of user keypoints for a specific body part across
     *                     frames
     * @param proPartData  Map of pro keypoints for a specific body part across
     *                     frames
     * @return List of aligned frames (each frame represented as pair of user and
     *         pro frame)
     */
    public static List<int[]> dtwWithAlignmentPath(Map<Integer, float[]> userPartData,
            Map<Integer, float[]> proPartData) {
        // Create instance of PoseDataProcessing to calculate distances
        PoseDataProcessing poseDataProcessing = new PoseDataProcessing();

        // Get the number of frames for user and pro poses
        int n = userPartData.size(); // Number of frames in user pose sequence
        int m = proPartData.size(); // Number of frames in pro pose sequence

        // Initialize the DTW Matrix
        float[][] dtwMatrix = new float[n + 1][m + 1];
        int[][] pathMatrix = new int[n + 1][m + 1]; // Store the path for backtracking

        // Fill the matrix with high values (infinity) except starting point
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dtwMatrix[i][j] = Float.MAX_VALUE;
            }
        }
        dtwMatrix[0][0] = 0; // Starting point

        // Calculate DTW cost and track the optimal path
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // Get coordinates for user and pro frames
                float[] userCoords = userPartData.get(i - 1);
                float[] proCoords = proPartData.get(j - 1);

                // Calculate distance (cost) between user and pro frame
                float cost = 0;
                if (userCoords != null && proCoords != null) {
                    cost = poseDataProcessing.calculateDistance(userCoords, proCoords);
                }

                // Find the minimum cost for this cell
                float minCost = Math.min(
                        Math.min(dtwMatrix[i - 1][j], dtwMatrix[i][j - 1]), // Vertical or Horizontal
                        dtwMatrix[i - 1][j - 1] // Diagonal
                );

                // Update the dtwMatrix and track path
                dtwMatrix[i][j] = cost + minCost;

                // Track the direction of the move
                if (dtwMatrix[i][j] == (cost + dtwMatrix[i - 1][j])) {
                    pathMatrix[i][j] = 1; // Vertical move
                } else if (dtwMatrix[i][j] == (cost + dtwMatrix[i][j - 1])) {
                    pathMatrix[i][j] = 2; // Horizontal move
                } else {
                    pathMatrix[i][j] = 3; // Diagonal move
                }
            }
        }

        // Backtrack to find the optimal path
        List<int[]> alignmentPath = new ArrayList<>();
        int i = n, j = m;

        while (i > 0 && j > 0) {
            alignmentPath.add(new int[] { i - 1, j - 1 }); // Add aligned indices (user frame, pro frame)

            // Move according to the path direction
            if (pathMatrix[i][j] == 1) {
                i--;
            } else if (pathMatrix[i][j] == 2) {
                j--;
            } else {
                i--;
            }
        }

        // Reverse the path to start from beginning
        Collections.reverse(alignmentPath); // TODO: Replace with Merge Sort later
        return alignmentPath;
    }
}
