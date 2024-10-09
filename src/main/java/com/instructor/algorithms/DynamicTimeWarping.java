package com.instructor.algorithms;

//import com.instructor.evaluation.PoseProcessing;

public class DynamicTimeWarping {

	/* Test code for Dynamic Warping which can account for frame difference
	 
	 
	// Calculate the DTW distance between 2 sequences of keypoints
	public static float dtw(float[][] userPose, float[][] proPose) {
		int n = userPose.length; // Number of frames in user pose sequence
		int m = proPose.length; // Number of frames in pro pose sequence
		
		// Initialize the DTW matrix
		float[][] dtwMatrix = new float[n + 1][m + 1];
		
		// Fill the matrix with high values
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				dtwMatrix[i][j] = Float.MAX_VALUE;
			}
		}
		dtwMatrix[0][0] = 0;
		
		// Compute the DTW cost matrix
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				// Use the similarity between frame
				float cost = PoseProcessing.calculateSimilarity(userPose[i - 1], proPose[j - 1]);
				dtwMatrix[i][j] = cost + Math.min(Math.min(dtwMatrix[i - 1][j], dtwMatrix[i][j - 1]), dtwMatrix[i - 1][j - 1]);	
			}
		}
		// Return the final DTW distance (lower is better)
		return dtwMatrix[n][m];
	}
	
	*/
	
}
