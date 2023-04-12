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
import com.palaref.saequiz.model.QuizQuestion;

import java.util.List;

public class QuestionAdapter extends ArrayAdapter<QuizQuestion>{
    public QuestionAdapter(Context context, List<QuizQuestion> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        QuizQuestion question = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cell_question_quiz_creation, parent, false);

        TextView questionName = convertView.findViewById(R.id.questionname_textview_cell);
        questionName.setText(question.getQuestion());

        return convertView;
    }
}
