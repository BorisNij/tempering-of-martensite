package niji.kovsky.bn.spotify.explorer.commands;

import niji.kovsky.bn.spotify.explorer.AuthSpotifyService;
import niji.kovsky.bn.spotify.explorer.UserConsole;

public class AuthCommand implements SpotifyExplorerCommand {

    private final AuthSpotifyService authSpotifyService;
    private final UserConsole view;

    public AuthCommand(AuthSpotifyService authSpotifyService, UserConsole view) {
        this.authSpotifyService = authSpotifyService;
        this.view = view;
    }

    @Override
    public void execute() {
        tryStartListeningForAccessCode();

        this.view.display("Waiting for Access Code...");

        trySendingDefaultBrowserToAuthUri();

        tryGettingAccessCode();

        tryGettingAccessToken();
    }

    private void tryGettingAccessToken() {
        if (!authSpotifyService.manageToGetAccessToken()) {
            this.view.errorMsg("Failed to get Access Token");
        } else {
            this.view.display("Access Token received successfully." + "\n" +
                              "Authentication completed!");
        }
    }

    private void tryGettingAccessCode() {
        if (!authSpotifyService.manageToGetAccessCode()) {
            this.view.errorMsg("Failed to get Access Code");
        } else {
            this.authSpotifyService.stopListeningForAccessCode();
            this.view.display("Access Code received successfully.");
        }
    }

    private void trySendingDefaultBrowserToAuthUri() {
        if (!authSpotifyService.manageToSendDefaultBrowserToAuthUri()) {
            this.view.errorMsg("Failed to start default browser.");
            this.view.errorMsg("Use this link to request Access Code:");
            this.view.display(this.authSpotifyService.authUri());
        }
    }

    private void tryStartListeningForAccessCode() {
        if (!authSpotifyService.startListeningForAccessCode()) {
            this.view.errorMsg("Failed to start server, cannot begin listening for Access Code");
        }
    }
}
