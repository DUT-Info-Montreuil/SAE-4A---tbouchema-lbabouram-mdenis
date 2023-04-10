package com.palaref.saequiz.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.palaref.saequiz.databinding.FragmentFavoritesBinding;

import quiz.api.integration.QuizRequests;
import quiz.api.integration.UserRequests;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private QuizRequests quizRequests;
    private UserRequests userRequests;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FavoritesViewModel dashboardViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        quizRequests = getArguments().getString("quizRequests") != null ? (QuizRequests) getArguments().getSerializable("quizRequests") : null;
        userRequests = getArguments().getString("userRequests") != null ? (UserRequests) getArguments().getSerializable("userRequests") : null;

        final TextView textView = binding.textFavorites;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}