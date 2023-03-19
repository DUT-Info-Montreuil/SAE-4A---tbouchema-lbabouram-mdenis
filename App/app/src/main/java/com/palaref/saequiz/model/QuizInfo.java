package com.palaref.saequiz.model;

public class QuizInfo { // this class is the same as what we have in the database
    private int id;
    private String name;
    private String description;
    private int creatorId;
    private String creationDate;
    private String lastUpdateDate;
    private int numberOfFavorites;
    private String[] tags;

    public QuizInfo(int id, String name, String description, int creatorId, String creationDate, String lastUpdateDate, int numberOfFavorites, String[] tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.creationDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
        this.numberOfFavorites = numberOfFavorites;
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

    public String getCreationDate() {
        return creationDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public int getNumberOfFavorites() {
        return numberOfFavorites;
    }

    public String[] getTags() {
        return tags;
    }
}
