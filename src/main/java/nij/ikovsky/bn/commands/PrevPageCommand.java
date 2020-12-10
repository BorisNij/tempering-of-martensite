package nij.ikovsky.bn.commands;

import nij.ikovsky.bn.AuthService;
import nij.ikovsky.bn.Cache;
import nij.ikovsky.bn.UserConsole;

import java.util.Map;

public class PrevPageCommand implements SpotifyExplorerCommand {
    private final AuthService authService;
    private final UserConsole view;
    @SuppressWarnings("rawtypes")
    private final Map<String, Cache> itemCaches;

    public PrevPageCommand(AuthService authService, UserConsole view, @SuppressWarnings("rawtypes") Map<String, Cache> itemCaches) {
        this.authService = authService;
        this.view = view;
        this.itemCaches = itemCaches;
    }

    @Override
    public void execute() {
        if (this.authService.isAuthorized()) {
            @SuppressWarnings("rawtypes") final Cache nowShowing = this.itemCaches.get("nowShowing");
            if (nowShowing.getItems()
                    .isEmpty()) {
                this.view.errorMsg("Item list is not initialized. " +
                                   "Try fetching items from Spotify first ('new', 'featured', 'categories')");
                return;
            }
            //noinspection unchecked
            this.view.display(this.itemCaches.get("nowShowing")
                                      .prevPage());
        } else {
            this.view.display("Please authenticate with Spotify first ('auth')");
        }
    }
}
