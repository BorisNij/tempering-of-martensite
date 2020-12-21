package niji.kovsky.bn.spotify.explorer.commands;

import niji.kovsky.bn.spotify.explorer.AuthSpotifyService;
import niji.kovsky.bn.spotify.explorer.Cache;
import niji.kovsky.bn.spotify.explorer.UserConsole;

import java.util.Map;

public class NextPageCommand implements SpotifyExplorerCommand {
    private final AuthSpotifyService authSpotifyService;
    private final UserConsole view;
    @SuppressWarnings("rawtypes")
    private final Map<String, Cache> itemCaches;

    public NextPageCommand(AuthSpotifyService authSpotifyService, UserConsole view, @SuppressWarnings("rawtypes") Map<String, Cache> itemCaches) {
        this.authSpotifyService = authSpotifyService;
        this.view = view;
        this.itemCaches = itemCaches;
    }

    @Override
    public void execute() {
        if (this.authSpotifyService.isAuthorized()) {
            @SuppressWarnings("rawtypes") final Cache nowShowing = this.itemCaches.get("nowShowing");
            if (nowShowing.getItems()
                    .isEmpty()) {
                this.view.errorMsg("Item list is not initialized. " +
                                   "Try fetching items from Spotify first ('new', 'featured', 'categories')");
                return;
            }
            //noinspection unchecked
            this.view.display(nowShowing.nextPage());
        } else {
            this.view.display("Please authenticate with Spotify first ('auth')");
        }
    }
}
