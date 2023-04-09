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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.palaref.saequiz.MainActivity;
import com.palaref.saequiz.R;
import com.palaref.saequiz.databinding.FragmentProfileBinding;
import com.palaref.saequiz.model.User;
import com.palaref.saequiz.utils.SQLiteManager;

import java.util.Objects;

import quiz.api.integration.QuizRequests;
import quiz.api.integration.UserRequests;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private boolean hasPopped = false;
    private Button loginButton;
    private Button logoutButton;

    private final ActivityResultLauncher<Intent> loginLauncher = registerForActivityResult( // this is a variable even though it looks like a method
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Check if login was successful
                    int id = MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1);
                    if (id != -1) {
                        // Load user data and update UI
                        loadUserData(id);
                    } else {
                        // Display error message or retry login
                        profileViewModel.setText("Not logged in");
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                }
            });

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        QuizRequests quizRequests = getArguments().getString("quizRequests") != null ? (QuizRequests) getArguments().getSerializable("quizRequests") : null;
        UserRequests userRequests = getArguments().getString("userRequests") != null ? (UserRequests) getArguments().getSerializable("userRequests") : null;

        final TextView textView = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        loginButton = binding.loginButtonProfileFragment;
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            loginLauncher.launch(intent);
        });

        logoutButton = binding.logoutButton;
        logoutButton.setOnClickListener(v -> {
            MainActivity.sharedPreferences.edit().putInt(MainActivity.USER_ID, -1).apply();
            profileViewModel.setText("Not logged in");
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1) != -1)
            loginButton.setVisibility(View.GONE);
        if(MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1) == -1)
            logoutButton.setVisibility(View.GONE);

        int id = MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1);
        if(id == -1 && !hasPopped){
            // not logged in
            hasPopped = true;
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
        else if (id == -1)
            profileViewModel.setText("Not logged in");
        else
            profileViewModel.setText("User not found");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}