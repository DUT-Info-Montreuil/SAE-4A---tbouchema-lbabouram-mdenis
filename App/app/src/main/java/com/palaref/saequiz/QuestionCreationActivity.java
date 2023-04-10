package com.palaref.saequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class QuestionCreationActivity extends AppCompatActivity {

    private EditText questionEditText, answer1EditText, answer2EditText, answer3EditText, answer4EditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_creation);
    }
}