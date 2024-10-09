package com.instructor.evaluation;

public class PoseScoring {

	public int calculateScore(float similarityScore, float maxSimilarity) {
		// Normalize the similarity score to be out of 100
		// Low similarity score means the user's pose is very similar to professional dancer's pose
		return (int) Math.max(0,  100 - (similarityScore / maxSimilarity) * 100);
	}
}
