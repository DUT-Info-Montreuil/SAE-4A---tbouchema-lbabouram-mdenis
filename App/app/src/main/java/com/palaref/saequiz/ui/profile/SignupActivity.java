package com.palaref.saequiz.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.palaref.saequiz.R;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setupViews();
        setupListeners();
    }

    private void setupViews() {
        usernameEditText = findViewById(R.id.username_edittext);
        passwordEditText = findViewById(R.id.password_edittext_signup);
        emailEditText = findViewById(R.id.email_edittext);
        signUpButton = findViewById(R.id.signup_button);
    }

    private void setupListeners() {
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                enableSignUpButton();
            }
        });
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                enableSignUpButton();
            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                enableSignUpButton();
            }
        });

        signUpButton.setOnClickListener(v -> signUp());
    }

    private void enableSignUpButton() {
        signUpButton.setEnabled(!usernameEditText.getText().toString().isEmpty()
                && !emailEditText.getText().toString().isEmpty()
                && !passwordEditText.getText().toString().isEmpty());
    }

    private void signUp() {
        Intent data = new Intent();
        data.putExtra("username", usernameEditText.getText().toString());
        data.putExtra("password", passwordEditText.getText().toString());
        data.putExtra("email", emailEditText.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }
}