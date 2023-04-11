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

    //private ActivityQuizGameBinding binding;
    private QuizGame quizGame;
    private TextView questionTextView, scoreTextView, multiplierTextView;
    private Button answer1Button, answer2Button, answer3Button, answer4Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityQuizGameBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_quiz_game);

        Intent intent = getIntent();
        int quizId = intent.getIntExtra("quizId", -1);

        SQLiteManager sqLiteManager = SQLiteManager.getInstance(this);
        quizGame = sqLiteManager.getQuizGameByQuizInfoId(quizId);

        setupViews();
        setupListeners();
        nextQuestion();
    }

    private void setupViews() {
        questionTextView = findViewById(R.id.question_textview_quiz_game);
        scoreTextView = findViewById(R.id.score_textview_quiz_game);
        multiplierTextView = findViewById(R.id.scoremultiplier_textview_quiz_game);
        answer1Button = findViewById(R.id.answer1_button_game);
        answer2Button = findViewById(R.id.answer2_button_game);
        answer3Button = findViewById(R.id.answer3_button_game);
        answer4Button = findViewById(R.id.answer4_button_game);
    }

    private void setupListeners() {
        answer1Button.setOnClickListener(v -> {
            if (quizGame.checkAnswer(0)) {
                correctAnswer();
            }
            else {
                wrongAnswer();
            }
        });
        answer2Button.setOnClickListener(v -> {
            if (quizGame.checkAnswer(1)) {
                correctAnswer();
            }
            else {
                wrongAnswer();
            }
        });
        answer3Button.setOnClickListener(v -> {
            if (quizGame.checkAnswer(2)) {
                correctAnswer();
            }
            else {
                wrongAnswer();
            }
        });
        answer4Button.setOnClickListener(v -> {
            if (quizGame.checkAnswer(3)) {
                correctAnswer();
            }
            else {
                wrongAnswer();
            }
        });
    }

    private void correctAnswer() {
        // toast and delay for 1 second
        setButtonActive(false);
        Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        scoreTextView.setText(String.valueOf("Score : " + quizGame.getScore()));
        multiplierTextView.setText(String.valueOf("x" + quizGame.getMultiplier()));
        new Handler().postDelayed(() -> {
            nextQuestion();
        }, 1000);
    }

    private void wrongAnswer() {
        // toast and delay for 1 second
        setButtonActive(false);
        Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
        scoreTextView.setText(String.valueOf("Score : " + quizGame.getScore()));
        multiplierTextView.setText(String.valueOf("x" + quizGame.getMultiplier()));
        new Handler().postDelayed(() -> {
            nextQuestion();
        }, 1000);
    }

    private void setButtonActive(boolean active) {
        answer1Button.setEnabled(active);
        answer2Button.setEnabled(active);
        answer3Button.setEnabled(active);
        answer4Button.setEnabled(active);
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
        setButtonActive(true);
    }
}