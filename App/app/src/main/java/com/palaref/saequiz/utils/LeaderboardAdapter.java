package com.palaref.saequiz.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.palaref.saequiz.R;
import com.palaref.saequiz.model.User;

import java.util.List;

public class LeaderboardAdapter extends ArrayAdapter<User> {
    public LeaderboardAdapter(@NonNull Context context, @NonNull List<User> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User currentUser = getItem(position);
        if(convertView == null)
            convertView = View.inflate(getContext(), R.layout.leaderboard_score_cell, null);

        TextView rank = convertView.findViewById(R.id.leaderboard_score_cell_rank);
        rank.setText(String.valueOf(position + 1));

        TextView name = convertView.findViewById(R.id.leaderboard_score_cell_name);
        name.setText(currentUser.getUsername());

        TextView score = convertView.findViewById(R.id.leaderboard_score_cell_score);
        score.setText(SQLiteManager.getInstance(getContext()).getCountOfUserCompleted(currentUser.getId()) + "pts");

        return convertView;
    }
}
