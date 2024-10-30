package com.instructor.evaluation;

// TODO: COMPLETE FEEDBACK 
public class PoseFeedback {

    /**
     * Method to print feedback
     * 
     * @param score User score
     */
    public void getScoreFeedback(int score) {
        // Provide feedback based on score range
        // Case 1: Score >= 90
        if (score >= 90) {
            System.out.println("Great Job! Keep it up!");
        }
        // Case 2: 80 <= Score < 90
        else if (score >= 80) {
            System.out.println("Good effort! Few improvements needed.");
        }
        // Case 3: 70 <= Score < 80
        else if (score >= 70) {
            System.out.println("Not bad, try practicing more.");
        }
        // Case 4: Score < 70
        else {
            System.out.println("Keep practicing!");
        }
    }

    /**
     * Method to get feedback for body part
     * 
     * @param score    User score
     * @param bodyPart Body part working on
     */
    public void getScoreFeedback(int score, String bodyPart) {
        // Provide feedback based on score and body part
        // Case 1: Score >= 90
        if (score >= 90) {
            System.out.println("Great job matching your " + bodyPart + "!");
        }
        // Case 2: 80 <= Score < 90
        else if (score >= 80) {
            System.out.println("Good effort matching your " + bodyPart + ". Few improvements needed.");
        }
        // Case 3: 70 <= Score < 80
        else if (score >= 70) {
            System.out.println("Not bad, try matching your " + bodyPart + " more.");
        }
        // Case 4: Score < 70
        else {
            System.out.println("Keep matching your " + bodyPart + "!");
        }
    }
}
