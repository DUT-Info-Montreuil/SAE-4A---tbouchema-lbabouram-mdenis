package quiz.api.dto;

import java.util.ArrayList;

public class QuizInfoAPI {
    private String _id;
    private String _quizName;
    private String _quizDescription;
    private String _quizCreator;
    private String _quizCreationDate;
    private ArrayList<String> _quizTags;
    private QuizGame _quizGame;

    public QuizInfoAPI(String id, String quizName, String quizDescription, String quizCreator, ArrayList<String> quizTags, QuizGame quizGame, String quizCreationDate) {
        _id = id;
        _quizName = quizName;
        _quizDescription = quizDescription;
        _quizCreator = quizCreator;
        _quizTags = quizTags;
        _quizGame = quizGame;
        _quizCreationDate = quizCreationDate;
    }

    public String getId() {
        return _id;
    }

    public String getName() {
        return _quizName;
    }

    public String getDescription() {
        return _quizDescription;
    }

    public String getCreator() {
        return _quizCreator;
    }

    public String getCreationDate() {
        return _quizCreationDate;
    }

    public ArrayList<String> getTags() {
        return _quizTags;
    }

    public QuizGame getGame() {
        return _quizGame;
    }
}
