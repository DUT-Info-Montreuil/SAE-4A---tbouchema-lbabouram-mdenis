package com.palaref.saequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.palaref.saequiz.databinding.ActivityQuizGameBinding;
import com.palaref.saequiz.model.QuizGame;
import com.palaref.saequiz.utils.AnswerAdapter;

public class QuizGameActivity extends AppCompatActivity {

    private ActivityQuizGameBinding binding;
    private QuizGame quizGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizGameBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_quiz_game);

        Intent intent = getIntent();
        int quizId = intent.getIntExtra("quizId", -1);

        // get quiz from database
    }
}