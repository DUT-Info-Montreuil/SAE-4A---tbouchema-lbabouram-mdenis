package com.palaref.saequiz.model;

import java.sql.Date;
import java.util.Arrays;

public class QuizInfo { // this class is the same as what we have in the database
    private int id;
    private String name;
    private String description;
    private int creatorId;
    private Date creationDate;
    private int numberOfFavorites; // will be obtained from the database by counting the number of rows in the favorites table that have this quiz id
    private String[] tags; // will be obtained from the database by selecting all the tags that have this quiz id

    public QuizInfo(int id, String name, String description, int creatorId, Date creationDate) { // this constructor should only be used when we get the data from the database because the id is already set
        this.id = id;
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.creationDate = creationDate;
        this.numberOfFavorites = numberOfFavorites;
        this.tags = tags;
    }

    public QuizInfo(String name, String description, int creatorId, Date creationDate) { // this constructor should only be used when we create a new quiz
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.creationDate = creationDate;
        this.numberOfFavorites = numberOfFavorites;
        this.tags = tags;
    }

    public void setNumberOfFavorites(int numberOfFavorites) {
        this.numberOfFavorites = numberOfFavorites;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public int getNumberOfFavorites() {
        return numberOfFavorites;
    }

    public String[] getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "QuizInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creatorId=" + creatorId +
                ", creationDate=" + creationDate +
                ", numberOfFavorites=" + numberOfFavorites +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }
}
