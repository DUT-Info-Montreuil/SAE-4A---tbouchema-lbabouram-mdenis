package com.palaref.saequiz.ui.search;

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
import com.palaref.saequiz.databinding.FragmentSearchBinding;
import com.palaref.saequiz.model.User;

import quiz.api.integration.QuizRequests;
import quiz.api.integration.UserRequests;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private FragmentSearchBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        QuizRequests quizRequests = getArguments().getString("quizRequests") != null ? (QuizRequests) getArguments().getSerializable("quizRequests") : null;
        UserRequests userRequests = getArguments().getString("userRequests") != null ? (UserRequests) getArguments().getSerializable("userRequests") : null;

        final TextView textView = binding.textSearch;
        searchViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}