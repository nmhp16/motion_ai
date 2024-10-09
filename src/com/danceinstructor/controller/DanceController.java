package com.danceinstructor.controller;

import com.danceinstructor.model.User;
import com.danceinstructor.model.Feedback;

public class DanceController {
    public Feedback analyzeDanceMove(User user, String move) {
        String feedbackMessage = "Great job, " + user.getName() + "! Keep practicing.";
        return new Feedback(feedbackMessage);
    }
}
