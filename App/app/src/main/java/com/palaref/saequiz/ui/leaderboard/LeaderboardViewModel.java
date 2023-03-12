package com.palaref.saequiz.ui.leaderboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LeaderboardViewModel extends ViewModel {
    private MutableLiveData<String> text;

    public LeaderboardViewModel() {
        text = new MutableLiveData<>();
        text.setValue("This is the Leaderboard fragment");
    }

    public MutableLiveData<String> getText() {
        return text;
    }
}