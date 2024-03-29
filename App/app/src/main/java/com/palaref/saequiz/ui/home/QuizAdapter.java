package com.palaref.saequiz.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.palaref.saequiz.MainActivity;
import com.palaref.saequiz.QuizOverviewActivity;
import com.palaref.saequiz.R;
import com.palaref.saequiz.model.QuizInfo;
import com.palaref.saequiz.utils.SQLiteManager;

import java.util.ArrayList;

public class QuizAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<QuizInfo> quizInfoList;

    private static final int VIEW_TYPE_MONTHLY_QUIZ = 0;
    private static final int VIEW_TYPE_QUIZ = 1;


    public QuizAdapter(Context context, QuizInfo monthlyQuiz, ArrayList<QuizInfo> quizInfoList) {
        // This is where we initialize the adapter
        this.inflater = LayoutInflater.from(context);

        // We add the monthly quiz to the list in the first position
        this.quizInfoList = new ArrayList<>();
        this.quizInfoList.add(monthlyQuiz);
        this.quizInfoList.addAll(quizInfoList);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) { // If the position is 0, we return the monthly button
            return VIEW_TYPE_MONTHLY_QUIZ;
        } else { // Otherwise, we return the normal button
            return VIEW_TYPE_QUIZ;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MONTHLY_QUIZ) {
            View view = inflater.inflate(R.layout.monthly_quiz_button_view, parent, false);
            return new MonthlyQuizViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.quiz_button_view, parent, false);
            return new QuizViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MonthlyQuizViewHolder) {
            // Bind monthly button layout
            MonthlyQuizViewHolder monthlyQuizViewHolder = (MonthlyQuizViewHolder) holder;
            SQLiteManager sqLiteManager = SQLiteManager.getInstance(inflater.getContext());
            QuizInfo monthlyQuiz = sqLiteManager.getMonthlyQuiz();
            if(monthlyQuiz == null){
                // hide Monthly Quiz button
                monthlyQuizViewHolder.mainButton.setVisibility(View.GONE);
                monthlyQuizViewHolder.bar.setVisibility(View.GONE);
            }
            else {
                monthlyQuizViewHolder.mainButton.setVisibility(View.VISIBLE);
                monthlyQuizViewHolder.bar.setVisibility(View.VISIBLE);
                monthlyQuizViewHolder.mainButton.setText("Quiz of the month : " + monthlyQuiz.getName());
                monthlyQuizViewHolder.mainButton.setOnClickListener(v -> {
                    // start QuizOverviewActivity
                    Intent intent = new Intent(inflater.getContext(), QuizOverviewActivity.class);
                    intent.putExtra("quizId", monthlyQuiz.getId());
                    inflater.getContext().startActivity(intent);
                });
            }
        } else {
            QuizInfo quizInfo = quizInfoList.get(position);
            QuizViewHolder quizViewHolder = (QuizViewHolder) holder;
            quizViewHolder.mainButton.setText(quizInfo.getName());

            if(MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1) == -1)
                quizViewHolder.heartIcon.setVisibility(View.INVISIBLE);
            else {
                quizViewHolder.heartIcon.setVisibility(View.VISIBLE);
                int userId = MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1);
                SQLiteManager sqliteManager = SQLiteManager.getInstance(inflater.getContext());
                if(sqliteManager.isQuizFavorite(userId, quizInfo.getId()))
                    quizViewHolder.heartIcon.setImageResource(R.drawable.heart);
                else
                    quizViewHolder.heartIcon.setImageResource(R.drawable.emptyheart);
            }

            // Bind button layout and make buttons launch quiz activity when it is ready
            quizViewHolder.mainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // start QuizOverviewActivity
                    Intent intent = new Intent(inflater.getContext(), QuizOverviewActivity.class);
                    intent.putExtra("quizId", quizInfo.getId());
                    inflater.getContext().startActivity(intent);
                }
            });
            quizViewHolder.heartIcon.setOnClickListener(v -> {
                int userId = MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1);
                SQLiteManager sqliteManager = SQLiteManager.getInstance(inflater.getContext());
                if(userId == -1)
                    Toast.makeText(inflater.getContext(), "Please log in to favorite quizzes", Toast.LENGTH_SHORT).show();

                if(sqliteManager.isQuizFavorite(userId, quizInfo.getId())) {
                    sqliteManager.removeFavoriteForUser(userId, quizInfo.getId());
                    quizViewHolder.heartIcon.setImageResource(R.drawable.emptyheart);
                } else {
                    sqliteManager.addFavoriteForUser(userId, quizInfo.getId());
                    quizViewHolder.heartIcon.setImageResource(R.drawable.heart);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        // This is where we return the number of items in the list
        return quizInfoList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {
        // This is where we define the view holder
        // We can define the views here

        ImageView bottomBar;
        Button mainButton;
        ImageView heartIcon;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            bottomBar = itemView.findViewById(R.id.bottom_bar);
            mainButton = itemView.findViewById(R.id.main_button);
            heartIcon = itemView.findViewById(R.id.heart_imageview_main_quiz);
        }
    }
    private static class MonthlyQuizViewHolder extends RecyclerView.ViewHolder {
        Button mainButton;
        ImageView bar;
        MonthlyQuizViewHolder(View itemView) {
            super(itemView);
            mainButton = itemView.findViewById(R.id.monthly_quiz_button_home);
            bar = itemView.findViewById(R.id.monthly_quiz_bar_home);
        }
    }
}