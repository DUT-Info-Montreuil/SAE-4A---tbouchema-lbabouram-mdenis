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
    private static final String QUIZINFO_ID = "id";
    private static final String QUIZINFO_NAME = "name";
    private static final String QUIZINFO_DESCRIPTION = "description";
    private static final String QUIZINFO_CREATOR_ID = "creator_id";
    private static final String QUIZINFO_CREATION_DATE = "creation_date";

    private static final String USERS_TABLE = "users";
    private static final String USERS_ID = "id";
    private static final String USERS_USERNAME = "username";
    private static final String USERS_DESCRIPTION = "description";
    private static final String USERS_PICTURE = "picture";
    private static final String USERS_ISADMIN = "isadmin"; // This is only temporary because it makes no sense to store this in the local database

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

    private static final String FAVORITES_TABLE = "favorites";
    private static final String FAVORITES_ID = "id";
    private static final String FAVORITES_USER_ID = "user_id";
    private static final String FAVORITES_QUIZINFO_ID = "quizinfo_id";

    private static final String BESTSCORES_TABLE = "bestscores";
    private static final String BESTSCORES_ID = "id";
    private static final String BESTSCORES_USER_ID = "user_id";
    private static final String BESTSCORES_QUIZINFO_ID = "quizinfo_id";
    private static final String BESTSCORES_SCORE = "score";
    private static final String BESTSCORES_DATE = "date";

    private static final String COMPLETED_TABLE = "completed";
    private static final String COMPLETED_ID = "id";
    private static final String COMPLETED_USER_ID = "user_id";
    private static final String COMPLETED_QUIZINFO_ID = "quizinfo_id";
    private static final String COMPLETED_DATE = "date";

    private static final String MONTHLYQUIZ_TABLE = "monthlyquiz";
    private static final String MONTHLYQUIZ_ID = "id";
    private static final String MONTHLYQUIZ_QUIZINFO_ID = "quizinfo_id";
    private static final String MONTHLYQUIZ_DATE = "date";


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
                .append(USERS_PICTURE).append(" BLOB, ")
                .append(USERS_ISADMIN).append(" INT);");
        db.execSQL(sql.toString());

        sql = new StringBuilder().append("CREATE TABLE ").append(QUIZINFO_TABLE).append(" (")
                .append(QUIZINFO_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(QUIZINFO_NAME).append(" TEXT, ")
                .append(QUIZINFO_DESCRIPTION).append(" TEXT, ")
                .append(QUIZINFO_CREATOR_ID).append(" INT, ")
                .append(QUIZINFO_CREATION_DATE).append(" TEXT, ")
                .append("FOREIGN KEY(").append(QUIZINFO_CREATOR_ID).append(") REFERENCES ")
                .append(USERS_TABLE).append("(").append(QUIZINFO_CREATOR_ID).append("));");
        db.execSQL(sql.toString());

        sql = new StringBuilder().append("CREATE TABLE ").append(QUIZGAME_TABLE).append(" (")
                .append(QUIZGAME_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(QUIZGAME_QUIZINFO_ID).append(" INT, ")
                .append("FOREIGN KEY(").append(QUIZGAME_QUIZINFO_ID).append(") REFERENCES ")
                .append(QUIZINFO_TABLE).append("(").append(QUIZINFO_ID).append("));");
        db.execSQL(sql.toString());

        sql = new StringBuilder().append("CREATE TABLE ").append(QUESTION_TABLE).append(" (")
                .append(QUESTION_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(QUESTION_QUIZGAME_ID).append(" INT, ")
                .append(QUESTION_TEXT).append(" TEXT, ")
                .append(QUESTION_NUMBER).append(" INT, ")
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

        sql = new StringBuilder().append("CREATE TABLE ").append(FAVORITES_TABLE).append(" (")
                .append(FAVORITES_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(FAVORITES_USER_ID).append(" INT, ")
                .append(FAVORITES_QUIZINFO_ID).append(" INT, ")
                .append("FOREIGN KEY(").append(FAVORITES_USER_ID).append(") REFERENCES ")
                .append(USERS_TABLE).append("(").append(USERS_ID).append("), ")
                .append("FOREIGN KEY(").append(FAVORITES_QUIZINFO_ID).append(") REFERENCES ")
                .append(QUIZINFO_TABLE).append("(").append(QUIZINFO_ID).append("));");
        db.execSQL(sql.toString());

        sql = new StringBuilder().append("CREATE TABLE ").append(BESTSCORES_TABLE).append(" (")
                .append(BESTSCORES_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(BESTSCORES_USER_ID).append(" INT, ")
                .append(BESTSCORES_QUIZINFO_ID).append(" INT, ")
                .append(BESTSCORES_SCORE).append(" INT, ")
                .append(BESTSCORES_DATE).append(" TEXT, ")
                .append("FOREIGN KEY(").append(BESTSCORES_USER_ID).append(") REFERENCES ")
                .append(USERS_TABLE).append("(").append(USERS_ID).append("), ")
                .append("FOREIGN KEY(").append(BESTSCORES_QUIZINFO_ID).append(") REFERENCES ")
                .append(QUIZINFO_TABLE).append("(").append(QUIZINFO_ID).append("));");
        db.execSQL(sql.toString());

        sql = new StringBuilder().append("CREATE TABLE ").append(COMPLETED_TABLE).append(" (")
                .append(COMPLETED_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(COMPLETED_USER_ID).append(" INT, ")
                .append(COMPLETED_QUIZINFO_ID).append(" INT, ")
                .append(COMPLETED_DATE).append(" TEXT, ")
                .append("FOREIGN KEY(").append(COMPLETED_USER_ID).append(") REFERENCES ")
                .append(USERS_TABLE).append("(").append(USERS_ID).append("), ")
                .append("FOREIGN KEY(").append(COMPLETED_QUIZINFO_ID).append(") REFERENCES ")
                .append(QUIZINFO_TABLE).append("(").append(QUIZINFO_ID).append("));");
        db.execSQL(sql.toString());

        sql = new StringBuilder().append("CREATE TABLE ").append(MONTHLYQUIZ_TABLE).append(" (")
                .append(MONTHLYQUIZ_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(MONTHLYQUIZ_QUIZINFO_ID).append(" INT, ")
                .append(MONTHLYQUIZ_DATE).append(" TEXT, ")
                .append("FOREIGN KEY(").append(MONTHLYQUIZ_QUIZINFO_ID).append(") REFERENCES ")
                .append(QUIZINFO_TABLE).append("(").append(QUIZINFO_ID).append("));");
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
        if(user.getUsername().equals("Tarook")){
            values.put(USERS_ISADMIN, 1);
        }
        else {
            values.put(USERS_ISADMIN, 0);
        }

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
        values.put(QUIZINFO_NAME, quiz.getName());
        values.put(QUIZINFO_DESCRIPTION, quiz.getDescription());
        values.put(QUIZINFO_CREATOR_ID, quiz.getCreatorId());
        values.put(QUIZINFO_CREATION_DATE, quiz.getCreationDate().toString());

        long id = db.insert(QUIZINFO_TABLE, null, values);
        quiz.setId((int) id);
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
        try(Cursor result = db.rawQuery("SELECT * FROM " + QUIZINFO_TABLE + " WHERE " + QUIZINFO_ID + " = " + id, null)){
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

    public QuizInfo getQuizByName(String name){
        SQLiteDatabase db = getReadableDatabase();
        QuizInfo quiz = null;
        try(Cursor result = db.rawQuery("SELECT * FROM " + QUIZINFO_TABLE + " WHERE " + QUIZINFO_NAME + "=?", new String[]{name})){
            if(result.moveToFirst()){
                int id = result.getInt(0);
                String name1 = result.getString(1);
                String description = result.getString(2);
                int creatorId = result.getInt(3);
                Date creationDate = convertStringToDate(result.getString(4));
                quiz = new QuizInfo(id, name1, description, creatorId, creationDate);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return quiz;
    }

    public void updateQuizInfo(QuizInfo quizInfo){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUIZINFO_ID, quizInfo.getId());
        values.put(QUIZINFO_NAME, quizInfo.getName());
        values.put(QUIZINFO_DESCRIPTION, quizInfo.getDescription());
        values.put(QUIZINFO_CREATOR_ID, quizInfo.getCreatorId());
        values.put(QUIZINFO_CREATION_DATE, quizInfo.getCreationDate().toString());

        db.update(QUIZINFO_TABLE, values, QUIZINFO_ID + " = ?", new String[]{String.valueOf(quizInfo.getId())});
    }

    public void addQuizGame(QuizGame quizGame){ // this should create a quizGame tuple and it's questions and answers
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUIZGAME_QUIZINFO_ID, quizGame.getQuizInfoId());

        long id = db.insert(QUIZGAME_TABLE, null, values);
        // quizgame has an id of 0 because it's not set yet by the database so we need to get it from the database
        quizGame.setQuizId((int) id);
        for(int i = 0; i < quizGame.getQuestions().size(); i++){
            addQuestion(quizGame.getQuestions().get(i), ((int) id), i);
        }
    }

    public void addQuestion(QuizQuestion question, int quizGameId, int questionNumber){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUESTION_TEXT, question.getQuestion());
        values.put(QUESTION_QUIZGAME_ID, quizGameId);
        values.put(QUESTION_NUMBER, questionNumber);

        long id = db.insert(QUESTION_TABLE, null, values);
        question.setQuizQuestionId((int) id);
        for(int i = 0; i < question.getAnswers().size(); i++){
            addAnswer(question.getAnswers().get(i), question.getQuizQuestionId());
        }
    }

    public void addAnswer(QuizAnswer answer, int questionId){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ANSWER_TEXT, answer.getAnswer());
        values.put(ANSWER_IS_CORRECT, answer.isCorrect());
        values.put(ANSWER_QUESTION_ID, questionId);
        values.put(ANSWER_NUMBER, answer.getAnswerNumber());

        long id = db.insert(ANSWER_TABLE, null, values);
        answer.setQuizAnswerId((int) id);
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

    public void addFavoriteForUser(int userId, int quizInfoId){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FAVORITES_USER_ID, userId);
        values.put(FAVORITES_QUIZINFO_ID, quizInfoId);

        db.insert(FAVORITES_TABLE, null, values);
    }

    public void removeFavoriteForUser(int userId, int quizInfoId){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(FAVORITES_TABLE, FAVORITES_USER_ID + " = ? AND " + FAVORITES_QUIZINFO_ID + " = ?", new String[]{String.valueOf(userId), String.valueOf(quizInfoId)});
    }

    public ArrayList<QuizInfo> getAllFavoritesOfUser(int userId){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<QuizInfo> quizInfos = new ArrayList<>();
        try(Cursor result = db.rawQuery("SELECT * FROM " + QUIZINFO_TABLE + " WHERE " + QUIZINFO_ID + " IN (SELECT " + FAVORITES_QUIZINFO_ID + " FROM " + FAVORITES_TABLE + " WHERE " + FAVORITES_USER_ID + " = " + userId + ")", null)){
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    int id = result.getInt(0);
                    String name = result.getString(1);
                    String description = result.getString(2);
                    int creatorId = result.getInt(3);
                    Date creationDate = convertStringToDate(result.getString(4));
                    quizInfos.add(new QuizInfo(id, name, description, creatorId, creationDate));
                }
            }
        }
        return quizInfos;
    }

    public ArrayList<QuizInfo> getAllQuizzesOfUser(int userId){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<QuizInfo> quizInfos = new ArrayList<>();
        try(Cursor result = db.rawQuery("SELECT * FROM " + QUIZINFO_TABLE + " WHERE " + QUIZINFO_CREATOR_ID + " = " + userId, null)){
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    int id = result.getInt(0);
                    String name = result.getString(1);
                    String description = result.getString(2);
                    int creatorId = result.getInt(3);
                    Date creationDate = convertStringToDate(result.getString(4));
                    quizInfos.add(new QuizInfo(id, name, description, creatorId, creationDate));
                }
            }
        }
        return quizInfos;
    }

    public boolean isQuizFavorite(int userId, int quizInfoId){
        SQLiteDatabase db = getReadableDatabase();
        try(Cursor result = db.rawQuery("SELECT * FROM " + FAVORITES_TABLE + " WHERE " + FAVORITES_USER_ID + " = " + userId + " AND " + FAVORITES_QUIZINFO_ID + " = " + quizInfoId, null)){
            return result.getCount() != 0;
        }catch (Exception e){
            return false;
        }
    }

    public void addBestScoreForUser(int userId, int quizInfoId, int score){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BESTSCORES_USER_ID, userId);
        values.put(BESTSCORES_QUIZINFO_ID, quizInfoId);
        values.put(BESTSCORES_SCORE, score);
        values.put(BESTSCORES_DATE, getNowDate().toString());

        db.insert(BESTSCORES_TABLE, null, values);
    }

    public void updateUserBestScore(int userId, int quizInfoId, int score){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BESTSCORES_SCORE, score);
        values.put(BESTSCORES_DATE, getNowDate().toString());

        db.update(BESTSCORES_TABLE, values, BESTSCORES_USER_ID + " = ? AND " + BESTSCORES_QUIZINFO_ID + " = ?", new String[]{String.valueOf(userId), String.valueOf(quizInfoId)});
    }

    public int getUserBestScore(int userId, int quizInfoId){
        SQLiteDatabase db = getReadableDatabase();
        try(Cursor result = db.rawQuery("SELECT * FROM " + BESTSCORES_TABLE + " WHERE " + BESTSCORES_USER_ID + " = " + userId + " AND " + BESTSCORES_QUIZINFO_ID + " = " + quizInfoId, null)){
            if(result.moveToFirst()){
                return result.getInt(3);
            }
        }catch (Exception e){
            return -1;
        }
        return -1;
    }

    public void addCompletedQuizForUser(int userId, int quizInfoId){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COMPLETED_USER_ID, userId);
        values.put(COMPLETED_QUIZINFO_ID, quizInfoId);
        values.put(COMPLETED_DATE, getNowDate().toString());

        db.insert(COMPLETED_TABLE, null, values);
    }

    public ArrayList<User> get10MostCompletionUsersOfMonths(Date date){ // returns the 10 users with the most completed quizzes in a month. Date as string is formatted as yyyy-MM-dd
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<User> users = new ArrayList<>();
        try(Cursor result = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + USERS_ID + " IN (SELECT " + COMPLETED_USER_ID + " FROM " + COMPLETED_TABLE + " WHERE " + COMPLETED_DATE + " LIKE '" + date.toString().substring(0, 7) + "%' GROUP BY " + COMPLETED_USER_ID + " ORDER BY COUNT(" + COMPLETED_USER_ID + ") DESC LIMIT 10)", null)){
            if(result.getCount() != 0){
                while(result.moveToNext()){
                    int id = result.getInt(0);
                    String username = result.getString(1);
                    String description = result.getString(2);
                    Bitmap profilePicture = getBitmapFromByteArray(result.getBlob(3));
                    users.add(new User(id, username, description, profilePicture));
                }
            }
        }catch (Exception e){
            return null;
        }
        return users;
    }

    public int getCountOfUserCompleted(int userID){
        SQLiteDatabase db = getReadableDatabase();
        try(Cursor result = db.rawQuery("SELECT * FROM " + COMPLETED_TABLE + " WHERE " + COMPLETED_USER_ID + " = " + userID, null)){
            return result.getCount();
        }catch (Exception e){
            return -1;
        }
    }

    public boolean isQuizCompleted(int userId, int quizInfoId){
        SQLiteDatabase db = getReadableDatabase();
        try(Cursor result = db.rawQuery("SELECT * FROM " + COMPLETED_TABLE + " WHERE " + COMPLETED_USER_ID + " = " + userId + " AND " + COMPLETED_QUIZINFO_ID + " = " + quizInfoId, null)){
            return result.getCount() != 0;
        }catch (Exception e){
            return false;
        }
    }

    public void addMonthlyQuiz(int quizInfoId){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MONTHLYQUIZ_QUIZINFO_ID, quizInfoId);
        values.put(MONTHLYQUIZ_DATE, getNowDate().toString());

        db.insert(MONTHLYQUIZ_TABLE, null, values);
    }

    public void updateMonthlyQuiz(int quizInfoId){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MONTHLYQUIZ_QUIZINFO_ID, quizInfoId);
        values.put(MONTHLYQUIZ_DATE, getNowDate().toString());

        db.update(MONTHLYQUIZ_TABLE, values, MONTHLYQUIZ_ID + " = ?", new String[]{String.valueOf(1)});
    }

    public QuizInfo getMonthlyQuiz(){
        SQLiteDatabase db = getReadableDatabase();
        try(Cursor result = db.rawQuery("SELECT * FROM " + MONTHLYQUIZ_TABLE + " WHERE " + MONTHLYQUIZ_ID + " = 1", null)){
            if(result.moveToFirst()){
                int quizInfoId = result.getInt(1);
                return getQuizInfoById(quizInfoId);
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    public java.sql.Date getMonthlyQuizDate(){
        SQLiteDatabase db = getReadableDatabase();
        try(Cursor result = db.rawQuery("SELECT * FROM " + MONTHLYQUIZ_TABLE + " WHERE " + MONTHLYQUIZ_ID + " = 1", null)){
            if(result.moveToFirst()){
                String date = result.getString(2);
                return java.sql.Date.valueOf(date);
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    public boolean isAdmin(int userId){ // checks if the isadmin column is true in the users table
        SQLiteDatabase db = getReadableDatabase();
        try(Cursor result = db.rawQuery("SELECT * FROM " + USERS_TABLE + " WHERE " + USERS_ID + " = " + userId + " AND " + USERS_ISADMIN + " = 1", null)){
            return result.getCount() != 0;
        }catch (Exception e){
            return false;
        }
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