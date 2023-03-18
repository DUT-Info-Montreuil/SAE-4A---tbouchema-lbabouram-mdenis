package com.palaref.saequiz.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.palaref.saequiz.R;
import com.palaref.saequiz.model.Quiz;

import java.util.ArrayList;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    Context context;
    ArrayList<Quiz> quizList;


    public QuizAdapter(Context context, ArrayList<Quiz> quizList) {
        // This is where we initialize the adapter
        this.context = context;
        this.quizList = quizList;
    }


    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This is where we inflate the view and return the view holder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.quiz_button_view, parent, false);

        return new QuizAdapter.QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        // This is where we update the view holder with the data
        holder.mainButton.setText(quizList.get(position).getName());
        // bind the data to the view holder
    }

    @Override
    public int getItemCount() {
        // This is where we return the number of items in the list
        return quizList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        // This is where we define the view holder
        // We can define the views here

        ImageView bottomBar;
        Button mainButton;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            bottomBar = itemView.findViewById(R.id.bottom_bar);
            mainButton = itemView.findViewById(R.id.main_button);
        }
    }
}
