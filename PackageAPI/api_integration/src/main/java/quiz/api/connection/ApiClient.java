package quiz.api.connection;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;

public class ApiClient {
    private static ApiClient instance;
    private JerseyClient client;
    private final String host;

    protected ApiClient(String host) {
        client = JerseyClientBuilder.createClient();
        this.host = host;
    }

    public static ApiClient getInstance(String host) {
        return instance == null ? instance = new ApiClient(host) : instance;
    }

    public JerseyClient getClient() {
        return client;
    }

    public String getHost() {
        return host;
    }
}
