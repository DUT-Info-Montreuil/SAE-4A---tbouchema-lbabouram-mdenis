package com.palaref.saequiz.ui.profile;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.palaref.saequiz.MainActivity;
import com.palaref.saequiz.R;
import com.palaref.saequiz.model.User;
import com.palaref.saequiz.utils.SQLiteManager;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signUpButton;
    private EditText userEditText; // can either be email or username
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupViews();
        setupListeners();
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> login());
        signUpButton.setOnClickListener(v -> signUp());
        userEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableLoginButton();
            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableLoginButton();
            }
        });
    }

    private void setupViews() {
        loginButton = findViewById(R.id.log_in_button);
        signUpButton = findViewById(R.id.sign_up_button);
        userEditText = findViewById(R.id.user_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
    }

    private void enableLoginButton() {
        loginButton.setEnabled(!userEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty());
    }

    private void login() {
        // TODO: implement login by calling the API to authenticate the user and then open the main activity
        // since API is not implemented yet only use shared preferences
        String user = userEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        // for now just gonna check if user is in local database and not care about password
        User userObject = SQLiteManager.getInstance(this).getUserByUsername(user);
        if(userObject != null) {
            MainActivity.sharedPreferences.edit().putInt(MainActivity.USER_ID, userObject.getId()).apply();
            finish();
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void signUp() {
        // TODO: implement sign up by opening a new activity with a form and a button to submit which will call the API to create a new user and then log in
        // since API is not implemented yet only use shared preferences
        Intent intent = new Intent(this, SignupActivity.class);
        //startActivityForResult(intent, 69);
        // for now some data will be left behind in the form
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                String username = data.getStringExtra("username");
                String password = data.getStringExtra("password");
                String email = data.getStringExtra("email");
                User user = new User(username, this);
                SQLiteManager.getInstance(this).addUser(user);
                MainActivity.sharedPreferences.edit().putInt(MainActivity.USER_ID, user.getId()).apply();
                finish();
            }
        });
    }
}