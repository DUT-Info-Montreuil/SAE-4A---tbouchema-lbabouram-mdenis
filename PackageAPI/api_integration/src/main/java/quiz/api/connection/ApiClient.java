package quiz.api.connection;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;

import jakarta.ws.rs.core.NewCookie;

public class ApiClient {
    private static ApiClient instance;
    private static JerseyClient client;
    public static NewCookie cookie;
    public static String _host;

    protected ApiClient(String host) {
        client = JerseyClientBuilder.createClient();
        _host = host;
    }

    public static JerseyClient getInstance(String host) {
        if (instance == null) {
            instance = new ApiClient(host);
        }
        return client;
    }
}
