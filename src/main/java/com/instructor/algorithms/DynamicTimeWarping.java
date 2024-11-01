package com.instructor.algorithms;

import java.util.Map;
import java.util.HashMap;

import com.instructor.data.PoseDataProcessing;

// TODO: COMPLETE TEST CODE FOR DYNAMIC WARPING WHICH ACCOUNTS FOR FRAME DIFFERENCES
public class DynamicTimeWarping {

    /**
     * Calculate the DTW distance between 2 sequences of keypoints
     * 
     * @param userKeypoints Map of user keypoints across frames
     * @param proKeypoints  Map of pro keypoints across frames
     * @return DTW distance (lower is better)
     */
    public static float dtw(Map<String, Map<Integer, float[]>> userKeypoints,
            Map<String, Map<Integer, float[]>> proKeypoints) {
        PoseDataProcessing poseDataProcessing = new PoseDataProcessing();

        int n = userKeypoints.get("shoulder_left").size(); // Number of frames in user pose sequence
        int m = proKeypoints.get("shoulder_left").size(); // Number of frames in pro pose sequence

        // Initialize the DTW matrix
        float[][] dtwMatrix = new float[n + 1][m + 1];

        // Fill the matrix with high values (infinity) except starting point
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                dtwMatrix[i][j] = Float.MAX_VALUE;
            }
        }
        dtwMatrix[0][0] = 0;

        // Compute the DTW cost matrix
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                float cost = 0;

                // Calculate the cost between frames i (user) and j (pro) for each keypoint
                for (String keypoint : userKeypoints.keySet()) {
                    float[] userCoords = userKeypoints.getOrDefault(keypoint, new HashMap<>()).get(i - 1);
                    float[] proCoords = proKeypoints.getOrDefault(proKeypoints, new HashMap<>()).get(j - 1);

                    if (userCoords != null & proCoords != null) {
                        // Calculate distance between keypoints
                        cost += poseDataProcessing.calculateDistance(userCoords, proCoords);
                    }
                }

                // Add the cost to optimal path cost to this cell
                dtwMatrix[i][j] = cost + Math.min(Math.min(dtwMatrix[i - 1][j], dtwMatrix[i][j - 1]), // Vertical or
                                                                                                      // Horizontal
                        dtwMatrix[i - 1][j - 1] // Diagonal
                );
            }
        }
        // Return the final DTW distance (lower is better)
        return dtwMatrix[n][m];
    }
}
