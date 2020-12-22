package net.bnijik.spotify.explorer;

import com.sun.net.httpserver.HttpServer;
import net.bnijik.spotify.explorer.commands.AuthCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Manages authorization through Spotify's OAuth 2.0 endpoint to get an Access Token.
 * Follows a five-step process (see {@link AuthCommand} class
 * for the actual execution sequence):
 * <ol>
 *     <li> Starts an {@link HttpServer} and begins listening for Access Code</li>
 *     <li> Attempts to send the default browser of the user to Spotify's OAuth endpoint ({@link #manageToSendDefaultBrowserToAuthUri()}),
 *     <li> OAuth presents the user with an "Authorize access" or "Cancel" choice</li>
 *     <li> Spotify redirects the user's browser to a redirect uri, and if the user chooses
 *          "Authorizes access", Spotify sends back an Access Code</li>
 *     <li> Issues a <i>POST</i> {@link HttpRequest} with the Access Code and client credentials
 *          in its body to request an Access Token </li>
 * </ol>
 * Uses an {@link SpotifyResponseParser} to parse the JSON response from Spotify and extract the Access Token.
 * Provides accessor methods for the Access Token ({@link #getAccessToken()}) and also for the auth uri
 * ({@link #authUri()}) for the case the user needs to point his browser there by himself
 * (if {@link #manageToSendDefaultBrowserToAuthUri()} method fails).
 */
@Service
public class AuthSpotifyService {

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

    private final SpotifyResponseParser<String> responseParser;
    private String accessToken = "";
    private String accessCode = "";
    private HttpServer server;

    @Autowired
    public AuthSpotifyService(SpotifyResponseParser<String> responseParser) {
        this.responseParser = responseParser;
    }

    public boolean startListeningForAccessCode() {
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(port), 0);
            server.start();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean manageToSendDefaultBrowserToAuthUri() {
        try {
            Desktop.getDesktop()
                    .browse(URI.create(authUri()));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean manageToGetAccessCode() {
        Semaphore s = new Semaphore(0);
        final AccessCodeHolder accessCodeHolder = new AccessCodeHolder();

        server.createContext(this.contextPath, exchange -> {
            try {
                String query = exchange.getRequestURI()
                        .getQuery();
                String status;
                if (query != null && query.contains("code=")) {
                    accessCodeHolder.aCode = query.substring(5);
                    status = "Got the code. Return back to your program.";
                } else {
                    status = "Not found authorization code. Try again.";
                    accessCodeHolder.aCode = null;
                }
                exchange.sendResponseHeaders(200, status.length());
                exchange.getResponseBody()
                        .write(status.getBytes());
                exchange.getResponseBody()
                        .close();
            } finally {
                s.release();
            }
        });

        try {
            //noinspection ResultOfMethodCallIgnored
            s.tryAcquire(1, TimeUnit.MINUTES);
        } catch (Exception e) {
            return false;
        }
        this.accessCode = accessCodeHolder.aCode;
        return true;
    }

    public void stopListeningForAccessCode() {
        this.server.stop(1);
    }

    public boolean manageToGetAccessToken() {
        if (accessCode == null || accessCode.isBlank()) {
            return false;
        }

        String uri = baseUri + tokenPath;

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-type", "application/x-www-form-urlencoded")
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString("&client_id=" + clientId
                                                          + "&client_secret=" + clientSecret
                                                          + "&grant_type=authorization_code"
                                                          + "&code=" + accessCode
                                                          + "&redirect_uri=" + redirectBaseUri + ":" + port))
                .build();

        try {
            HttpClient client = HttpClient.newBuilder()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String parsedAccessToken = this.responseParser.parseAccessToken(response.body());

            if (parsedAccessToken.toLowerCase()
                        .contains("error") || parsedAccessToken.isBlank()) {
                throw new IOException();
            }
            this.accessToken = parsedAccessToken;

        } catch (IOException | InterruptedException | NullPointerException e) {
            this.accessToken = null;
            return false;
        }

        return true;
    }

    public boolean isAuthorized() {
        return accessToken != null && !accessToken.isBlank();
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String authUri() {
        return this.baseUri
               + "/authorize?client_id=" + clientId
               + "&redirect_uri=" + redirectBaseUri + ":" + port
               + "&response_type=code";
    }

    private static class AccessCodeHolder {
        private volatile String aCode = null;
    }
}