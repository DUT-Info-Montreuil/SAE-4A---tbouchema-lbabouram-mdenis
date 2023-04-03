package quiz.api.dto;

import java.io.File;
import java.util.ArrayList;

public class UserProfile {
    private String _pseudo;
    private String _email;
    private File _ProfilePicture;
    private int _score;
    private ArrayList<String> _FavQuizzes;
    private ArrayList<String> _CreatedQuizzes;
    private ArrayList<String> _QuizzesPlayed;

    public UserProfile(String pseudo, String email, File profilePicture, int score, ArrayList<String> favQuizzes, ArrayList<String> createdQuizzes, ArrayList<String> quizzesPlayed) {
        _pseudo = pseudo;
        _email = email;
        _ProfilePicture = profilePicture;
        _score = score;
        _FavQuizzes = favQuizzes;
        _CreatedQuizzes = createdQuizzes;
        _QuizzesPlayed = quizzesPlayed;
    }

    public String getPseudo() {
        return _pseudo;
    }

    public String getEmail() {
        return _email;
    }

    public File getProfilePicture() {
        return _ProfilePicture;
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
