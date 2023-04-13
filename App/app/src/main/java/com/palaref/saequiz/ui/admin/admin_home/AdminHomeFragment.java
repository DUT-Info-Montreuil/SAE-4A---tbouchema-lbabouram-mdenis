package com.palaref.saequiz.ui.admin.admin_home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.palaref.saequiz.databinding.FragmentAdminHomeBinding;

public class AdminHomeFragment extends Fragment {

    private FragmentAdminHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AdminHomeViewModel adminHomeViewModel =
                new ViewModelProvider(this).get(AdminHomeViewModel.class);

        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.changeMonthlyQuizButtonAdmin.setOnClickListener(v -> {
            // start activty that lists all quizzes and return the selected quiz
            // then update the monthly quiz
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}