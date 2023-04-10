package com.palaref.saequiz;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.palaref.saequiz.model.QuizQuestion;
import com.palaref.saequiz.utils.QuestionAdapter;

import java.util.ArrayList;

public class QuizCreationActivity extends AppCompatActivity {
    private ListView questionsListView;
    private ArrayList<QuizQuestion> questions = new ArrayList<>();
    private Button addQuestionButton, submitQuizButton;
    private EditText quizNameEditText, quizDescriptionEditText;

    // this is used to add a question to the list
    private final ActivityResultLauncher<Intent> questionCreationLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    QuizQuestion question = (QuizQuestion) result.getData().getSerializableExtra("question");
                    questions.add(question);
                }
                else {
                    Toast.makeText(this, "There was an error adding the question.", Toast.LENGTH_SHORT).show();
                }
            });

    // this is used to update a question in the list
    private final ActivityResultLauncher<Intent> questionUpdateLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    QuizQuestion question = (QuizQuestion) result.getData().getSerializableExtra("question");
                    int index = result.getData().getIntExtra("index", -1);
                    if (index != -1) {
                        questions.set(index, question);
                    }
                }
                else {
                    Toast.makeText(this, "There was an error updating the question.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_creation);

        setupViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupList();
    }

    private void setupList(){
        QuestionAdapter questionAdapter = new QuestionAdapter(this, questions);
        questionsListView.setAdapter(questionAdapter);
    }

    private void setupViews() {
        questionsListView = findViewById(R.id.questions_listview_quiz_creation);
        addQuestionButton = findViewById(R.id.addquestion_button_quiz_creation);
        submitQuizButton = findViewById(R.id.submit_button_quiz_creation);
        quizNameEditText = findViewById(R.id.title_edittext_quiz_creation);
        quizDescriptionEditText = findViewById(R.id.description_edittext_quiz_creation);
    }

    private void setupListeners() {
        addQuestionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuestionCreationActivity.class);
            questionCreationLauncher.launch(intent);
        });
    }
}