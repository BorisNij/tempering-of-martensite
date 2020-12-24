package net.bnijik.spotify.explorer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthSpotifyConfig {
    @Value("${spotify.auth.base-uri}")
    private String baseUri;

    @Value("${spotify.auth.token-path}")
    private String tokenPath;

    @Value("${server.port}")
    private int port;

    @Value("${spotify.auth.redirect-base-uri}")
    private String redirectBaseUri;

    @Value("${spotify.auth.client-id}")
    private String clientId;

    @Value("${spotify.auth.client-secret}")
    private String clientSecret;

    @Value("${server.context-path}")
    private String contextPath;

    public String getBaseUri() {
        return baseUri;
    }

    public String getTokenPath() {
        return tokenPath;
    }

    public int getPort() {
        return port;
    }

    public String getRedirectBaseUri() {
        return redirectBaseUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getContextPath() {
        return contextPath;
    }
}
