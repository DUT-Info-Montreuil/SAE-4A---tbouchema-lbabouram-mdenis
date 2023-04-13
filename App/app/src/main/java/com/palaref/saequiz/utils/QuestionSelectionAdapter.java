package com.palaref.saequiz.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.palaref.saequiz.R;
import com.palaref.saequiz.model.QuizInfo;

import java.util.List;

public class QuestionSelectionAdapter extends ArrayAdapter<QuizInfo> {
    public QuestionSelectionAdapter(@NonNull Context context, @NonNull List<QuizInfo> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        QuizInfo currentQuiz = getItem(position);
        if(convertView == null)
            convertView = View.inflate(getContext(), R.layout.quiz_select_cell, null);

        TextView title = convertView.findViewById(R.id.title_select_cell);
        title.setText(currentQuiz.getName());

        return convertView;
    }
}
