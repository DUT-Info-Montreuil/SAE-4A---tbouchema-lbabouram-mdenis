package com.palaref.saequiz.model;

import java.util.ArrayList;

public class QuizQuestion {
    private final String question;
    private final ArrayList<QuizAnswer> answers;

    public QuizQuestion(String question, ArrayList<QuizAnswer> answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<QuizAnswer> getAnswers() {
        return answers;
    }
}
