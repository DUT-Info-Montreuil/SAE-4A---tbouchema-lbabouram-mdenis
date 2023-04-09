package com.palaref.saequiz.ui.leaderboard;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.palaref.saequiz.R;
import com.palaref.saequiz.databinding.FragmentLeaderboardBinding;

import quiz.api.integration.QuizRequests;
import quiz.api.integration.UserRequests;

public class LeaderboardFragment extends Fragment {

    private LeaderboardViewModel leaderboardViewModel;
    private FragmentLeaderboardBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        leaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        QuizRequests quizRequests = getArguments().getString("quizRequests") != null ? (QuizRequests) getArguments().getSerializable("quizRequests") : null;
        UserRequests userRequests = getArguments().getString("userRequests") != null ? (UserRequests) getArguments().getSerializable("userRequests") : null;

        final TextView textView = binding.textLeaderboard;
        leaderboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText); // basically binds the textView to the String in the ViewModel so that it is updated when the String is updated

        return root;
    }
}