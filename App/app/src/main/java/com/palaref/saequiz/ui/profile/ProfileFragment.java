package com.palaref.saequiz.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.palaref.saequiz.MainActivity;
import com.palaref.saequiz.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel notificationsViewModel;

    private final ActivityResultLauncher<Intent> loginLauncher = registerForActivityResult( // this is a variable even though it looks like a method
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Check if login was successful
                    int id = MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1);
                    if (id != -1) {
                        // Load user data and update UI
                        // ...
                        loadUserData(id);
                    } else {
                        // Display error message or retry login
                        // ...
                    }
                }
            });

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfile;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        int id = MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1);
        if(id == -1){
            // not logged in
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            loginLauncher.launch(intent);
        }else {
            // logged in
            loadUserData(id);
        }
    }

    private void loadUserData(int id){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}