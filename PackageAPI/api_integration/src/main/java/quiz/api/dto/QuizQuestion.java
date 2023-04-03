package quiz.api.dto;

import java.util.ArrayList;

public class QuizQuestion {
    private String _question;
    private ArrayList<QuizAnswer> answers;

    public QuizQuestion(String question, ArrayList<QuizAnswer> answers) {
        _question = question;
        this.answers = answers;
    }

    public ArrayList<QuizAnswer> getAnswers() {
        return answers;
    }

    public String getQuestion() {
        return _question;
    }
}
