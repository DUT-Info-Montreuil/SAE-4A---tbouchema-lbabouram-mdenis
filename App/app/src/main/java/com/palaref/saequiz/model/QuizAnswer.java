package com.palaref.saequiz.model;

public class QuizAnswer {
    int quizAnswerId;
    int quizQuestionId;
    private final String answer;
    private final boolean isCorrect;
    int answerNumber;

    public QuizAnswer(String answer, boolean isCorrect) {
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    public QuizAnswer(int quizAnswerId, int quizQuestionId, String answer, boolean isCorrect, int answerNumber) {
        this.quizAnswerId = quizAnswerId;
        this.quizQuestionId = quizQuestionId;
        this.answer = answer;
        this.isCorrect = isCorrect;
        this.answerNumber = answerNumber;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public int getQuizAnswerId() {
        return quizAnswerId;
    }

    public int getQuizQuestionId() {
        return quizQuestionId;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }
}
