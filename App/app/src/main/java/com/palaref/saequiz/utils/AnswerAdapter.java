package com.palaref.saequiz.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatButton;

import com.palaref.saequiz.R;
import com.palaref.saequiz.model.QuizAnswer;

public class AnswerAdapter extends BaseAdapter {
    private Context context;
    private QuizAnswer[] answers;
    private LayoutInflater inflater;

    public AnswerAdapter(Context context, QuizAnswer[] answers) {
        this.context = context;
        this.answers = answers;
    }

    @Override
    public int getCount() {
        return answers.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
            convertView = inflater.inflate(R.layout.question_grid_item, null);

        AppCompatButton answerButton = convertView.findViewById(R.id.answer_button_quiz_game);
        answerButton.setText(answers[position].getAnswer());


        return null;
    }
}
