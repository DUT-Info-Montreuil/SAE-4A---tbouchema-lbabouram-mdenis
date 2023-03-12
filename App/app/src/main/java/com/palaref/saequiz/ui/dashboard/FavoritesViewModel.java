package com.palaref.saequiz.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavoritesViewModel extends ViewModel {

    private final MutableLiveData<String> Text;

    public FavoritesViewModel() {
        Text = new MutableLiveData<>();
        Text.setValue("This is the Favorites fragment");
    }

    public LiveData<String> getText() {
        return Text;
    }
}