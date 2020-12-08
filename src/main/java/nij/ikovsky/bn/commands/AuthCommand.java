package nij.ikovsky.bn.commands;

import nij.ikovsky.bn.AuthService;
import nij.ikovsky.bn.UserConsole;

public class AuthCommand implements SpotifyExplorerCommand {

    private final AuthService authService;
    private final UserConsole view;

    public AuthCommand(AuthService authService, UserConsole view) {
        this.authService = authService;
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
        if (!authService.manageToGetAccessToken()) {
            this.view.errorMsg("Failed to get Access Token");
        } else {
            this.view.display("Access Token received successfully." + "\n" +
                              "Authentication completed!");
        }
    }

    private void tryGettingAccessCode() {
        if (!authService.manageToGetAccessCode()) {
            this.view.errorMsg("Failed to get Access Code");
        } else {
            this.authService.stopListeningForAccessCode();
            this.view.display("Access Code received successfully.");
        }
    }

    private void trySendingDefaultBrowserToAuthUri() {
        if (!authService.manageToSendDefaultBrowserToAuthUri()) {
            this.view.errorMsg("Failed to start default browser.");
            this.view.errorMsg("Use this link to request Access Code:");
            this.view.display(this.authService.authUri());
        }
    }

    private void tryStartListeningForAccessCode() {
        if (!authService.startListeningForAccessCode()) {
            this.view.errorMsg("Failed to start server, cannot begin listening for Access Code");
        }
    }
}
