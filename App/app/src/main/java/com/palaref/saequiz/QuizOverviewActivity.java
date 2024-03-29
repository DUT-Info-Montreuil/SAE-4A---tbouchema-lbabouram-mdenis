package com.palaref.saequiz;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.palaref.saequiz.model.QuizInfo;
import com.palaref.saequiz.utils.SQLiteManager;

import java.util.Arrays;

public class QuizOverviewActivity extends AppCompatActivity {

    private TextView quizName;
    private TextView quizDescription;
    private TextView quizCreator;
    private TextView bestScore;
    private TextView tags;
    private Button playButton;
    private int quizId;

    private final ActivityResultLauncher<Intent> quizLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Check if login was successful
                    int score = result.getData().getIntExtra("score", -1);
                    SQLiteManager sqLiteManager = SQLiteManager.getInstance(this);
                    if (score != -1) {
                        // Load user data and update UI
                        int bestScoreInt = sqLiteManager.getUserBestScore(MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1), quizId);
                        if(score > bestScoreInt && bestScoreInt != -1){
                            Toast.makeText(this, "New best score updated: " + score + " : " + bestScoreInt, Toast.LENGTH_SHORT).show();
                            bestScore.setText("Best score : " + score);
                            sqLiteManager.updateUserBestScore(MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1), quizId, score);
                        } else if (bestScoreInt != -1){
                            Toast.makeText(this, "Your score was : " + score, Toast.LENGTH_SHORT).show();
                            bestScore.setText("Best score : " + bestScoreInt);
                        }else{
                            Toast.makeText(this, "New score was : " + score, Toast.LENGTH_SHORT).show();
                            bestScore.setText("Best score : " + score);
                            sqLiteManager.addBestScoreForUser(MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1), quizId, score);
                        }
                        if(!sqLiteManager.isQuizCompleted(MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1), quizId)){
                            sqLiteManager.addCompletedQuizForUser(MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1), quizId);
                        }
                    } else {
                        // Display error message or retry login
                        Toast.makeText(this, "There was an error getting your score", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_overview);

        Intent intent = getIntent(); // We get the intent that started this activity which contains the quiz id
        quizId = intent.getIntExtra("quizId", -1); // We get the quiz id from the intent
        setupViews();
        setupListeners();
    }

    private void setupViews(){
        quizName = findViewById(R.id.quiz_title_textview_overview);
        quizDescription = findViewById(R.id.description_textview_overview);
        quizCreator = findViewById(R.id.creator_textview_overview);
        bestScore = findViewById(R.id.bestscore_textview_overview);
        tags = findViewById(R.id.tags_textview_overview);
        playButton = findViewById(R.id.play_button_overview);

        SQLiteManager sqLiteManager = SQLiteManager.getInstance(this);

        QuizInfo quizInfo = sqLiteManager.getQuizInfoById(quizId);
        quizName.setText(quizInfo.getName());
        quizDescription.setText(quizInfo.getDescription());
        String creatorString = "By : " + sqLiteManager.getUserById(quizInfo.getCreatorId()).getUsername();
        quizCreator.setText(creatorString);
        setupBestScore();
        String tagsString = "Tags: " + Arrays.toString(quizInfo.getTags());
        tags.setText(tagsString); // TODO: Get tags
    }

    private void setupListeners(){
        playButton.setOnClickListener(v -> startQuiz());
    }

    private void startQuiz(){
        if(quizId == -1) {
            Toast.makeText(this, "There was an error starting the quiz", Toast.LENGTH_SHORT).show();
            return; // If the quiz id is -1, we return
        }

        Intent intent = new Intent(this, QuizGameActivity.class);
        intent.putExtra("quizId", quizId);
        // start activity for result to get the score
        quizLauncher.launch(intent);
    }

    private void setupBestScore(){
        int bestScoreInt = SQLiteManager.getInstance(this).getUserBestScore(MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1), quizId);
        if(bestScoreInt == -1){
            bestScore.setText("You don't have any score for this quiz yet");
        } else {
            bestScore.setText("Best score : " + bestScoreInt);
        }
    }
}