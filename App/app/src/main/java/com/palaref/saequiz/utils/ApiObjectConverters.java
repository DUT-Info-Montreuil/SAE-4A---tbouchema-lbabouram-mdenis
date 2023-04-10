package com.palaref.saequiz.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.palaref.saequiz.model.QuizInfo;
import com.palaref.saequiz.model.User;

import java.io.File;
import java.util.ArrayList;

import quiz.api.dto.QuizInfoAPI;
import quiz.api.dto.UserAPI;
import quiz.api.integration.UserRequests;

public class ApiObjectConverters {
    public static QuizInfo convertQuizInfoApiToQuizInfo(QuizInfoAPI quizInfoApi) {
        return new QuizInfo(Integer.parseInt(quizInfoApi.getId()), quizInfoApi.getName(), quizInfoApi.getDescription(), Integer.parseInt(quizInfoApi.getCreator()), quizInfoApi.getCreationDate(), quizInfoApi.getGame());
    }

    public static ArrayList<QuizInfo> convertMultiQuizInfoApiToQuizInfo(ArrayList<QuizInfoAPI> quizInfoApi) {
        ArrayList<QuizInfo> quizInfos = new ArrayList<>();
        for (QuizInfoAPI quizInfo : quizInfoApi) {
            quizInfos.add(convertQuizInfoApiToQuizInfo(quizInfo));
        }
        return quizInfos;
    }

    public static User convertUserApiToUser(UserAPI userApi, UserRequests userRequests) {
        return new User(Integer.parseInt(userApi.getId()), userApi.getUsername(), "uwu", filetoBitmap(userRequests.GetUserProfilePicture(userApi.getId()))); // TODO: add description to userAPI or remove it from the constructor
    }

    public static Bitmap filetoBitmap(File file) {
        return  BitmapFactory.decodeFile(file.getAbsolutePath());
    }
}
