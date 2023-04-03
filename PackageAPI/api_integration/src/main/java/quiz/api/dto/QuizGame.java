package quiz.api.dto;

import java.util.ArrayList;

public class QuizGame {
    private ArrayList<QuizQuestion> questions;

    public QuizGame(ArrayList<QuizQuestion> questions) {
        this.questions = questions;
    }

    public ArrayList<QuizQuestion> getQuestions() {
        return questions;
    }
}
