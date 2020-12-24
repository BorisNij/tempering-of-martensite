package net.bnijik.spotify.explorer.commands;

import net.bnijik.spotify.explorer.service.AuthSpotifyService;
import net.bnijik.spotify.explorer.service.UserConsoleService;

public class AuthCommand implements SpotifyExplorerCommand {

    private final AuthSpotifyService authSpotifyService;
    private final UserConsoleService view;

    public AuthCommand(AuthSpotifyService authSpotifyService, UserConsoleService view) {
        this.authSpotifyService = authSpotifyService;
        this.view = view;
    }

    @Override
    public void execute() {
        tryStartListeningForAccessCode();

        view.display("Waiting for Access Code...");

        trySendingDefaultBrowserToAuthUri();

        tryGettingAccessCode();

        tryGettingAccessToken();
    }

    private void tryGettingAccessToken() {
        if (!authSpotifyService.manageToGetAccessToken()) {
            view.errorMsg("Failed to get Access Token");
        } else {
            view.display("Access Token received successfully." + "\n" +
                         "Authentication completed!");
        }
    }

    private void tryGettingAccessCode() {
        if (!authSpotifyService.manageToGetAccessCode()) {
            view.errorMsg("Failed to get Access Code");
        } else {
            authSpotifyService.stopListeningForAccessCode();
            view.display("Access Code received successfully.");
        }
    }

    private void trySendingDefaultBrowserToAuthUri() {
        if (!authSpotifyService.manageToSendDefaultBrowserToAuthUri()) {
            view.errorMsg("Failed to start default browser.");
            view.errorMsg("Use this link to request Access Code:");
            view.display(authSpotifyService.authUri());
        }
    }

    private void tryStartListeningForAccessCode() {
        if (!authSpotifyService.startListeningForAccessCode()) {
            view.errorMsg("Failed to start server, cannot begin listening for Access Code");
        }
    }
}
