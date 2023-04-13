package quiz.api.integration;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.core.NewCookie;

import quiz.api.connection.ApiClient;

public class AuthRequests {

    /**
     * This method will return the cookie value if the login is successful
     * @param login The login of the user
     * @param password The password of the user
     * @return The cookie value if the login is successful
     */
    public static String login(String login, String password) {
        Form form = new Form();
        form.param("login", login);
        form.param("password", password);

        Response response = ApiClient.getInstance(ApiClient._host)
                .target(ApiClient._host + "api/auth/login")
                .request()
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            return "Failed to login";
        }

        NewCookie authCookie = response.getCookies().get(".AspNetCore.Cookies");
        if (authCookie == null) {
            return "Failed to login";
        }

        ApiClient.cookie = authCookie;
        return "Successfully logged in";
    }

    public static String logout() {
        Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/auth/logout")
                .request()
                .method("POST");

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
           return "Successfully logged out.";
        } else {
           return "Failed to log out.";
        }
    }

    public static String register(String login, String password, String email) {
        Form form = new Form();
        form.param("login", login);
        form.param("password", password);
        form.param("email", email);

        try {
            Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/auth/register")
                    .request()
                    .method("POST", Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
            return "Successfully registered.";
        } catch (Exception e) {
            return "Failed to register.";
        }
    }

    public static String UpdatePassword(String elementId, String password) {
        Form form = new Form();
        form.param("elementId", elementId);
        form.param("password", password);

        try {
            Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/auth/password")
                    .request()
                    .cookie(ApiClient.cookie)
                    .put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

            return "Successfully updated password.";
        } catch (Exception e) {
            return "Failed to update password.";
        }
    }

    public static String ping() {
        try {
            Response response = ApiClient.getInstance(ApiClient._host).target(ApiClient._host + "api/auth/ping")
                    .request()
                    .cookie(ApiClient.cookie)
                    .get();
            return "Successfully pinged.";
        } catch (Exception e) {
            return "Failed to ping.";
        }
    }
}
