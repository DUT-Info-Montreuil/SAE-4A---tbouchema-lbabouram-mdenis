package quiz.api.integration;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import quiz.api.connection.ApiClient;
import quiz.api.dto.UserAPI;
import quiz.api.utils.JsonUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Each Update method used in this class will need the <b>userid</b> and the <b>elementid</b> and will return the response of the server if it succeeds otherwise they will return <b>null</b> <br> <br>
 * The <b>userid</b> is the id of the user that is logged in and the <b>elementid</b> is the id of the element that is being updated. <br> <br>
 * The last parameter if there is one is the new value of the element. <br> <br>
 * The Get methods will only need the <b>elementid</b> except <br> <pre>GetAllUsers()</pre> and will return a <b>UserAPI</b> object if the request succeeds otherwise they will return <b>null</b> <br>
 */
public class UserRequests {

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static ArrayList<UserAPI> GetAllUsers() {
        try {
            String response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/user/")
                    .request()
                    .get(String.class);
            return JsonUtils.parseJsonToUserList(response);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static UserAPI GetUserById(String elementid) {
        try {
            String url = ApiClient._host + "api/user/byid/" + elementid;
            String response = ApiClient.getInstance(ApiClient._host).target(url)
                    .request()
                    .get(String.class);
            return JsonUtils.parseJsonToUserProfile(response);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static String GetUserByLogin(String login) {
        String url = ApiClient._host + "api/user/bylogin/" + login;
        try {
            String response = ApiClient.getInstance(ApiClient._host).target(url)
                    .request()
                    .get(String.class);
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static File GetUserProfilePicture(String elementId) {
        String url = ApiClient._host + "api/user/userprofilepicture/" + elementId;
        try {
            File response = ApiClient.getInstance(ApiClient._host).target(url)
                    .request()
                    .get(File.class);
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static void UpdateUserProfilePicture(String elementId, File file) {
        FormDataMultiPart form = new FormDataMultiPart();
        form.bodyPart(new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE));

        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/user/userprofilepicture/" + elementId)
                .request()
                .method("PUT", Entity.entity(form, form.getMediaType()), Response.class);
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static void UpdateUserScore(String elementId, int score) {
        Form form = new Form();
        form.param("score", String.valueOf(score));

        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/user/score/" + elementId)
                .request()
                .method("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static void UpdateUserAddScore(String elementId, int score) {
        Form form = new Form();
        form.param("score", String.valueOf(score));

        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/user/addscore/" + elementId)
                .request()
                .method("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static String UpdateUserSubScore(String userid, String elementId, int score) {
        Form form = new Form();
        form.param("score", String.valueOf(score));

        try {
            String response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/user/subscore/" + elementId)
                    .request()
                    .method("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
            return response;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static void UpdateUserAddFavorite(String userid, String elementId, String quizId) {
        Form form = new Form();
        form.param("quizid", quizId);

        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/user/addfavquiz/" + elementId)
                .request()
                .method("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static void UpdateUserRemoveFavorite(String elementId, String quizId) {
        Form form = new Form();
        form.param("quizid", quizId);

        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/user/removefavquiz/" + elementId)
                .request()
                .method("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static void UpdateUserAddCreatedQuiz(String elementId, String quizId) {
        Form form = new Form();
        form.param("quizid", quizId);

        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/user/addcreatedquiz/" + elementId)
                .request()
                .method("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static void UpdateUserAddPlayedQuiz(String elementId, String quizId) {
        Form form = new Form();;
        form.param("quizid", quizId);

        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/user/addplayedquiz/" + elementId)
                .request()
                .method("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static void UpdateUserPseudo(String elementId, String username) {
        Form form = new Form();
        form.param("username", username);

        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/user/updatepseudo/" + elementId)
                .request()
                .method("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static void UpdateUserEmail(String elementId, String email) {
        Form form = new Form();
        form.param("email", email);

        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/user/updateemail/" + elementId)
                .request()
                .method("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public static void UpdateUserPassword(String elementId, String password) {
        Form form = new Form();
        form.param("password", password);

        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/user/updatepassword/" + elementId)
                .request()
                .method("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
    }
}
