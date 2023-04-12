package com.palaref.saequiz.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.palaref.saequiz.R;
import com.palaref.saequiz.model.QuizInfo;

import java.util.List;

public class QuizGridAdapter extends ArrayAdapter<QuizInfo> {


    public QuizGridAdapter(Context context, List<QuizInfo> quizInfos) {
        super(context, 0, quizInfos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        QuizInfo currentQuiz = getItem(position);
        if(convertView == null)
            convertView = View.inflate(getContext(), R.layout.favorite_quizzes_cell, null);

        TextView mainText = convertView.findViewById(R.id.quiz_main_textview_favorite);
        mainText.setText(currentQuiz.getName());

        return convertView;
    }
}
