package com.palaref.saequiz.model;

import java.util.ArrayList;

public class QuizGame {
    int quizId;
    int quizInfoId;
    private ArrayList<QuizQuestion> questions;
    private int score = 0; // this is only local, not saved in the database
    private int multiplier = 1;

    public QuizGame(ArrayList<QuizQuestion> questions, int quizInfoId) {
        this.questions = questions;
        this.quizInfoId = quizInfoId;
    }

    public QuizGame(int quizId, int quizInfoId, ArrayList<QuizQuestion> questions) {
        this.quizId = quizId;
        this.questions = questions;
        this.quizInfoId = quizInfoId;
    }

    public ArrayList<QuizQuestion> getQuestions() {
        return questions;
    }

    private int currentQuestionIndex = 0;

    public QuizQuestion nextQuestion() { // if null is returned, there are no more questions
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex++);
        }
        return null;
    }

    public boolean checkAnswer(int answerNumber) {
        if (questions.get(currentQuestionIndex).getAnswers().get(answerNumber).isCorrect()) {
            addScore(10 * multiplier++);
            return true;
        }else {
            multiplier = 1;
            return false;
        }
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getQuizId() {
        return quizId;
    }

    public int getQuizInfoId() {
        return quizInfoId;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}
