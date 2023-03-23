package com.palaref.saequiz.ui.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.palaref.saequiz.databinding.FragmentHomeBinding;
import com.palaref.saequiz.model.QuizInfo;
import com.palaref.saequiz.model.User;
import com.palaref.saequiz.utils.RecycleItemSpacingDecorator;
import com.palaref.saequiz.utils.SQLiteManager;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupAdapter();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        // this isn't really clean code but it works
        homeViewModel.getQuizzes().getValue().clear();
        homeViewModel.getQuizzes().getValue().addAll(SQLiteManager.getInstance(this.getContext()).selectAllQuizInfos());
    }

    private void debugList() {
        for(QuizInfo quiz : SQLiteManager.getInstance(this.getContext()).selectAllQuizInfos()) {
            Log.d("Quiz", quiz.getName());
            for(QuizInfo quiz2 : Objects.requireNonNull(homeViewModel.getQuizzes().getValue())) {
                Log.d("Quiz2", quiz2.getName());
            }
        }
    }


    private void setupAdapter() {
        RecyclerView recyclerView = binding.recyclerView;
        QuizAdapter adapter = new QuizAdapter(getContext(), homeViewModel.getMonthlyQuiz().getValue(), homeViewModel.getQuizzes().getValue());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}