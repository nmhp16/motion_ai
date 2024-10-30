package com.instructor.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.instructor.data.PoseDataProcessing;

// TODO: COMPLETE TEST CODE
public class PoseScoring {
	private PoseFeedback feedback = new PoseFeedback();
	private PoseDataProcessing poseDataProcessing = new PoseDataProcessing();
	private List<Integer> scores = new ArrayList<>();
	private int score;
	private int averageScore;

	// Threshold for considering a pose as "wrong"
	private static final int THRESHOLD_SCORE = 60;

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
			averageScore = 0;

			for (Integer frame : userPartData.keySet()) {
				float[] userData = userPartData.get(frame);
				float[] proData = proPartData.get(frame);

				if (proData != null) {
					float distance = poseDataProcessing.calculateDistance(userData, proData);
					float maxDistance = 4.0f; // TODO: Replace with actual distance later
					score = calculateScore(distance, maxDistance); // Calculate score
					scores.add(score); // Collect scores for output

					// Calculate average score sum first
					averageScore += score;

					// Check if the score is below the threshold and add to incorrect frames
					if (score < THRESHOLD_SCORE) {
						// Store list of frame for incorrect pose
						if (!incorrectFrames.containsKey(bodyPart)) {
							incorrectFrames.put(bodyPart, new ArrayList<>());
						}
						incorrectFrames.get(bodyPart).add(frame);

						// Store list of score for incorrect pose
						if (!lowScoreFrames.containsKey(bodyPart)) {
							lowScoreFrames.put(bodyPart, new ArrayList<>());
						}
						lowScoreFrames.get(bodyPart).add(score);
					}
				}
			}
			// Calculate average score
			averageScore = averageScore / scores.size();

			if (isPartNeeded(bodyPart)) {
				System.out.println("=================================================");
				System.out.println("Body Part: " + bodyPart);
				System.out.println("Average Score: " + averageScore);

				// Provide feedback for the current frame
				feedback.getScoreFeedback(averageScore, bodyPart);
				System.out.println();

				if (lowScoreFrames.get(bodyPart) == null) {
					System.out.println("Bad Scores: none");
					System.out.println();
				} else {
					System.out.println("Bad Scores: " + lowScoreFrames.get(bodyPart));
					System.out.println();
				}

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
