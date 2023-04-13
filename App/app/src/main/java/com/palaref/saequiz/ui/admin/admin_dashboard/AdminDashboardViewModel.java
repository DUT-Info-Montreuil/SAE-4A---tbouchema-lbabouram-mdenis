package com.palaref.saequiz.ui.admin.admin_dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AdminDashboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AdminDashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("You are in admin mode.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}