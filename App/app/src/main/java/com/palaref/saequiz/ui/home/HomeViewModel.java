package com.palaref.saequiz.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.palaref.saequiz.model.QuizInfo;
import com.palaref.saequiz.model.User;
import com.palaref.saequiz.utils.SQLiteManager;

import java.sql.Date;
import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<QuizInfo> monthlyQuiz;
    private MutableLiveData<ArrayList<QuizInfo>> quizzes;

    public HomeViewModel() {
        monthlyQuiz = new MutableLiveData<>();
        monthlyQuiz.setValue(new QuizInfo(0, "Monthly Quiz", "Description 0", 0, SQLiteManager.getDateOf(06, 05, 2003)));
        quizzes = new MutableLiveData<>();
        quizzes.setValue(new ArrayList<>());
        //addBunchOfQuizzes();
    }

    public MutableLiveData<ArrayList<QuizInfo>> getQuizzes() {
        return quizzes;
    }

    public MutableLiveData<QuizInfo> getMonthlyQuiz() {
        return monthlyQuiz;
    }

    public void addBunchOfQuizzes(){
        quizzes.getValue().add(new QuizInfo(1, "Quiz 1", "Description 1", 1, SQLiteManager.getNowDate()));
        quizzes.getValue().add(new QuizInfo(2, "Quiz 2", "Description 2", 2, SQLiteManager.getNowDate()));
        quizzes.getValue().add(new QuizInfo(3, "Quiz 3", "Description 3", 3, SQLiteManager.getNowDate()));
        quizzes.getValue().add(new QuizInfo(4, "Quiz 4", "Description 4", 4, SQLiteManager.getNowDate()));
        quizzes.getValue().add(new QuizInfo(5, "Quiz 5", "Description 5", 5, SQLiteManager.getNowDate()));
        quizzes.getValue().add(new QuizInfo(6, "Quiz 6", "Description 6", 6, SQLiteManager.getNowDate()));
        quizzes.getValue().add(new QuizInfo(7, "Quiz 7", "Description 7", 7, SQLiteManager.getNowDate()));
    }
}