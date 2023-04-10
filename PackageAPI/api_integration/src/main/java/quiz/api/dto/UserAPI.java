package quiz.api.dto;

import java.io.File;
import java.util.ArrayList;

public class UserAPI {
    private String _id;
    private String _pseudo;
    private String _email;
    private File _ProfilePicture;
    private int _score;
    private ArrayList<String> _FavQuizzes;
    private ArrayList<String> _CreatedQuizzes;
    private ArrayList<String> _QuizzesPlayed;

    public UserAPI(String id, String pseudo, String email, int score, ArrayList<String> favQuizzes, ArrayList<String> createdQuizzes, ArrayList<String> quizzesPlayed) {
        _id = id;
        _pseudo = pseudo;
        _email = email;
        _score = score;
        _FavQuizzes = favQuizzes;
        _CreatedQuizzes = createdQuizzes;
        _QuizzesPlayed = quizzesPlayed;
    }

    public String getId() {
        return _id;
    }

    public String getUsername() {
        return _pseudo;
    }

    public String getEmail() {
        return _email;
    }

    public File getProfilePicture() {
        return _ProfilePicture;
    }

    public void setProfilePicture(File profilePicture) {
        _ProfilePicture = profilePicture;
    }

    public int getScore() {
        return _score;
    }

    public ArrayList<String> getFavQuizzes() {
        return _FavQuizzes;
    }

    public ArrayList<String> getCreatedQuizzes() {
        return _CreatedQuizzes;
    }

    public ArrayList<String> getQuizzesPlayed() {
        return _QuizzesPlayed;
    }
}
