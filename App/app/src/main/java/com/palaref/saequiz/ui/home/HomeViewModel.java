package com.palaref.saequiz.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.palaref.saequiz.model.QuizInfo;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<QuizInfo> monthlyQuiz;
    private MutableLiveData<ArrayList<QuizInfo>> quizzes;

    public HomeViewModel() {
        monthlyQuiz = new MutableLiveData<>();
        monthlyQuiz.setValue(new QuizInfo(0, "Monthly Quiz", "Description 0", 0, "2021-01-01", "2021-01-01", 0, new String[]{"tag1", "tag2"}));

        quizzes = new MutableLiveData<>();
        quizzes.setValue(new ArrayList<>());
        addBunchOfQuizzes();
    }

    public MutableLiveData<ArrayList<QuizInfo>> getQuizzes() {
        return quizzes;
    }

    public MutableLiveData<QuizInfo> getMonthlyQuiz() {
        return monthlyQuiz;
    }

    public void addBunchOfQuizzes(){
        quizzes.getValue().add(new QuizInfo(1, "Quiz 1", "Description 1", 1, "2021-01-01", "2021-01-01", 1, new String[]{"tag1", "tag2"}));
        quizzes.getValue().add(new QuizInfo(2, "Quiz 2", "Description 2", 2, "2021-01-01", "2021-01-01", 2, new String[]{"tag1", "tag2"}));
        quizzes.getValue().add(new QuizInfo(3, "Quiz 3", "Description 3", 3, "2021-01-01", "2021-01-01", 3, new String[]{"tag1", "tag2"}));
        quizzes.getValue().add(new QuizInfo(4, "Quiz 4", "Description 4", 4, "2021-01-01", "2021-01-01", 4, new String[]{"tag1", "tag2"}));
        quizzes.getValue().add(new QuizInfo(5, "Quiz 5", "Description 5", 5, "2021-01-01", "2021-01-01", 5, new String[]{"tag1", "tag2"}));
        quizzes.getValue().add(new QuizInfo(6, "Quiz 6", "Description 6", 6, "2021-01-01", "2021-01-01", 6, new String[]{"tag1", "tag2"}));
        quizzes.getValue().add(new QuizInfo(7, "Quiz 7", "Description 7", 7, "2021-01-01", "2021-01-01", 7, new String[]{"tag1", "tag2"}));
    }
}