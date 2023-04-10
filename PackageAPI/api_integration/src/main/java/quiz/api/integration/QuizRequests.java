package quiz.api.integration;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import quiz.api.connection.ApiClient;
import quiz.api.dto.QuizInfoAPI;
import quiz.api.dto.QuizGame;

import jakarta.ws.rs.core.MediaType;
import quiz.api.utils.JsonUtils;

import java.util.ArrayList;

import static quiz.api.utils.JsonUtils.createQuizGameJson;

/**
 * Each Update method used in this class will need the <b>userid</b> and the <b>elementid</b> and will return the response of the server if it succeeds otherwise they will return <b>null</b> <br> <br>
 * The <b>userid</b> is the id of the user that is logged in and the <b>elementid</b> is the id of the element that is being updated. <br> <br>
 * The last parameter if there is one is the new value of the element. <br> <br>
 * The Get methods will only need the <b>elementid</b> except <br> <pre>GetAllQuiz()</pre> and will return an Object if the request succeeds otherwise they will return <b>null</b> <br>
 */
public class QuizRequests extends ApiClient {
    /**
     * This constructor is used to create a new QuizRequests object
     * @param host The host of the API declared in the ApiClient <br>
     * Example: http://localhost:8080/
     */
    public QuizRequests(String host) {
        super(host);
    }

    /**
     * Refer to QuizRequests class documentation for more information
     */
    public String ping() {
        try {
            return super.getClient().target(super.getHost() + "api/quiz/ping")
                    .request()
                    .get(String.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Return 5 random quizzes at each request <br>
     * If there are less than 5 quizzes in the database it will return all the quizzes which have not been sent yet <br>
     * After that it will reset the list of quizzes that have been sent and start sending them again <br>
     * Refer to QuizRequests class documentation for more information
     */
    public ArrayList<QuizInfoAPI> GetRandomQuizzes() {
        try {
            String response = super.getClient().target(super.getHost() + "api/quiz/random")
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
    public ArrayList<QuizInfoAPI> GetAllQuizzes() {
        try {
            String response = super.getClient().target(super.getHost() + "api/quiz/")
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
    public QuizInfoAPI GetQuizById(String id) {
        try {
            String response = super.getClient().target(super.getHost() + "api/quiz/byid/" + id)
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
    public QuizGame GetQuizGameById(String id) {
        try {
            String response = super.getClient().target(super.getHost() + "api/quiz/game/" + id)
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
    public String UpdateQuizQuestionnary(String userid, String elementId, QuizGame quizGame) {
        JsonObject quizJson = createQuizGameJson(quizGame);

        FormDataMultiPart form = new FormDataMultiPart();
        form.field("userid", userid);
        form.field("elementId", elementId);
        form.bodyPart(new FormDataBodyPart("quizJson", quizJson.toString(), MediaType.APPLICATION_JSON_TYPE));

        String response = super.getClient().target(super.getHost() + "api/quiz/questionnary")
                .request()
                .method("PUT", Entity.entity(form, MediaType.MULTIPART_FORM_DATA_TYPE), String.class);

        return response;
    }

    /**
     * Refer to QuizRequests class documentation for more information
     */
    public String UpdateQuizName(String userid, String elementId, String name) {
        Form form = new Form();
        form.param("userid", userid);
        form.param("elementId", elementId);
        form.param("quizName", name);

        String response = super.getClient().target(super.getHost() + "api/quiz/name")
                .request()
                .method("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
        return response;
    }

    /**
     * Refer to QuizRequests class documentation for more information
     */
    public String UpdateQuizDescription(String userid, String elementId, String description) {
        Form form = new Form();
        form.param("userid", userid);
        form.param("elementId", elementId);
        form.param("quizDescription", description);

        String response = super.getClient().target(super.getHost() + "api/quiz/description")
                .request()
                .method("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);

        System.out.println(response);
        return response;
    }

    /**
     * Refer to QuizRequests class documentation for more information
     */
    public String PostQuiz(QuizInfoAPI quizInfoAPI) {
        JsonArrayBuilder tags = Json.createArrayBuilder();
        for (String tag : quizInfoAPI.getTags()) {
            tags.add(tag);
        }

        JsonObject quizJson = Json.createObjectBuilder()
                .add("quizName", quizInfoAPI.getName())
                .add("quizDescription", quizInfoAPI.getDescription())
                .add("quiztags", tags)
                .add("quizCreatorId", quizInfoAPI.getCreator())
                .add("quizQuestionnary", createQuizGameJson(quizInfoAPI.getGame()))
                .build();

        String response = super.getClient().target(super.getHost() + "api/quiz")
                .request()
                .post(Entity.entity(quizJson, MediaType.APPLICATION_JSON), String.class);

        System.out.println(response);
        return response;
    }

    /**
     * Refer to QuizRequests class documentation for more information
     */
    public String DeleteQuiz(String userid, String elementId) {
        Form form = new Form();
        form.param("userid", userid);
        form.param("elementId", elementId);

        String response = super.getClient().target(super.getHost() + "api/quiz/byelementid")
                .request()
                .method("DELETE", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);

        System.out.println(response);
        return response;
    }
}
