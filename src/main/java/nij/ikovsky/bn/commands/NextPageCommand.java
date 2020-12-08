package nij.ikovsky.bn.commands;

import nij.ikovsky.bn.AuthService;
import nij.ikovsky.bn.Cache;
import nij.ikovsky.bn.UserConsole;

import java.util.Map;

@SuppressWarnings("rawtypes")
public class NextPageCommand implements SpotifyExplorerCommand {
    private final AuthService authService;
    private final UserConsole view;
    private final Map<String, Cache> itemCaches;

    public NextPageCommand(AuthService authService, UserConsole view, Map<String, Cache> itemCaches) {
        this.authService = authService;
        this.view = view;
        this.itemCaches = itemCaches;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute() {
        if (this.authService.isAuthorized()) {
            this.view.display(this.itemCaches.get("nowShowing")
                                      .nextPage());
        } else {
            this.view.display("Please authenticate with Spotify first ('auth')");
        }
    }
}
