package com.palaref.saequiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.palaref.saequiz.databinding.ActivityMainBinding;
import com.palaref.saequiz.model.QuizInfo;
import com.palaref.saequiz.model.User;
import com.palaref.saequiz.utils.SQLiteManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity { // for some reason onCreate and onStart (and maybe others) are called twice, keep that in mind when coding. This goes for all activities.

    private ActivityMainBinding binding;
    public static SharedPreferences sharedPreferences;
    NavController navController;
    public static final String USER_ID = "user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup binding for view binding, see https://developer.android.com/topic/libraries/view-binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(sharedPreferences == null)
            sharedPreferences = getSharedPreferences("com.palaref.saequiz", MODE_PRIVATE);

        hideSystemUI();
        setupNav();

        setupTheme();

        checkIfUserExists();

        //this.deleteDatabase("quizDB");
    }

    private void checkIfUserExists(){ // makes sure that the user id in shared preferences is valid
        SQLiteManager sqLiteManager = SQLiteManager.getInstance(this);
        if(sqLiteManager.getUserById(sharedPreferences.getInt(USER_ID, -1)) == null)
            sharedPreferences.edit().putInt(USER_ID, -1).apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void debugDatabse() {
        ArrayList<User> userList = SQLiteManager.getInstance(this).getAllUsers();
        ArrayList<QuizInfo> quizList = SQLiteManager.getInstance(this).getAllQuizInfos();
        Log.d("USER", "onCreate: " + userList.size() + " users loaded from DB" + userList.toString());
        for(User user : userList){
            Log.d("USER", "onCreate: " + user.toString());
        }
        Log.d("QUIZ", "onCreate: " + quizList.size() + " WYRs loaded from DB" + quizList.toString());
        for(QuizInfo quizInfo : quizList){
            Log.d("QUIZ", "onCreate: " + quizInfo.toString());
        }
    }

    private void setupNav(){
        BottomNavigationView navView = binding.navViewMain;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_favorites, R.id.navigation_profile, R.id.navigation_leaderboard, R.id.navigation_search)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    // this hides is done to get a true full screen experience try commenting if you don't get what it does
    // also it's deprecated but I can't find a better way to do it that works
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
    }

    private void setupTheme(){
        //set the night theme by default if user didn't manually switch to light, else use shared preference
        if(sharedPreferences.contains("nightMode")) {
            if (sharedPreferences.getBoolean("nightMode", true)) {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    public void switchNightDay(){
        if (getDelegate().getLocalNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            sharedPreferences.edit().putBoolean("nightMode", false).apply();
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            sharedPreferences.edit().putBoolean("nightMode", true).apply();
        }
    }
}