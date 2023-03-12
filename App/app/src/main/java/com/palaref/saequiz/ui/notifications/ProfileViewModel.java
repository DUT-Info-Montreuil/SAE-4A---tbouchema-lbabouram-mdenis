package com.palaref.saequiz.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> Text;

    public ProfileViewModel() {
        Text = new MutableLiveData<>();
        Text.setValue("This is the Profile fragment");
    }

    public LiveData<String> getText() {
        return Text;
    }
}