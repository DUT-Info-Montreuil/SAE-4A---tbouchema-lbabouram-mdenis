package com.palaref.saequiz;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.palaref.saequiz.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setup binding for view binding, see https://developer.android.com/topic/libraries/view-binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("com.palaref.saequiz", MODE_PRIVATE);

        hideSystemUI();
        setupNav();

        setupTheme();
    }

    private void setupNav(){
        BottomNavigationView navView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_favorites, R.id.navigation_profile, R.id.navigation_leaderboard, R.id.navigation_search)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        /*
        navView.setOnItemSelectedListener(item -> {
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int animIdEnter, animIdExit;

                // Get the position of the current and target destinations in the menu
                int currentPosition = navView.getMenu().findItem(destination.getId()).getOrder();
                int targetPosition = navView.getMenu().findItem(item.getItemId()).getOrder();

                // Set the animation based on the relative position of the destinations
                if (currentPosition < targetPosition) {
                    animIdEnter = R.anim.slide_in_right;
                    animIdExit = R.anim.slide_out_left;
                } else {
                    animIdEnter = R.anim.slide_in_left;
                    animIdExit = R.anim.slide_out_right;
                }

                NavOptions navOptions = new NavOptions.Builder()
                        .setEnterAnim(animIdEnter)
                        .setExitAnim(animIdExit)
                        .setPopEnterAnim(animIdEnter)
                        .setPopExitAnim(animIdExit)
                        .build();

                //controller.navigate(destination.getId(), arguments, navOptions);
            });

            return NavigationUI.onNavDestinationSelected(item, navController);
        });
        */
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