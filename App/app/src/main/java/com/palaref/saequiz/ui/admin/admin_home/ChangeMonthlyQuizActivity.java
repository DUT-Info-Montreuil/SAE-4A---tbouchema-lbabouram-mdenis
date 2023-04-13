package com.palaref.saequiz.ui.admin.admin_home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.palaref.saequiz.R;
import com.palaref.saequiz.model.QuizInfo;
import com.palaref.saequiz.utils.QuestionSelectionAdapter;
import com.palaref.saequiz.utils.SQLiteManager;

public class ChangeMonthlyQuizActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_monthly_quiz);

        listView = findViewById(R.id.select_listview_changemonthly);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // get the selected quiz and update the monthly quiz
            QuizInfo selectedQuiz = (QuizInfo) parent.getItemAtPosition(position);
            if(SQLiteManager.getInstance(this).getMonthlyQuiz() == null){
                SQLiteManager.getInstance(this).addMonthlyQuiz(selectedQuiz.getId());
            }else {
                SQLiteManager.getInstance(this).updateMonthlyQuiz(selectedQuiz.getId());
            }
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        listView.setAdapter(new QuestionSelectionAdapter(this, SQLiteManager.getInstance(this).getAllQuizInfos()));
    }
}