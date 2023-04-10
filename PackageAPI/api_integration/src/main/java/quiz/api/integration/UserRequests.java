package quiz.api.integration;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
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
public class UserRequests extends ApiClient {
    /**
     * This constructor is used to create a new UserRequests object
     * @param host The host of the API declared in the ApiClient<br>
     * Example: http://localhost:8080/
     */
    public UserRequests(String host) {
        super(host);
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public String ping() {
        try {
            return super.getClient().target(super.getHost() + "api/user/ping")
                    .request()
                    .get(String.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public ArrayList<UserAPI> GetAllUsers() {
        try {
            String response = super.getClient().target(super.getHost() + "api/user/")
                    .request()
                    .get(String.class);
            return JsonUtils.parseJsonToUserList(response, this);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public UserAPI GetUserById(String elementid) {
        try {
            String url = super.getHost() + "api/user/byid/" + elementid;
            String response = super.getClient().target(url)
                    .request()
                    .get(String.class);
            return JsonUtils.parseJsonToUserProfile(response, this);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public String GetUserByLogin(String login) {
        String url = super.getHost() + "api/user/bylogin/" + login;
        try {
            String response = super.getClient().target(url)
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
    public File GetUserProfilePicture(String elementid) {
        String url = super.getHost() + "api/user/userprofilepicture/" + elementid;
        try {
            File response = super.getClient().target(url)
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
    public String UpdateUserProfilePicture(String userid, String elementid, File file) {
        FormDataMultiPart form = new FormDataMultiPart();
        form.field("userid", userid);
        form.field("elementid", elementid);
        form.bodyPart(new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE));

        try {
            String response = super.getClient().target(super.getHost() + "api/user/userprofilepicture")
                    .request()
                    .method("PUT", Entity.entity(form, MediaType.MULTIPART_FORM_DATA_TYPE), String.class);
            return response;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * Refer to UserRequests class documentation for more information
     */
    public String UpdateUserScore(String userid, String elementid, int score) {
        Form form = new Form();
        form.param("userid", userid);
        form.param("elementid", elementid);
        form.param("score", String.valueOf(score));

        try {
            String response = super.getClient().target(super.getHost() + "api/user/score")
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
    public String UpdateUserAddScore(String userid, String elementid, int score) {
        Form form = new Form();
        form.param("userid", userid);
        form.param("elementid", elementid);
        form.param("score", String.valueOf(score));

        try {
            String response = super.getClient().target(super.getHost() + "api/user/addscore")
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
    public String UpdateUserSubScore(String userid, String elementid, int score) {
        Form form = new Form();
        form.param("userid", userid);
        form.param("elementid", elementid);
        form.param("score", String.valueOf(score));

        try {
            String response = super.getClient().target(super.getHost() + "api/user/subscore")
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
    public String UpdateUserAddFavorite(String userid, String elementid, String quizId) {
        Form form = new Form();
        form.param("userid", userid);
        form.param("elementid", elementid);
        form.param("quizid", quizId);

        try {
            String response = super.getClient().target(super.getHost() + "api/user/addfavquiz")
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
    public String UpdateUserRemoveFavorite(String userid, String elementid, String quizId) {
        Form form = new Form();
        form.param("userid", userid);
        form.param("elementid", elementid);
        form.param("quizid", quizId);

        try {
            String response = super.getClient().target(super.getHost() + "api/user/removefavquiz")
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
    public String UpdateUserAddCreatedQuiz(String userid, String elementid, String quizId) {
        Form form = new Form();
        form.param("userid", userid);
        form.param("elementid", elementid);
        form.param("quizid", quizId);

        try {
            String response = super.getClient().target(super.getHost() + "api/user/addcreatedquiz")
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
    public String  UpdateUserAddPlayedQuiz(String userid, String elementid, String quizId) {
        Form form = new Form();
        form.param("userid", userid);
        form.param("elementid", elementid);
        form.param("quizid", quizId);

        try {
            String response = super.getClient().target(super.getHost() + "api/user/addplayedquiz")
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
    public String UpdateUserPseudo(String userid, String elementid, String username) {
        Form form = new Form();
        form.param("userid", userid);
        form.param("elementid", elementid);
        form.param("username", username);

        try {
            String response = super.getClient().target(super.getHost() + "api/user/updateusername")
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
    public String UpdateUserEmail(String userid, String elementid, String email) {
        Form form = new Form();
        form.param("userid", userid);
        form.param("elementid", elementid);
        form.param("email", email);

        try {
            String response = super.getClient().target(super.getHost() + "api/user/updateemail")
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
    public String UpdateUserPassword(String userid, String elementid, String password) {
        Form form = new Form();
        form.param("userid", userid);
        form.param("elementid", elementid);
        form.param("password", password);

        try {
            String response = super.getClient().target(super.getHost() + "api/user/updatepassword")
                    .request()
                    .method("PUT", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
            return response;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
