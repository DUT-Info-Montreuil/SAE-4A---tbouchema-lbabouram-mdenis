package com.palaref.saequiz.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<String> text;

    public SearchViewModel() {
        text = new MutableLiveData<>();
        text.setValue("This is the Search fragment");
    }

    public LiveData<String> getText() {
        return text;
    }
}