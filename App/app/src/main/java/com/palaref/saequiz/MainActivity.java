package com.palaref.saequiz;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;
import com.palaref.saequiz.databinding.ActivityMainBinding;
import com.palaref.saequiz.model.QuizInfo;
import com.palaref.saequiz.model.User;
import com.palaref.saequiz.utils.SQLiteManager;

import java.util.ArrayList;

import quiz.api.connection.ApiClient;
import quiz.api.integration.QuizRequests;
import quiz.api.integration.UserRequests;

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

        //this.deleteDatabase("quizDB");

        debugDatabse();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(SQLiteManager.getInstance(this).getAllUsers().size() == 0)
            SQLiteManager.getInstance(this).addUser(new User("testMan", "La congolexicomatisation des lois du marché propres aux congolais.", Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)));
        else
            Log.d("User", "Database has one or more users : "+ SQLiteManager.getInstance(this).getAllUsers().get(0).getUsername());

        if(SQLiteManager.getInstance(this).getAllQuizInfos().size() == 0){
            //SQLiteManager.getInstance(this).addQuiz(new QuizInfo("test quiz", "Description incroyable", 1, SQLiteManager.getNowDate().toString()));
            //SQLiteManager.getInstance(this).addQuiz(new QuizInfo("test quiz 2", "Description EPIC", 1, SQLiteManager.getNowDate()));
            //SQLiteManager.getInstance(this).addQuiz(new QuizInfo("test quiz 3", "Description wow", 1, SQLiteManager.getNowDate()));
            //SQLiteManager.getInstance(this).addQuiz(new QuizInfo("test quiz 4", "Description cool", 1, SQLiteManager.getNowDate()));
            //SQLiteManager.getInstance(this).addQuiz(new QuizInfo("test quiz 5", "Description pas ouf", 1, SQLiteManager.getNowDate()));
        }
        else
            Log.d("Quiz", "Database has one or more quizzes : " + SQLiteManager.getInstance(this).getAllQuizInfos().toString());

        /*
        SQLiteManager.getInstance(this).addQuiz(new QuizInfo("test quiz", "Description incroyable", 1, SQLiteManager.getNowDate()));
        SQLiteManager.getInstance(this).addQuiz(new QuizInfo("test quiz 2", "Description EPIC", 1, SQLiteManager.getNowDate()));
        SQLiteManager.getInstance(this).addQuiz(new QuizInfo("test quiz 3", "Description wow", 1, SQLiteManager.getNowDate()));
        SQLiteManager.getInstance(this).addQuiz(new QuizInfo("test quiz 4", "Description cool", 1, SQLiteManager.getNowDate()));
        SQLiteManager.getInstance(this).addQuiz(new QuizInfo("test quiz 5", "Description pas ouf", 1, SQLiteManager.getNowDate()));

         */
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
        ApiClient apiCLient = ApiClient.getInstance("http://localhost:5231/");
        QuizRequests quizRequests = new QuizRequests(apiCLient.getHost());
        UserRequests userRequests = new UserRequests(apiCLient.getHost());

        BottomNavigationView navView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_favorites, R.id.navigation_profile, R.id.navigation_leaderboard, R.id.navigation_search)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.getGraph().findNode(R.id.navigation_home)
                .addArgument("quizRequests", new NavArgument.Builder()
                        .setDefaultValue(quizRequests)
                        .build());
        navController.getGraph().findNode(R.id.navigation_home)
                .addArgument("userRequests", new NavArgument.Builder()
                        .setDefaultValue(userRequests)
                        .build());
        navController.getGraph().findNode(R.id.navigation_favorites)
                .addArgument("quizRequests", new NavArgument.Builder()
                        .setDefaultValue(quizRequests)
                        .build());
        navController.getGraph().findNode(R.id.navigation_favorites)
                .addArgument("userRequests", new NavArgument.Builder()
                        .setDefaultValue(userRequests)
                        .build());
        navController.getGraph().findNode(R.id.navigation_profile)
                .addArgument("quizRequests", new NavArgument.Builder()
                        .setDefaultValue(quizRequests)
                        .build());
        navController.getGraph().findNode(R.id.navigation_profile)
                .addArgument("userRequests", new NavArgument.Builder()
                        .setDefaultValue(userRequests)
                        .build());
        navController.getGraph().findNode(R.id.navigation_leaderboard)
                .addArgument("quizRequests", new NavArgument.Builder()
                        .setDefaultValue(quizRequests)
                        .build());
        navController.getGraph().findNode(R.id.navigation_leaderboard)
                .addArgument("userRequests", new NavArgument.Builder()
                        .setDefaultValue(userRequests)
                        .build());
        navController.getGraph().findNode(R.id.navigation_search)
                .addArgument("quizRequests", new NavArgument.Builder()
                        .setDefaultValue(quizRequests)
                        .build());
        navController.getGraph().findNode(R.id.navigation_search)
                .addArgument("userRequests", new NavArgument.Builder()
                        .setDefaultValue(userRequests)
                        .build());
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