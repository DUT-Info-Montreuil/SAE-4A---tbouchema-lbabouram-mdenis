package com.palaref.saequiz.model;

import java.util.ArrayList;

public class QuizGame {
    private ArrayList<QuizQuestion> questions;

    public QuizGame(ArrayList<QuizQuestion> questions) {
        this.questions = questions;
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
}
