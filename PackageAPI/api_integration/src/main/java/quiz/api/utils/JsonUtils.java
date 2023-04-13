package quiz.api.utils;

import jakarta.json.*;
import quiz.api.dto.*;
import quiz.api.integration.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JsonUtils {
    /**
     * Parse a json string to a UserAPI ArrayList
     * @param jsonString
     * @param userRequests
     * @return ArrayList<UserAPI>
     * @throws FileNotFoundException
     */
    public static ArrayList<UserAPI> parseJsonToUserList(String jsonString) throws FileNotFoundException {
        ArrayList<UserAPI> userAPIS = new ArrayList<>();

        JsonReader reader = Json.createReader(new FileInputStream(jsonString));
        JsonArray jsonArray = reader.readArray();
        reader.close();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.getJsonObject(i);
            String id = jsonObject.getString("id");
            String pseudo = jsonObject.getString("pseudo");
            String email = jsonObject.getString("email");
            int score = jsonObject.getInt("score");

            ArrayList<String> favQuizzes = new ArrayList<>();
            JsonArray favQuizzesArray = jsonObject.getJsonArray("favQuizzes");
            for (int j = 0; j < favQuizzesArray.size(); j++) {
                favQuizzes.add(favQuizzesArray.getString(j));
            }

            ArrayList<String> createdQuizzes = new ArrayList<>();
            JsonArray createdQuizzesArray = jsonObject.getJsonArray("createdQuiz");
            for (int j = 0; j < createdQuizzesArray.size(); j++) {
                createdQuizzes.add(createdQuizzesArray.getString(j));
            }

            ArrayList<String> quizzesPlayed = new ArrayList<>();
            JsonArray quizzesPlayedArray = jsonObject.getJsonArray("playedQuiz");
            for (int j = 0; j < quizzesPlayedArray.size(); j++) {
                quizzesPlayed.add(quizzesPlayedArray.getString(j));
            }

            File profilePicture = UserRequests.GetUserProfilePicture(jsonObject.getString("id"));
            UserAPI userAPI = new UserAPI(id, pseudo, email, score, favQuizzes, createdQuizzes, quizzesPlayed, profilePicture);
            userAPIS.add(userAPI);
        }

        return userAPIS;
    }

    /**
     * Parse a json string to a QuizGame ArrayList
     * @param json
     * @return QuizGame
     */
    public static ArrayList<QuizInfoAPI> parseJsonToQuizInfoList(String json) {
        ArrayList<QuizInfoAPI> quizInfoAPIList = new ArrayList<>();
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonArray jsonArray = jsonReader.readArray();

        for (JsonValue value : jsonArray) {
            JsonObject jsonObject = (JsonObject) value;
            String id = jsonObject.getString("id");
            String quizName = jsonObject.getString("quizName");
            String quizDescription = jsonObject.getString("quizDescription");
            String quizCreatorId = jsonObject.getString("quizCreatorId");
            String quizCreationDate = jsonObject.getString("quizCreationDate");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(quizCreationDate, new java.text.ParsePosition(0));
            String quizCreationDateFormatted = dateFormat.format(date);


            JsonArray quizTagsJsonArray = jsonObject.getJsonArray("quiztags");
            ArrayList<String> quizTags = new ArrayList<>();
            for (JsonValue quizTagJsonValue : quizTagsJsonArray) {
                String quizTag = quizTagJsonValue.toString().replaceAll("\"", "");
                quizTags.add(quizTag);
            }

            JsonObject quizQuestionnaryJsonObject = jsonObject.getJsonObject("quizQuestionnary");
            JsonArray quizQuestionsJsonArray = quizQuestionnaryJsonObject.getJsonArray("questions");
            ArrayList<QuizQuestion> quizQuestions = new ArrayList<>();
            for (JsonValue quizQuestionJsonValue : quizQuestionsJsonArray) {
                JsonObject quizQuestionJsonObject = (JsonObject) quizQuestionJsonValue;
                String question = quizQuestionJsonObject.getString("question");

                JsonArray quizAnswersJsonArray = quizQuestionJsonObject.getJsonArray("answers");
                ArrayList<QuizAnswer> quizAnswers = new ArrayList<>();
                for (JsonValue quizAnswerJsonValue : quizAnswersJsonArray) {
                    JsonObject quizAnswerJsonObject = (JsonObject) quizAnswerJsonValue;
                    String answer = quizAnswerJsonObject.getString("answer");
                    boolean isCorrect = quizAnswerJsonObject.getBoolean("isCorrect");
                    QuizAnswer quizAnswer = new QuizAnswer(answer, isCorrect);
                    quizAnswers.add(quizAnswer);
                }
                QuizQuestion quizQuestion = new QuizQuestion(question, quizAnswers);
                quizQuestions.add(quizQuestion);
            }
            quizInfoAPIList.add(new QuizInfoAPI(id, quizName, quizDescription, quizCreatorId, quizTags, new QuizGame(quizQuestions), quizCreationDateFormatted));
        }
        jsonReader.close();
        return quizInfoAPIList;
    }

    /**
     * Parse a json string to a unique QuizGame
     * @param jsonString
     * @return QuizGame
     */
    public static QuizInfoAPI parseJsonToQuizInfo(String jsonString) {
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject quizObject = reader.readObject();

        String id = quizObject.getString("id");
        String quizName = quizObject.getString("quizName");
        String quizDescription = quizObject.getString("quizDescription");
        String quizCreatorId = quizObject.getString("quizCreatorId");
        String quizCreationDate = quizObject.getString("quizCreationDate");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse(quizCreationDate, new java.text.ParsePosition(0));
        String quizCreationDateFormatted = dateFormat.format(date);

        ArrayList<String> quizTags = new ArrayList<String>();
        JsonArray tagsArray = quizObject.getJsonArray("quiztags");
        for (int i = 0; i < tagsArray.size(); i++) {
            quizTags.add(tagsArray.getString(i));
        }

        JsonArray questionsArray = quizObject.getJsonObject("quizQuestionnary").getJsonArray("questions");
        ArrayList<QuizQuestion> questions = new ArrayList<QuizQuestion>();
        for (int i = 0; i < questionsArray.size(); i++) {
            JsonObject questionObject = questionsArray.getJsonObject(i);
            String question = questionObject.getString("question");

            JsonArray answersArray = questionObject.getJsonArray("answers");
            ArrayList<QuizAnswer> answers = new ArrayList<QuizAnswer>();
            for (int j = 0; j < answersArray.size(); j++) {
                JsonObject answerObject = answersArray.getJsonObject(j);
                String answer = answerObject.getString("answer");
                boolean isCorrect = answerObject.getBoolean("isCorrect");
                QuizAnswer quizAnswer = new QuizAnswer(answer, isCorrect);
                answers.add(quizAnswer);
            }

            QuizQuestion quizQuestion = new QuizQuestion(question, answers);
            questions.add(quizQuestion);
        }

        return new QuizInfoAPI(id,quizName, quizDescription, quizCreatorId, quizTags, new QuizGame(questions), quizCreationDateFormatted);
    }

    /**
     * Parse a json string to a unique UserAPI
     * @param json
     * @param userRequests
     * @return UserAPI
     * @throws FileNotFoundException
     */
    public static UserAPI parseJsonToUserProfile(String json) throws FileNotFoundException {
        JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
        String id = jsonObject.getString("id");
        String pseudo = jsonObject.getString("pseudo");
        String email = jsonObject.getString("email");
        int score = jsonObject.getInt("score");

        ArrayList<String> favQuizzes = new ArrayList<>();
        JsonArray favQuizzesArray = jsonObject.getJsonArray("favQuizzes");
        for (int j = 0; j < favQuizzesArray.size(); j++) {
            favQuizzes.add(favQuizzesArray.getString(j));
        }

        ArrayList<String> createdQuizzes = new ArrayList<>();
        JsonArray createdQuizzesArray = jsonObject.getJsonArray("createdQuiz");
        for (int j = 0; j < createdQuizzesArray.size(); j++) {
            createdQuizzes.add(createdQuizzesArray.getString(j));
        }

        ArrayList<String> quizzesPlayed = new ArrayList<>();
        JsonArray quizzesPlayedArray = jsonObject.getJsonArray("playedQuiz");
        for (int j = 0; j < quizzesPlayedArray.size(); j++) {
            quizzesPlayed.add(quizzesPlayedArray.getString(j));
        }

        File profilePicture = UserRequests.GetUserProfilePicture(jsonObject.getString("id"));
        return new UserAPI(id, pseudo, email, score, favQuizzes, createdQuizzes, quizzesPlayed, profilePicture);
    }

    /**
     * Convert a QuizGame to a JsonObject
     * @param quizGame QuizGame to convert
     * @return JsonObject
     */
    public static JsonObject createQuizGameJson(QuizGame quizGame) {
        JsonObjectBuilder questionnaryBuilder = Json.createObjectBuilder();
        JsonArrayBuilder questionBuilder = Json.createArrayBuilder();

        for (QuizQuestion question : quizGame.getQuestions()) {
            JsonArrayBuilder answerBuilder = Json.createArrayBuilder();

            for (QuizAnswer answer : question.getAnswers()) {
                JsonObjectBuilder answerObjectBuilder = Json.createObjectBuilder()
                        .add("answer", answer.getAnswer())
                        .add("isCorrect", answer.isCorrect());

                answerBuilder.add(answerObjectBuilder.build());
            }

            JsonObjectBuilder questionObjectBuilder = Json.createObjectBuilder()
                    .add("question", question.getQuestion())
                    .add("answers", answerBuilder);

            questionBuilder.add(questionObjectBuilder.build());
        }

        questionnaryBuilder.add("questions", questionBuilder);

        return questionnaryBuilder.build();
    }

    public static QuizGame parseJsonToQuizGame(String json) {
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        ArrayList<QuizQuestion> questions = new ArrayList<>();

        JsonArray questionsArray = jsonObject.getJsonArray("questions");
        for (JsonValue questionValue : questionsArray) {
            JsonObject questionObject = questionValue.asJsonObject();
            String question = questionObject.getString("question");

            ArrayList<QuizAnswer> answers = new ArrayList<>();
            JsonArray answersArray = questionObject.getJsonArray("answers");
            for (JsonValue answerValue : answersArray) {
                JsonObject answerObject = answerValue.asJsonObject();
                String answer = answerObject.getString("answer");
                boolean isCorrect = answerObject.getBoolean("isCorrect");
                answers.add(new QuizAnswer(answer, isCorrect));
            }

            questions.add(new QuizQuestion(question, answers));
        }

        return new QuizGame(questions);
    }
}
