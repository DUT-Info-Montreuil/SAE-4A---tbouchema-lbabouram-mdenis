package com.palaref.saequiz.ui.favorites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavoritesViewModel extends ViewModel {

    private final MutableLiveData<String> state; // either "fav" or "my"

    public FavoritesViewModel() {
        state = new MutableLiveData<>();
        state.setValue("fav");
    }

    public LiveData<String> getState() {
        return state;
    }

    public void setState(String state) {
        this.state.setValue(state);
    }
}