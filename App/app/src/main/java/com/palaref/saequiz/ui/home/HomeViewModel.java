package com.palaref.saequiz.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> Text;

    public HomeViewModel() {
        Text = new MutableLiveData<>();
        Text.setValue("This is the Home fragment");
    }

    public LiveData<String> getText() {
        return Text;
    }
}