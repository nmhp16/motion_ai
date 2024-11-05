package com.instructor.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.instructor.algorithms.DynamicTimeWarping;

// TODO: COMPLETE TEST CODE
public class PoseScoring {
	private PoseFeedback feedback = new PoseFeedback();
	private List<Integer> scores = new ArrayList<>();
	private int score = 0;
	private int overallScore = 0;

	// Threshold for considering a pose as "wrong"
	private static final int THRESHOLD_SCORE = 90;

	/**
	 * Give score based on the similarity between User and Pro
	 * 
	 * @param similarityScore
	 * @param maxSimilarity
	 * @return
	 */
	public int calculateScore(float similarityScore, float maxSimilarity) {
		// Cap the similarity score to avoid exceeding maxSimilarity
		similarityScore = Math.min(similarityScore, maxSimilarity);

		// Normalize the similarity score on a scale of 0-1
		float normalizedSimilarity = similarityScore / maxSimilarity;

		// Calculate final score (out of 100) - lower normalized similarity gives higher
		// score
		return (int) Math.max(0, (1 - normalizedSimilarity) * 100);
	}

	/**
	 * Score user pose against professional pose and provide feedback
	 * 
	 * @param userKeypoints Map containing user keypoints
	 * @param proKeypoints  Map containing professional keypoints
	 */
	public void calculatePoseScore(Map<String, Map<Integer, float[]>> userKeypoints,
			Map<String, Map<Integer, float[]>> proKeypoints) {
		// Track frames where user performed poorly
		Map<String, List<Integer>> incorrectFrames = new HashMap<>();
		Map<String, List<Integer>> lowScoreFrames = new HashMap<>();

		for (String bodyPart : userKeypoints.keySet()) {
			Map<Integer, float[]> userPartData = userKeypoints.get(bodyPart);
			Map<Integer, float[]> proPartData = proKeypoints.get(bodyPart);
			scores = new ArrayList<>();

			// Check if there's user data and professional data for the body part
			if (userPartData == null || proPartData == null) {
				continue;
			}

			// Calculate DTW distance between user and professional keypoints for current
			// body part
			float totalDtwDistance = DynamicTimeWarping.dtw(userPartData, proPartData);
			float maxDistance = 2.8f; // Replace actual value later

			// Normalize score based on DTW distance
			overallScore = calculateScore(totalDtwDistance, maxDistance); // Calculate score

			// Evaluate frame by frame using DTW
			for (Integer frame : userPartData.keySet()) {
				Map<Integer, float[]> userSingleFrameData = new HashMap<>();
				Map<Integer, float[]> proSingleFrameData = new HashMap<>();

				// Add current frame data to single frame maps
				userSingleFrameData.put(0, userPartData.get(frame));
				proSingleFrameData.put(0, proPartData.get(frame));

				// Calculate DTW distance for the current frame vs corresponding professional
				// frame
				float frameDtwDistance = DynamicTimeWarping.dtw(userSingleFrameData, proSingleFrameData);
				score = calculateScore(frameDtwDistance, maxDistance);
				scores.add(score);

				// Check if the frame score is below the threshold
				if (score < THRESHOLD_SCORE) {
					// Store the frame as incorrect
					// TODO: we need to add a clause if incorrect, then check if x y z are off and if x y z greater than or less than 
					// if it is x : left or right 
					// if it is y : up or down 
					// if it is Z : front and back 
					// when given an incorrect, then 
					if (!incorrectFrames.containsKey(bodyPart)) {
						incorrectFrames.put(bodyPart, new ArrayList<>());
					}
					incorrectFrames.get(bodyPart).add(frame); // Add the frame number

					// Store the low score for this frame
					if (!lowScoreFrames.containsKey(bodyPart)) {
						lowScoreFrames.put(bodyPart, new ArrayList<>());
					}
					lowScoreFrames.get(bodyPart).add(score); // Add the low score
				}
			}

			// Calculate overall score

			if (isPartNeeded(bodyPart)) {
				System.out.println("=================================================");
				System.out.println("Body Part: " + bodyPart);
				System.out.println("Overall Score: " + overallScore);

				// Provide feedback for the current frame
				System.out.println(feedback.provideSpecificFeedback(overallScore, bodyPart));
				System.out.println();

				// Check if there is bad score which < 90
				if (lowScoreFrames.get(bodyPart) == null || lowScoreFrames.get(bodyPart).isEmpty()) {
					System.out.println("Bad Scores: none");
					System.out.println();
				} else {
					System.out.println("Bad Scores: " + lowScoreFrames.get(bodyPart));
					System.out.println();
				}

				// Check for incorrect frames connect with bad score
				if (incorrectFrames.get(bodyPart) == null) {
					System.out.println("Incorrect frames: none.");
				} else {
					System.out.println("Incorrect frames: " + incorrectFrames.get(bodyPart));
				}

				System.out.println();
			}
		}
	}

	/**
	 * Helper method to check for only body parts needed
	 * 
	 * @param bodyPart Current body part
	 * @return True if it is needed, False otherwise
	 */
	private boolean isPartNeeded(String bodyPart) {
		return bodyPart.equalsIgnoreCase("shoulder_left") || bodyPart.equalsIgnoreCase("shoulder_right")
				|| bodyPart.equalsIgnoreCase("elbow_left") || bodyPart.equalsIgnoreCase("elbow_right")
				|| bodyPart.equalsIgnoreCase("wrist_left") || bodyPart.equalsIgnoreCase("wrist_right")
				|| bodyPart.equalsIgnoreCase("hip_left") || bodyPart.equalsIgnoreCase("hip_right")
				|| bodyPart.equalsIgnoreCase("knee_left") || bodyPart.equalsIgnoreCase("knee_right")
				|| bodyPart.equalsIgnoreCase("ankle_left") || bodyPart.equalsIgnoreCase("ankle_right")
				|| bodyPart.equalsIgnoreCase("heel_left") || bodyPart.equalsIgnoreCase("heel_right")
				|| bodyPart.equalsIgnoreCase("foot_index_left") || bodyPart.equalsIgnoreCase("foot_index_right")
				|| bodyPart.equalsIgnoreCase("nose");
	}
}
