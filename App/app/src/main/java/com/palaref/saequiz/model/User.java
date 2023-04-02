package com.palaref.saequiz.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.palaref.saequiz.R;

public class User {
    private int id;
    private String username;
    private String description;
    private Bitmap profilePicture;

    public User(int id, String username, String description, Bitmap profilePicture) { // this constructor should only be used when we get the data from the database because the id is already set
        this.id = id;
        this.username = username;
        this.description = description;
        this.profilePicture = profilePicture;
    }

    public User(String username, String description, Bitmap profilePicture) { // this constructor should only be used when we create a new user
        this.username = username;
        this.description = description;
        this.profilePicture = profilePicture;
    }

    public User(String username, Context context){
        this.username = username;
        this.description = "This is my increadible description !";
        this.profilePicture = BitmapFactory.decodeResource(context.getResources(), R.drawable.defaultpp1);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", description='" + description + '\'' +
                ", profilePicture=" + profilePicture +
                '}';
    }
}
