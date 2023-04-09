package com.palaref.saequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.palaref.saequiz.databinding.ActivityQuizGameBinding;
import com.palaref.saequiz.model.QuizGame;
import com.palaref.saequiz.model.QuizQuestion;
import com.palaref.saequiz.utils.SQLiteManager;

public class QuizGameActivity extends AppCompatActivity {

    private ActivityQuizGameBinding binding;
    private QuizGame quizGame;
    private TextView questionTextView, scoreTextView, multiplierTextView;
    private Button answer1Button, answer2Button, answer3Button, answer4Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizGameBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_quiz_game);

        Intent intent = getIntent();
        int quizId = intent.getIntExtra("quizId", -1);

        SQLiteManager sqLiteManager = SQLiteManager.getInstance(this);
        quizGame = sqLiteManager.getQuizGameByQuizInfoId(quizId);

        setupViews();
        nextQuestion();
        setupListeners();
    }

    private void setupViews() {
        questionTextView = binding.questionTextviewQuizGame;
        scoreTextView = binding.scoreTextviewQuizGame;
        multiplierTextView = binding.scoremultiplierTextviewQuizGame;
        answer1Button = binding.answer1ButtonGame;
        answer2Button = binding.answer2ButtonGame;
        answer3Button = binding.answer3ButtonGame;
        answer4Button = binding.answer4ButtonGame;
    }

    private void setupListeners() {
        answer1Button.setOnClickListener(v -> {
            if (quizGame.checkAnswer(0)) {
                nextQuestion();
            }
        });
        answer2Button.setOnClickListener(v -> {
            if (quizGame.checkAnswer(1)) {
                scoreTextView.setText(String.valueOf(quizGame.getScore()));
                multiplierTextView.setText(String.valueOf(quizGame.getMultiplier()));
                nextQuestion();
            }
        });
        answer3Button.setOnClickListener(v -> {
            if (quizGame.checkAnswer(2)) {
                scoreTextView.setText(String.valueOf(quizGame.getScore()));
                multiplierTextView.setText(String.valueOf(quizGame.getMultiplier()));
                nextQuestion();
            }
        });
        answer4Button.setOnClickListener(v -> {
            if (quizGame.checkAnswer(3)) {
                scoreTextView.setText(String.valueOf(quizGame.getScore()));
                multiplierTextView.setText(String.valueOf(quizGame.getMultiplier()));
                nextQuestion();
            }
        });
    }

    private void correctAnswer() {
        // toast and delay for 1 second
        new Handler().postDelayed(() -> {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            scoreTextView.setText(String.valueOf("Score : " + quizGame.getScore()));
            multiplierTextView.setText(String.valueOf("x" + quizGame.getMultiplier()));
            nextQuestion();
        }, 1000);
    }

    private void wrongAnswer() {
        // toast and delay for 1 second
        new Handler().postDelayed(() -> {
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
            scoreTextView.setText(String.valueOf("Score : " + quizGame.getScore()));
            multiplierTextView.setText(String.valueOf("x" + quizGame.getMultiplier()));
            nextQuestion();
        }, 1000);
    }

    private void nextQuestion() {
        QuizQuestion quizQuestion = quizGame.nextQuestion();
        if (quizQuestion == null) {
            // end of game return score to previous activity
            Intent intent = new Intent();
            intent.putExtra("score", quizGame.getScore());
            setResult(RESULT_OK, intent);
            finish();
            return;
        }
        questionTextView.setText(quizQuestion.getQuestion());
        answer1Button.setText(quizQuestion.getAnswers().get(0).getAnswer());
        answer2Button.setText(quizQuestion.getAnswers().get(1).getAnswer());
        answer3Button.setText(quizQuestion.getAnswers().get(2).getAnswer());
        answer4Button.setText(quizQuestion.getAnswers().get(3).getAnswer());
        scoreTextView.setText(String.valueOf("Score : " + quizGame.getScore()));
        multiplierTextView.setText(String.valueOf("x" + quizGame.getMultiplier()));
    }
}