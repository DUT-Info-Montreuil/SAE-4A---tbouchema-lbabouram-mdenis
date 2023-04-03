package quiz.api.dto;

import java.text.DateFormat;
import java.util.ArrayList;

public class QuizInfoAPI {
    private String _id;
    private String _quizName;
    private String _quizDescription;
    private String _quizCreator;
    private DateFormat _quizCreationDate;
    private ArrayList<String> _quizTags;
    private QuizGame _quizGame;

    public QuizInfoAPI(String id, String quizName, String quizDescription, String quizCreator, ArrayList<String> quizTags, QuizGame quizGame) {
        _id = id;
        _quizName = quizName;
        _quizDescription = quizDescription;
        _quizCreator = quizCreator;
        _quizTags = quizTags;
        _quizGame = quizGame;
    }

    public String getId() {
        return _id;
    }

    public String getQuizName() {
        return _quizName;
    }

    public String getQuizDescription() {
        return _quizDescription;
    }

    public String getQuizCreator() {
        return _quizCreator;
    }

    public DateFormat getQuizCreationDate() {
        return _quizCreationDate;
    }

    public ArrayList<String> getQuizTags() {
        return _quizTags;
    }

    public QuizGame getQuizQuestionnary() {
        return _quizGame;
    }

    public void setQuizCreationDate(DateFormat quizCreationDate) {
        _quizCreationDate = quizCreationDate;
    }
}
