package com.palaref.saequiz.ui.leaderboard;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.palaref.saequiz.R;
import com.palaref.saequiz.databinding.FragmentLeaderboardBinding;
import com.palaref.saequiz.utils.LeaderboardAdapter;
import com.palaref.saequiz.utils.SQLiteManager;

import java.util.Objects;

public class LeaderboardFragment extends Fragment {

    private LeaderboardViewModel leaderboardViewModel;
    private FragmentLeaderboardBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        leaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SQLiteManager sqLiteManager = SQLiteManager.getInstance(getContext());

        final ListView leaderboardListView = binding.leaderboardListview;
        leaderboardListView.setAdapter(new LeaderboardAdapter(requireContext(), sqLiteManager.get10MostCompletionUsersOfMonths(SQLiteManager.getNowDate())));
        return root;
    }
}