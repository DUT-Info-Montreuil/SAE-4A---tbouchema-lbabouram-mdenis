package com.palaref.saequiz.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.palaref.saequiz.MainActivity;
import com.palaref.saequiz.databinding.FragmentFavoritesBinding;
import com.palaref.saequiz.utils.QuizGridAdapter;
import com.palaref.saequiz.utils.SQLiteManager;

import java.util.Objects;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;
    private FavoritesViewModel favoritesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final AppCompatButton yourFavoriteButton = binding.yourFavoritesButtonFavorite;
        yourFavoriteButton.setOnClickListener(v -> {
            favoritesViewModel.setState("fav");
            refreshAdapter();
        });

        final AppCompatButton yourQuizzesButton = binding.yourQuizzesButtonFavorite;
        yourQuizzesButton.setOnClickListener(v -> {
            favoritesViewModel.setState("my");
            refreshAdapter();
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshAdapter();
    }

    private void refreshAdapter() {
        int userId = MainActivity.sharedPreferences.getInt(MainActivity.USER_ID, -1);

        if(userId == -1){
            // set everything to invisible and show a message
            binding.yourFavoritesButtonFavorite.setVisibility(View.INVISIBLE);
            binding.yourQuizzesButtonFavorite.setVisibility(View.INVISIBLE);
            binding.quizzesGridviewFavorite.setVisibility(View.INVISIBLE);
            binding.noFavoritesTextviewFavorite.setVisibility(View.VISIBLE);

            switch (Objects.requireNonNull(favoritesViewModel.getState().getValue())) {
                case "fav":
                    binding.noFavoritesTextviewFavorite.setText("You need to be logged in to see your favorites");
                    break;
                case "my":
                    binding.noFavoritesTextviewFavorite.setText("You need to be logged in to see your quizzes");
                    break;
            }
            return;
        }else {
            binding.yourFavoritesButtonFavorite.setVisibility(View.VISIBLE);
            binding.yourQuizzesButtonFavorite.setVisibility(View.VISIBLE);
            binding.quizzesGridviewFavorite.setVisibility(View.VISIBLE);
            binding.noFavoritesTextviewFavorite.setVisibility(View.INVISIBLE);
        }

        SQLiteManager sqLiteManager = SQLiteManager.getInstance(this.getContext());
        final GridView gridView = binding.quizzesGridviewFavorite;
        switch (Objects.requireNonNull(favoritesViewModel.getState().getValue())) {
            case "fav":
                gridView.setAdapter(new QuizGridAdapter(getContext(), sqLiteManager.getAllFavoritesOfUser(userId)));
                binding.yourFavoritesButtonFavorite.setAlpha(1f);
                binding.yourQuizzesButtonFavorite.setAlpha(0.5f);
                break;
            case "my":
                gridView.setAdapter(new QuizGridAdapter(getContext(), sqLiteManager.getAllQuizzesOfUser(userId)));
                binding.yourFavoritesButtonFavorite.setAlpha(0.5f);
                binding.yourQuizzesButtonFavorite.setAlpha(1f);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}