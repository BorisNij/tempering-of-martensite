package net.bnijik.spotify.explorer.service;

public interface AuthSpotifyService {
    boolean startListeningForAccessCode();

    boolean manageToSendDefaultBrowserToAuthUri();

    boolean manageToGetAccessCode();

    void stopListeningForAccessCode();

    boolean manageToGetAccessToken();

    String authUri();

    boolean isAuthorized();

    String getAccessToken();
}
