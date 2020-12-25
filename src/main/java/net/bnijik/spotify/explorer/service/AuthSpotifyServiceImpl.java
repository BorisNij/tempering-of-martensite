package net.bnijik.spotify.explorer.service;

import com.sun.net.httpserver.HttpServer;
import net.bnijik.spotify.explorer.configuration.AuthSpotifyConfig;
import net.bnijik.spotify.explorer.service.commands.AuthCommand;
import net.bnijik.spotify.explorer.utils.SpotifyResponseParser;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthSpotifyServiceImpl implements AuthSpotifyService {

    private final SpotifyResponseParser<String> responseParser;
    private final AuthSpotifyConfig authSpotifyConfig;
    private final HttpClient.Builder httpClientBuilder;
    private final HttpRequest.Builder httpRequestBuilder;

    private String accessToken = "";
    private String accessCode = "";
    private HttpServer server;

    @Autowired
    public AuthSpotifyServiceImpl(SpotifyResponseParser<String> responseParser, AuthSpotifyConfig authSpotifyConfig, HttpClient.Builder httpClientBuilder) {
        this.responseParser = responseParser;
        this.authSpotifyConfig = authSpotifyConfig;
        this.httpClientBuilder = httpClientBuilder;
        this.httpRequestBuilder = HttpRequest.newBuilder();
    }

    @Override
    public boolean startListeningForAccessCode() {
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(authSpotifyConfig.getPort()), 0);
            server.start();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean manageToSendDefaultBrowserToAuthUri() {
        try {
            Desktop.getDesktop()
                    .browse(URI.create(authUri()));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean manageToGetAccessCode() {
        Semaphore s = new Semaphore(0);
        final AccessCodeHolder accessCodeHolder = new AccessCodeHolder();

        server.createContext(authSpotifyConfig.getContextPath(), exchange -> {
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
        accessCode = accessCodeHolder.aCode;
        return true;
    }

    @Override
    public void stopListeningForAccessCode() {
        server.stop(1);
    }

    @Override
    public boolean manageToGetAccessToken() {
        if (accessCode == null || accessCode.isBlank()) {
            return false;
        }

        String uri = authSpotifyConfig.getBaseUri() + authSpotifyConfig.getTokenPath();

        HttpRequest request = httpRequestBuilder
                .header("Content-type", "application/x-www-form-urlencoded")
                .uri(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.ofString("&client_id=" + authSpotifyConfig.getClientId()
                                                          + "&client_secret=" + authSpotifyConfig.getClientSecret()
                                                          + "&grant_type=authorization_code"
                                                          + "&code=" + accessCode
                                                          + "&redirect_uri=" + authSpotifyConfig.getRedirectBaseUri() + ":" + authSpotifyConfig.getPort()))
                .build();

        try {
            HttpClient client = httpClientBuilder
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String parsedAccessToken = responseParser.parseAccessToken(response.body());

            if (parsedAccessToken.toLowerCase()
                        .contains("error") || parsedAccessToken.isBlank()) {
                throw new IOException();
            }
            accessToken = parsedAccessToken;

        } catch (IOException | InterruptedException | NullPointerException e) {
            accessToken = null;
            return false;
        }

        return true;
    }

    @Override
    public boolean isAuthorized() {
        return accessToken != null && !accessToken.isBlank();
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String authUri() {
        return authSpotifyConfig.getBaseUri()
               + "/authorize?client_id=" + authSpotifyConfig.getClientId()
               + "&redirect_uri=" + authSpotifyConfig.getRedirectBaseUri() + ":" + authSpotifyConfig.getPort()
               + "&response_type=code";
    }

    private static class AccessCodeHolder {
        private volatile String aCode = null;
    }
}