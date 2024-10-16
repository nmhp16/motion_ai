package com.instructor.evaluation;

// TODO: COMPLETE TEST CODE
public class PoseScoring {
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
}
