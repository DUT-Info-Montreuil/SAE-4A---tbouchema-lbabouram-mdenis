package com.palaref.saequiz.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.palaref.saequiz.model.QuizAnswer;
import com.palaref.saequiz.model.QuizGame;
import com.palaref.saequiz.model.QuizInfo;
import com.palaref.saequiz.model.QuizQuestion;
import com.palaref.saequiz.model.User;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class SQLiteManager extends SQLiteOpenHelper { // currently uses profiles which are from another project, but I will change them for quizzes

    private static SQLiteManager instance;
    private static final String DB_NAME = "quizDB";
    private static final int DB_VERSION = 1;

    private static final String QUIZINFO_TABLE = "quizinfos";
    private static final String ID_FIELD = "id";
    private static final String NAME_FIELD = "name";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String CREATOR_ID_FIELD = "creator_id";
    private static final String CREATION_DATE_FIELD = "creation_date";

    private static final String USERS_TABLE = "users";
    private static final String USERS_ID = "id";
    private static final String USERS_USERNAME = "username";
    private static final String USERS_DESCRIPTION = "description";
    private static final String USERS_PICTURE = "picture";

    private static final String QUIZGAME_TABLE = "quizgames";
    private static final String QUIZGAME_ID = "id"; // used to find questions and answers
    private static final String QUIZGAME_QUIZINFO_ID = "quizinfo_id";

    private static final String QUESTION_TABLE = "questions";
    private static final String QUESTION_ID = "id";
    private static final String QUESTION_QUIZGAME_ID = "quizgame_id";
    private static final String QUESTION_TEXT = "text";
    private static final String QUESTION_NUMBER = "number";

    private static final String ANSWER_TABLE = "answers";
    private static final String ANSWER_ID = "id";
    private static final String ANSWER_QUESTION_ID = "question_id";
    private static final String ANSWER_TEXT = "text";
    private static final String ANSWER_IS_CORRECT = "is_correct";
    private static final String ANSWER_NUMBER = "number";

    private SQLiteManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized SQLiteManager getInstance(Context context) {
        if(instance == null)
            instance = new SQLiteManager(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql;
        sql = new StringBuilder().append("CREATE TABLE ").append(USERS_TABLE).append(" (")
                .append(USERS_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(USERS_USERNAME).append(" TEXT, ")
                .append(USERS_DESCRIPTION).append(" TEXT, ")
                .append(USERS_PICTURE).append(" BLOB);");
        db.execSQL(sql.toString());

        sql = new StringBuilder().append("CREATE TABLE ").append(QUIZINFO_TABLE).append(" (")
                .append(ID_FIELD).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(NAME_FIELD).append(" TEXT, ")
                .append(DESCRIPTION_FIELD).append(" TEXT, ")
                .append(CREATOR_ID_FIELD).append(" INT, ")
                .append(CREATION_DATE_FIELD).append(" TEXT, ")
                .append("FOREIGN KEY(").append(CREATOR_ID_FIELD).append(") REFERENCES ")
                .append(USERS_TABLE).append("(").append(CREATOR_ID_FIELD).append("));");
        db.execSQL(sql.toString());

        sql = new StringBuilder().append("CREATE TABLE ").append(QUIZGAME_TABLE).append(" (")
                .append(QUIZGAME_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(QUIZGAME_QUIZINFO_ID).append(" INT, ")
                .append("FOREIGN KEY(").append(QUIZGAME_QUIZINFO_ID).append(") REFERENCES ")
                .append(QUIZINFO_TABLE).append("(").append(ID_FIELD).append("));");
        db.execSQL(sql.toString());

        sql = new StringBuilder().append("CREATE TABLE ").append(QUESTION_TABLE).append(" (")
                .append(QUESTION_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(QUESTION_QUIZGAME_ID).append(" INT, ")
                .append(QUESTION_TEXT).append(" TEXT, ")
                .append("FOREIGN KEY(").append(QUESTION_QUIZGAME_ID).append(") REFERENCES ")
                .append(QUIZGAME_TABLE).append("(").append(QUIZGAME_ID).append("));");
        db.execSQL(sql.toString());

        sql = new StringBuilder().append("CREATE TABLE ").append(ANSWER_TABLE).append(" (")
                .append(ANSWER_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ANSWER_QUESTION_ID).append(" INT, ")
                .append(ANSWER_TEXT).append(" TEXT, ")
                .append(ANSWER_IS_CORRECT).append(" INT, ")
                .append(ANSWER_NUMBER).append(" INT, ")
                .append("FOREIGN KEY(").append(ANSWER_QUESTION_ID).append(") REFERENCES ")
                .append(QUESTION_TABLE).append("(").append(QUESTION_ID).append("));");
        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void addUser(User user){
        Log.d("ADD_USER", "Adding user " + user.getUsername());
        SQLiteDatabase db = getWritableDatabase();

        if (getUserByUsername(user.getUsername()) != null) {
            Log.d("ADD_USER", "User " + user.getUsername() + " already exists in database");
            return;
        }

        ContentValues values = new ContentValues();
        values.put(USERS_USERNAME, user.getUsername());
        values.put(USERS_DESCRIPTION, user.getDescription());
        values.put(USERS_PICTURE, getByteArrayFromBitmap(user.getProfilePicture()));

        db.insert(USERS_TABLE, null, values);
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor result = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + USERS_USERNAME + "=?", new String[]{username})) {
            if (result.moveToFirst()) {
                @SuppressLint("Range") int id = result.getInt(result.getColumnIndex(USERS_ID));
                @SuppressLint("Range") String description = result.getString(result.getColumnIndex(USERS_DESCRIPTION));
                @SuppressLint("Range") Bitmap profilePicture = getBitmapFromByteArray(result.getBlob(result.getColumnIndex(USERS_PICTURE)));
                return new User(id, username, description, profilePicture);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<User> getAllUsers(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<User> users = new ArrayList<>();
        try(Cursor result = db.rawQuery("SELECT * FROM " + USERS_TABLE, null)){
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    @SuppressLint("Range") int id = result.getInt(result.getColumnIndex(USERS_ID));
                    @SuppressLint("Range") String username = result.getString(result.getColumnIndex(USERS_USERNAME));
                    @SuppressLint("Range") String description = result.getString(result.getColumnIndex(USERS_DESCRIPTION));
                    @SuppressLint("Range") Bitmap profilePicture = getBitmapFromByteArray(result.getBlob(result.getColumnIndex(USERS_PICTURE)));
                    users.add(new User(id, username, description, profilePicture));
                }
            }
        }
        return users;
    }

    public User getUserById(int id){
        SQLiteDatabase db = getReadableDatabase();
        User user = null;
        try(Cursor result = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + USERS_ID + " = " + id, null)){
            if(result.moveToFirst()){
                String username = result.getString(1);
                String description = result.getString(2);
                Bitmap profilePicture = getBitmapFromByteArray(result.getBlob(3));
                user = new User(id, username, description, profilePicture);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public void addQuiz(QuizInfo quiz){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME_FIELD, quiz.getName());
        values.put(DESCRIPTION_FIELD, quiz.getDescription());
        values.put(CREATOR_ID_FIELD, quiz.getCreatorId());
        values.put(CREATION_DATE_FIELD, quiz.getCreationDate().toString());

        db.insert(QUIZINFO_TABLE, null, values);
    }

    public ArrayList<QuizInfo> getAllQuizInfos(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<QuizInfo> quizzes = new ArrayList<>();
        try(Cursor result = db.rawQuery("SELECT * FROM " + QUIZINFO_TABLE, null)){
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    int id = result.getInt(0);
                    String name = result.getString(1);
                    String description = result.getString(2);
                    int creatorId = result.getInt(3);
                    Date creationDate = convertStringToDate(result.getString(4));
                    quizzes.add(new QuizInfo(id, name, description, creatorId, creationDate));
                }
            }
        }
        return quizzes;
    }

    public QuizInfo getQuizInfoById(int id){
        SQLiteDatabase db = getReadableDatabase();
        QuizInfo quiz = null;
        try(Cursor result = db.rawQuery("SELECT * FROM " + QUIZINFO_TABLE + " WHERE " + ID_FIELD + " = " + id, null)){
            if(result.moveToFirst()){
                String name = result.getString(1);
                String description = result.getString(2);
                int creatorId = result.getInt(3);
                Date creationDate = convertStringToDate(result.getString(4));
                quiz = new QuizInfo(id, name, description, creatorId, creationDate);
            }
        }
        return quiz;
    }

    public void updateQuizInfo(QuizInfo quizInfo){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID_FIELD, quizInfo.getId());
        values.put(NAME_FIELD, quizInfo.getName());
        values.put(DESCRIPTION_FIELD, quizInfo.getDescription());
        values.put(CREATOR_ID_FIELD, quizInfo.getCreatorId());
        values.put(CREATION_DATE_FIELD, quizInfo.getCreationDate().toString());

        db.update(QUIZINFO_TABLE, values, ID_FIELD + " = ?", new String[]{String.valueOf(quizInfo.getId())});
    }

    public void addQuizGame(QuizGame quizGame){ // this should create a quizGame tuple and it's questions and answers
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUIZGAME_QUIZINFO_ID, quizGame.getQuizInfoId());

        db.insert(QUIZGAME_TABLE, null, values);
        for(int i = 0; i < quizGame.getQuestions().size(); i++){
            addQuestion(quizGame.getQuestions().get(i), quizGame.getQuizId(), i);
        }
    }

    public void addQuestion(QuizQuestion question, int quizGameId, int questionNumber){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUESTION_TEXT, question.getQuestion());
        values.put(QUESTION_QUIZGAME_ID, quizGameId);
        values.put(QUESTION_NUMBER, questionNumber);

        db.insert(QUESTION_TABLE, null, values);

        for(int i = 0; i < question.getAnswers().size(); i++){
            addAnswer(question.getAnswers().get(i), question.getQuizQuestionId(), i);
        }
    }

    public void addAnswer(QuizAnswer answer, int questionId, int answerNumber){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ANSWER_TEXT, answer.getAnswer());
        values.put(ANSWER_IS_CORRECT, answer.isCorrect());
        values.put(ANSWER_QUESTION_ID, questionId);
        values.put(ANSWER_NUMBER, answerNumber);

        db.insert(ANSWER_TABLE, null, values);
    }

    public ArrayList<QuizAnswer> getQuizAnswers(int questionId){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<QuizAnswer> answers = new ArrayList<>();
        try(Cursor result = db.rawQuery("SELECT * FROM " + ANSWER_TABLE + " WHERE " + ANSWER_QUESTION_ID + " = " + questionId + " ORDER BY " + ANSWER_NUMBER + " ASC", null)){
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    int id = result.getInt(0);
                    int questionId2 = result.getInt(1);
                    String answer = result.getString(2);
                    boolean isCorrect = result.getInt(3) == 1;
                    int answerNumber = result.getInt(4);
                    answers.add(new QuizAnswer(id, questionId2, answer, isCorrect, answerNumber));
                }
            }
        }
        return answers;
    }

    public ArrayList<QuizQuestion> getQuizQuestions(int quizGameId){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<QuizQuestion> questions = new ArrayList<>();
        try(Cursor result = db.rawQuery("SELECT * FROM " + QUESTION_TABLE + " WHERE " + QUESTION_QUIZGAME_ID + " = " + quizGameId + " ORDER BY " + QUESTION_NUMBER + " ASC", null)){
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    int id = result.getInt(0);
                    int quizGameId2 = result.getInt(1);
                    String question = result.getString(2);
                    int questionNumber = result.getInt(3);
                    questions.add(new QuizQuestion(id, quizGameId2, question, getQuizAnswers(id), questionNumber));
                }
            }
        }
        return questions;
    }

    public QuizGame getQuizGameByQuizInfoId(int quizInfoId){
        SQLiteDatabase db = getReadableDatabase();
        QuizGame quizGame = null;
        try(Cursor result = db.rawQuery("SELECT * FROM " + QUIZGAME_TABLE + " WHERE " + QUIZGAME_QUIZINFO_ID + " = " + quizInfoId, null)){
            if(result.moveToFirst()){
                int id = result.getInt(0);
                int quizInfoId2 = result.getInt(1);
                quizGame = new QuizGame(id, quizInfoId2, getQuizQuestions(id));
            }
        }
        return quizGame;
    }

    private Bitmap getBitmapFromByteArray(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    private byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public static Date getDateOf(int day, int month, int year){
        LocalDate localDate = LocalDate.of(2023, 3, 19);
        Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return new Date(instant.toEpochMilli());
    }

    public java.sql.Date convertStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        try {
            java.util.Date date = formatter.parse(dateString);
            assert date != null;
            return new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getNowDate(){
        return new Date(System.currentTimeMillis());
    }
}