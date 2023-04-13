package com.palaref.saequiz.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.palaref.saequiz.AdminActivity;
import com.palaref.saequiz.MainActivity;
import com.palaref.saequiz.R;
import com.palaref.saequiz.databinding.FragmentProfileBinding;
import com.palaref.saequiz.model.User;
import com.palaref.saequiz.utils.SQLiteManager;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private boolean hasPopped = false;
    private Button loginButton, logoutButton, adminButton;

    private TextView usernameTextView, bioTextView;

    private ImageView profileImageView;

    private TextView statusText;


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

        setupViews();

        setupListeners();

        return root;
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            loginLauncher.launch(intent);
        });

        logoutButton.setOnClickListener(v -> {
            MainActivity.sharedPreferences.edit().putInt(MainActivity.USER_ID, -1).apply();
            profileViewModel.setText("Not logged in");
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);
        });

        adminButton.setOnClickListener(v -> {
            // start admin activity which isn't implemented yet
            Intent intent = new Intent(getActivity(), AdminActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void onStart() {
        super.onStart();

        if(isLoggedIn()){
            displayLoggedInViews();
            loadUserData(MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1));
        }else{
            displayLoggedOutViews();
            popLogginActivity();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isLoggedIn()){
            displayLoggedInViews();
            loadUserData(MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1));
        }else{
            displayLoggedOutViews();
            popLogginActivity();
        }
    }

    private void popLogginActivity() {
        if(!hasPopped){
            // not logged in
            hasPopped = true;
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            loginLauncher.launch(intent);
        }
    }

    public void setupViews(){
        statusText = binding.textProfile;
        profileViewModel.getText().observe(getViewLifecycleOwner(), statusText::setText);
        loginButton = binding.loginButtonProfileFragment;
        logoutButton = binding.logoutButton;
        adminButton = binding.adminButtonProfile;
        usernameTextView = binding.usernameTextviewProfile;
        bioTextView = binding.bioTextviewProfile;
        profileImageView = binding.ppImageviewProfile;
    }

    private void loadUserData(int id){
        Log.d("ProfileFragment", "loadUserData: " + id);
        User user = SQLiteManager.getInstance(getContext()).getUserById(id);

        if(user == null){
            profileViewModel.setText("User not found");
            return;
        }

        usernameTextView.setText(user.getUsername());
        bioTextView.setText(user.getDescription());
        profileImageView.setImageBitmap(user.getProfilePicture());
        profileViewModel.setText("Logged in as " + user.getUsername());
        if(SQLiteManager.getInstance(getContext()).isAdmin(user.getId()))
            adminButton.setVisibility(View.VISIBLE);
        else
            adminButton.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public boolean isLoggedIn(){ // possibly move this to parent class if time permits
        int id = MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1);
        return id != -1 && SQLiteManager.getInstance(this.getContext()).getUserById(id) != null;
    }

    private void displayLoggedInViews(){
        loginButton.setVisibility(View.GONE);
        logoutButton.setVisibility(View.VISIBLE);
        usernameTextView.setVisibility(View.VISIBLE);
        bioTextView.setVisibility(View.VISIBLE);
        profileImageView.setVisibility(View.VISIBLE);
        statusText.setVisibility(View.VISIBLE);
    }

    private void displayLoggedOutViews(){
        loginButton.setVisibility(View.VISIBLE);
        logoutButton.setVisibility(View.GONE);
        usernameTextView.setVisibility(View.GONE);
        bioTextView.setVisibility(View.GONE);
        profileImageView.setVisibility(View.GONE);
        adminButton.setVisibility(View.GONE);

        statusText.setVisibility(View.VISIBLE);
        statusText.setText("Not logged in");
    }
}