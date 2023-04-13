package com.palaref.saequiz.ui.admin.admin_home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.palaref.saequiz.databinding.FragmentAdminHomeBinding;
import com.palaref.saequiz.utils.SQLiteManager;

public class AdminHomeFragment extends Fragment {

    private FragmentAdminHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AdminHomeViewModel adminHomeViewModel =
                new ViewModelProvider(this).get(AdminHomeViewModel.class);

        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.changeMonthlyQuizButtonAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangeMonthlyQuizActivity.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.changeMonthlyQuizButtonAdmin.setText("Change Monthly Quiz");
        if(SQLiteManager.getInstance(getContext()).getMonthlyQuiz() == null){
            binding.monthlyQuizDateAdmin.setText("No monthly quiz selected");
        }else{

            binding.monthlyQuizDateAdmin.setText("Last update : " + SQLiteManager.getInstance(getContext()).getMonthlyQuizDate().toString());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}