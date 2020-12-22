package net.bnijik.spotify.explorer;

import com.sun.net.httpserver.HttpServer;
import net.bnijik.spotify.explorer.commands.AuthCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
public class AuthSpotifyService {

    //TODO: move properties to Resources
    private static final String SCHEME = "https://";
    private static final String AUTH_HOST = "accounts.spotify.com";
    private static final String TOKEN_PATH = "/api/token";
    private static final int PORT = 9090;
    private static final String REDIRECT_URI = "http://localhost" + ":" + PORT;
    private static final String CLIENT_ID = "8acb3fc9c0b7438eb583e7fce44f819a";
    private static final String CLIENT_SECRET = "23df0fbc957340e59733190b8d8acc53";

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
            server.bind(new InetSocketAddress(PORT), 0);
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

        server.createContext("/", exchange -> {
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

        String uri = SCHEME + AUTH_HOST + TOKEN_PATH;

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-type", "application/x-www-form-urlencoded")
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString("&client_id=" + CLIENT_ID
                                                          + "&client_secret=" + CLIENT_SECRET
                                                          + "&grant_type=authorization_code"
                                                          + "&code=" + accessCode
                                                          + "&redirect_uri=" + REDIRECT_URI))
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
        return "https://" + AUTH_HOST
               + "/authorize?client_id=" + CLIENT_ID
               + "&redirect_uri=" + REDIRECT_URI
               + "&response_type=code";
    }

    private static class AccessCodeHolder {
        private volatile String aCode = null;
    }
}