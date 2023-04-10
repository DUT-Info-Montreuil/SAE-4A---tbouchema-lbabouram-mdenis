package com.palaref.saequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class QuestionCreationActivity extends AppCompatActivity {

    private EditText questionEditText, answer1EditText, answer2EditText, answer3EditText, answer4EditText;
    private Button saveButton;
    private RadioGroup correctAnswerRadioGroup;
    private RadioButton correctAnswerRadioButton1, correctAnswerRadioButton2, correctAnswerRadioButton3, correctAnswerRadioButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_creation);

        // get if the question is being edited or created
        int questionNumber = getIntent().getIntExtra("questionNumber", -1);
        setupViews();

        if(getIntent().getBooleanExtra("isEdit", false)){
            updateViews();
        }

        saveButton.setOnClickListener(v -> {
            String question = questionEditText.getText().toString();
            String answer1 = answer1EditText.getText().toString();
            String answer2 = answer2EditText.getText().toString();
            String answer3 = answer3EditText.getText().toString();
            String answer4 = answer4EditText.getText().toString();
            int correctAnswer = correctAnswerRadioGroup.getCheckedRadioButtonId();
            switch (correctAnswer) {
                case R.id.radio1_question_creation:
                    correctAnswer = 0;
                    break;
                case R.id.radio2_question_creation:
                    correctAnswer = 1;
                    break;
                case R.id.radio3_question_creation:
                    correctAnswer = 2;
                    break;
                case R.id.radio4_question_creation:
                    correctAnswer = 3;
                    break;
            }

            // put the data needed to create the question in the intent
            getIntent().putExtra("question", question);
            getIntent().putExtra("answer1", answer1);
            getIntent().putExtra("answer2", answer2);
            getIntent().putExtra("answer3", answer3);
            getIntent().putExtra("answer4", answer4);
            getIntent().putExtra("correctAnswer", correctAnswer);
            getIntent().putExtra("questionNumber", questionNumber);
            setResult(RESULT_OK, getIntent());
            finish();
        });

        setupSaveButtonActive();
    }

    private void setupSaveButtonActive() {
        // set save button active only if all fields are filled
        questionEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                checkIfButtonShouldBeActive();
            }
        });
        answer1EditText.setOnFocusChangeListener((v, hasFocus) -> {
                    if(!hasFocus){
                        checkIfButtonShouldBeActive();
                    }
                });
        answer2EditText.setOnFocusChangeListener((v, hasFocus) -> {
                    if(!hasFocus){
                        checkIfButtonShouldBeActive();
                    }
                });
        answer3EditText.setOnFocusChangeListener((v, hasFocus) -> {
                    if(!hasFocus){
                        checkIfButtonShouldBeActive();
                    }
                });
        answer4EditText.setOnFocusChangeListener((v, hasFocus) -> {
                    if(!hasFocus){
                        checkIfButtonShouldBeActive();
                    }
                });
        correctAnswerRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    checkIfButtonShouldBeActive();
                });
    }

    private void updateViews() {
        String question = getIntent().getStringExtra("question");
        String answer1 = getIntent().getStringExtra("answer1");
        String answer2 = getIntent().getStringExtra("answer2");
        String answer3 = getIntent().getStringExtra("answer3");
        String answer4 = getIntent().getStringExtra("answer4");
        int correctAnswer = getIntent().getIntExtra("correctAnswer", -1);
        // set the data in the views
        questionEditText.setText(question);
        answer1EditText.setText(answer1);
        answer2EditText.setText(answer2);
        answer3EditText.setText(answer3);
        answer4EditText.setText(answer4);
        switch (correctAnswer) {
            case 0:
                correctAnswerRadioButton1.setChecked(true);
                break;
            case 1:
                correctAnswerRadioButton2.setChecked(true);
                break;
            case 2:
                correctAnswerRadioButton3.setChecked(true);
                break;
            case 3:
                correctAnswerRadioButton4.setChecked(true);
                break;
        }
    }

    private void setupViews() {
        questionEditText = findViewById(R.id.question_edittext_question_creation);
        answer1EditText = findViewById(R.id.answer1_edittext_question_creation);
        answer2EditText = findViewById(R.id.answer2_edittext_question_creation);
        answer3EditText = findViewById(R.id.answer3_edittext_question_creation);
        answer4EditText = findViewById(R.id.answer4_edittext_question_creation);
        correctAnswerRadioGroup = findViewById(R.id.radiogroup_question_creation);
        correctAnswerRadioButton1 = findViewById(R.id.radio1_question_creation);
        correctAnswerRadioButton2 = findViewById(R.id.radio2_question_creation);
        correctAnswerRadioButton3 = findViewById(R.id.radio3_question_creation);
        correctAnswerRadioButton4 = findViewById(R.id.radio4_question_creation);
        saveButton = findViewById(R.id.savequestion_button_question_creation);
        checkIfButtonShouldBeActive();
    }

    private void checkIfButtonShouldBeActive(){
        saveButton.setEnabled(!questionEditText.getText().toString().isEmpty()
                && !answer1EditText.getText().toString().isEmpty()
                && !answer2EditText.getText().toString().isEmpty()
                && !answer3EditText.getText().toString().isEmpty()
                && !answer4EditText.getText().toString().isEmpty()
                && correctAnswerRadioGroup.getCheckedRadioButtonId() != -1);
    }
}