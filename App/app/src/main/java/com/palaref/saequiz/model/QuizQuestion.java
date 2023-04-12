package com.palaref.saequiz.model;

import java.util.ArrayList;

public class QuizQuestion {
    int quizQuestionId;
    int quizGameId;
    private final String question;
    private final ArrayList<QuizAnswer> answers;

    private int questionNumber;

    public QuizQuestion(String question, ArrayList<QuizAnswer> answers, int questionNumber) {
        this.question = question;
        this.answers = answers;
    }

    public QuizQuestion(int quizQuestionId, int quizGameId, String question, ArrayList<QuizAnswer> answers, int questionNumber) {
        this.quizQuestionId = quizQuestionId;
        this.question = question;
        this.answers = answers;
        this.quizGameId = quizGameId;
        this.questionNumber = questionNumber;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<QuizAnswer> getAnswers() {
        return answers;
    }

    public int getQuizQuestionId() {
        return quizQuestionId;
    }

    public int getQuizGameId() {
        return quizGameId;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuizQuestionId(int quizQuestionId) {
        this.quizQuestionId = quizQuestionId;
    }

    public void setQuizGameId(int quizGameId) {
        this.quizGameId = quizGameId;
    }
}
