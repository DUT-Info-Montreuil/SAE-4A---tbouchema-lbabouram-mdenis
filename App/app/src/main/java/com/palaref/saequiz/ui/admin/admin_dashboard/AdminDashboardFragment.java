package com.palaref.saequiz.ui.admin.admin_dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.palaref.saequiz.databinding.FragmentAdminDashboardBinding;

import java.util.Objects;

public class AdminDashboardFragment extends Fragment {

    private FragmentAdminDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AdminDashboardViewModel adminDashboardViewModel =
                new ViewModelProvider(this).get(AdminDashboardViewModel.class);

        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.adminDashboardTitle;
        adminDashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final Button exitButton = binding.exitAdminButton; // finish activity on click
        exitButton.setOnClickListener(v -> requireActivity().finish());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}