package com.palaref.saequiz.model;

public class QuizAnswer {
    private final String answer;
    private final boolean isCorrect;

    public QuizAnswer(String answer, boolean isCorrect) {
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
