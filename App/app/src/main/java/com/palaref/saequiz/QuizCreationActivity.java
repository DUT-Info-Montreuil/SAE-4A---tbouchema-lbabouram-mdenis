package com.palaref.saequiz;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.palaref.saequiz.model.QuizAnswer;
import com.palaref.saequiz.model.QuizGame;
import com.palaref.saequiz.model.QuizInfo;
import com.palaref.saequiz.model.QuizQuestion;
import com.palaref.saequiz.utils.QuestionAdapter;
import com.palaref.saequiz.utils.SQLiteManager;

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
                    assert result.getData() != null;
                    String questionText = result.getData().getStringExtra("question");
                    String answer1 = result.getData().getStringExtra("answer1");
                    String answer2 = result.getData().getStringExtra("answer2");
                    String answer3 = result.getData().getStringExtra("answer3");
                    String answer4 = result.getData().getStringExtra("answer4");
                    int correctAnswer = result.getData().getIntExtra("correctAnswer", -1);
                    int questionNumber = result.getData().getIntExtra("questionNumber", -1);
                    ArrayList<QuizAnswer> answers = new ArrayList<>();
                    answers.add(new QuizAnswer(answer1, correctAnswer == 0, 1));
                    answers.add(new QuizAnswer(answer2, correctAnswer == 1, 2));
                    answers.add(new QuizAnswer(answer3, correctAnswer == 2, 3));
                    answers.add(new QuizAnswer(answer4, correctAnswer == 3, 4));
                    QuizQuestion question = new QuizQuestion(questionText, answers, questionNumber);
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
                    assert result.getData() != null;
                    String questionText = result.getData().getStringExtra("question");
                    String answer1 = result.getData().getStringExtra("answer1");
                    String answer2 = result.getData().getStringExtra("answer2");
                    String answer3 = result.getData().getStringExtra("answer3");
                    String answer4 = result.getData().getStringExtra("answer4");
                    int correctAnswer = result.getData().getIntExtra("correctAnswer", -1);
                    int questionNumber = result.getData().getIntExtra("questionNumber", -1);
                    if (questionNumber != -1) {
                        questions.set(questionNumber, new QuizQuestion(questionText, new ArrayList<QuizAnswer>() {{
                            add(new QuizAnswer(answer1, correctAnswer == 0, 1));
                            add(new QuizAnswer(answer2, correctAnswer == 1, 2));
                            add(new QuizAnswer(answer3, correctAnswer == 2, 3));
                            add(new QuizAnswer(answer4, correctAnswer == 3, 4));
                        }}, questionNumber));
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
        checkIfSubmitIsPossible();
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
            intent.putExtra("questionNumber", questions.size());
            intent.putExtra("isEdit", false);
            questionCreationLauncher.launch(intent);
        });

        questionsListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, QuestionCreationActivity.class);
            intent.putExtra("question", questions.get(position).getQuestion());
            intent.putExtra("answer1", questions.get(position).getAnswers().get(0).getAnswer());
            intent.putExtra("answer2", questions.get(position).getAnswers().get(1).getAnswer());
            intent.putExtra("answer3", questions.get(position).getAnswers().get(2).getAnswer());
            intent.putExtra("answer4", questions.get(position).getAnswers().get(3).getAnswer());
            intent.putExtra("correctAnswer", questions.get(position).getAnswers().indexOf(questions.get(position).getAnswers().stream().filter(QuizAnswer::isCorrect).findFirst().get()));
            intent.putExtra("questionNumber", position);
            intent.putExtra("isEdit", true);
            questionUpdateLauncher.launch(intent);
        });

        submitQuizButton.setOnClickListener(v -> submitQuiz());

        setupListenersToCheckIfSubmitIsPossible();
    }

    private void setupListenersToCheckIfSubmitIsPossible() {
        quizNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                checkIfSubmitIsPossible();
            }
        });
        quizDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                checkIfSubmitIsPossible();
            }
        });
    }

    private void checkIfSubmitIsPossible() {
        submitQuizButton.setEnabled(quizNameEditText.getText().toString().length() > 0
                && quizDescriptionEditText.getText().toString().length() > 0
                && questions.size() > 0);
    }

    // creates quizInfo and quizGame objects and saves them to the database
    private void submitQuiz() {
        SQLiteManager sqLiteManager = SQLiteManager.getInstance(this);
        // quizinfo : name, description, creatorId, date
        QuizInfo quizInfo = new QuizInfo(quizNameEditText.getText().toString(), quizDescriptionEditText.getText().toString(), MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1), SQLiteManager.getNowDate());
        sqLiteManager.addQuiz(quizInfo);
        // quizgame : array of questions, quizInfoId
        QuizGame quizGame = new QuizGame(questions, quizInfo.getId());
        sqLiteManager.addQuizGame(quizGame);
        //delay and toast
        new Handler().postDelayed(() -> Toast.makeText(this, "Quiz created successfully!", Toast.LENGTH_SHORT).show(), 1000);
        finish();
    }
}