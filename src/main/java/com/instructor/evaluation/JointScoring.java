package com.instructor.evaluation;

import java.util.Map;

import com.instructor.algorithms.DynamicTimeWarping;

public class JointScoring {
    	//TODO: these are values that we need to test for so that we can implement it accurately
    private static final float THRESHOLD = 0.01f;
    private static final float DTW_THRESHOLD = 0.05f;

    // Function to get feedback for joint movement
    public static String getJointFeedback(String jointName, float[] userFrameData, float[] proFrameData) {
        StringBuilder feedback = new StringBuilder();

        feedback.append("Frame feedback for ").append(jointName).append(": ");

        // Check X, Y, and Z directions and provide feedback
        if (userFrameData[0] > proFrameData[0] + THRESHOLD) {
            feedback.append("Move ").append(jointName).append(" to the left. ");
        } else if (userFrameData[0] < proFrameData[0] - THRESHOLD) {
            feedback.append("Move ").append(jointName).append(" to the right. ");
        }

        if (userFrameData[1] > proFrameData[1] + THRESHOLD) {
            feedback.append("Move ").append(jointName).append(" down. ");
        } else if (userFrameData[1] < proFrameData[1] - THRESHOLD) {
            feedback.append("Move ").append(jointName).append(" up. ");
        }

        if (userFrameData[2] > proFrameData[2] + THRESHOLD) {
            feedback.append("Move ").append(jointName).append(" back. ");
        } else if (userFrameData[2] < proFrameData[2] - THRESHOLD) {
            feedback.append("Move ").append(jointName).append(" forward. ");
        }

        feedback.append("\n");
        return feedback.toString();
    }

    // Function to compare user and pro frames, calculate DTW, and provide feedback
    public static String compareFramesAndProvideFeedback(String jointName, float[] userFrameData, float[] proFrameData) {
        // Calculate DTW distance (simulated for this example)
        float dtwDistance = DynamicTimeWarping.dtw(Map.of(0, userFrameData), Map.of(0, proFrameData));

        // If DTW distance is within the threshold, provide feedback
        if (dtwDistance <= DTW_THRESHOLD) {
            return getJointFeedback(jointName, userFrameData, proFrameData);
        } else {
            return "Pose is sufficiently aligned for " + jointName + " in this frame.\n";
        }
    }
}
