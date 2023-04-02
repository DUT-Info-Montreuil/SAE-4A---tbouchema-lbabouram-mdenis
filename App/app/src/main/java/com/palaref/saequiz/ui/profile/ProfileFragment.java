package com.palaref.saequiz.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.palaref.saequiz.MainActivity;
import com.palaref.saequiz.databinding.FragmentProfileBinding;
import com.palaref.saequiz.model.User;
import com.palaref.saequiz.utils.SQLiteManager;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;

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
                        profileViewModel.setText("Not logged in");
                    }
                }
            });

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final Button loginButton = binding.loginButtonProfileFragment;
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            loginLauncher.launch(intent);
        });
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
        Log.d("ProfileFragment", "loadUserData: " + id);
        User user = SQLiteManager.getInstance(getContext()).getUserById(id);
        if(user != null)
            profileViewModel.setText("Logged in as " + user.getUsername());
        else
            profileViewModel.setText("User not found");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}