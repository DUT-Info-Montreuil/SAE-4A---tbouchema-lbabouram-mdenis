package quiz.api.integration;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;
import quiz.api.connection.ApiClient;
import quiz.api.dto.QuizInfoAPI;
import quiz.api.dto.QuizGame;

import javax.ws.rs.core.MediaType;
import quiz.api.utils.JsonUtils;

import java.util.ArrayList;

import static quiz.api.utils.JsonUtils.createQuizGameJson;

/**
 * Each Update method used in this class will need the <b>userid</b> and the <b>elementid</b> and will return the response of the server if it succeeds otherwise they will return <b>null</b> <br> <br>
 * The <b>userid</b> is the id of the user that is logged in and the <b>elementid</b> is the id of the element that is being updated. <br> <br>
 * The last parameter if there is one is the new value of the element. <br> <br>
 * The Get methods will only need the <b>elementid</b> except <br> <pre>GetAllQuiz()</pre> and will return an Object if the request succeeds otherwise they will return <b>null</b> <br>
 */
public class QuizRequests {

    /**
     * Return 5 random quizzes at each request <br>
     * If there are less than 5 quizzes in the database it will return all the quizzes which have not been sent yet <br>
     * After that it will reset the list of quizzes that have been sent and start sending them again <br>
     * Refer to QuizRequests class documentation for more information
     */
    public static ArrayList<QuizInfoAPI> GetRandomQuizzes() {
        try {
            String response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/quiz/random")
                    .request()
                    .get(String.class);
            return JsonUtils.parseJsonToQuizInfoList(response);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Refer to QuizRequests class documentation for more information
     */
    public static ArrayList<QuizInfoAPI> GetAllQuizzes() {
        try {
            String response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/quiz/")
                    .request()
                    .get(String.class);
            return JsonUtils.parseJsonToQuizInfoList(response);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Refer to QuizRequests class documentation for more information
     */
    public static QuizInfoAPI GetQuizById(String elementId) {
        try {
            String response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/quiz/byid/" + elementId)
                    .request()
                    .get(String.class);
            return JsonUtils.parseJsonToQuizInfo(response);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Refer to QuizRequests class documentation for more information
     */
    public static QuizGame GetQuizGameById(String elementId) {
        try {
            String response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/quiz/game/" + elementId)
                    .request()
                    .get(String.class);
            return JsonUtils.parseJsonToQuizGame(response);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Refer to QuizRequests class documentation for more information
     */
    public static void UpdateQuizQuestionnary(String elementId, QuizGame quizGame) {
        JsonObject requestBody = Json.createObjectBuilder()
                .add("elementId", elementId)
                .add("quizGame", createQuizGameJson(quizGame))
                .build();

        Response response = ApiClient.getInstance(ApiClient._host)
                .target(ApiClient._host + "api/quiz/questionnary/" + elementId)
                .request(MediaType.APPLICATION_JSON)
                .cookie(ApiClient.cookie)
                .put(Entity.json(requestBody));
    }

    /**
     * Refer to QuizRequests class documentation for more information
     */
    public static void UpdateQuizName(String elementId, String name) {
        Form form = new Form();
        form.param("quizName", name);

        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/quiz/name/" + elementId)
                .request()
                .cookie(ApiClient.cookie)
                .build("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED))
                .invoke();
    }


    /**
     * Refer to QuizRequests class documentation for more information
     */
    public static void UpdateQuizDescription(String elementId, String description) {
        Form form = new Form();
        form.param("quizDescription", description);

        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/quiz/description/" + elementId)
                .request()
                .cookie(ApiClient.cookie)
                .build("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED))
                .invoke();
    }

    /**
     * Refer to QuizRequests class documentation for more information
     */
    public static void PostQuiz(QuizInfoAPI quizInfoAPI) {
        JsonArrayBuilder tags = Json.createArrayBuilder();
        for (String tag : quizInfoAPI.getTags()) {
            tags.add(tag);
        }

        JsonObject quizJson = Json.createObjectBuilder()
                .add("quizName", quizInfoAPI.getName())
                .add("quizDescription", quizInfoAPI.getDescription())
                .add("quiztags", tags)
                .add("quizCreatorId", quizInfoAPI.getCreator())
                .add("quizGame", createQuizGameJson(quizInfoAPI.getGame()))
                .build();

        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/quiz/")
                .request()
                .cookie(ApiClient.cookie)
                .post(Entity.entity(quizJson.toString(), MediaType.APPLICATION_JSON));
    }

    /**
     * Refer to QuizRequests class documentation for more information
     */
    public static void DeleteQuiz(String elementId) {

        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/quiz/" + elementId)
                .request()
                .cookie(ApiClient.cookie)
                .delete();
    }
}
