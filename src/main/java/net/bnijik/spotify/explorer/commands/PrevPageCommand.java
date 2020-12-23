package net.bnijik.spotify.explorer.commands;

import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.service.AuthSpotifyServiceImpl;
import net.bnijik.spotify.explorer.service.UserConsoleService;

import java.util.Map;

public class PrevPageCommand implements SpotifyExplorerCommand {
    private final AuthSpotifyServiceImpl authSpotifyService;
    private final UserConsoleService view;
    @SuppressWarnings("rawtypes")
    private final Map<String, MusicItemCache> itemCaches;

    public PrevPageCommand(AuthSpotifyServiceImpl authSpotifyService, UserConsoleService view, @SuppressWarnings("rawtypes") Map<String, MusicItemCache> itemCaches) {
        this.authSpotifyService = authSpotifyService;
        this.view = view;
        this.itemCaches = itemCaches;
    }

    @Override
    public void execute() {
        if (this.authSpotifyService.isAuthorized()) {
            @SuppressWarnings("rawtypes") final MusicItemCache nowShowing = this.itemCaches.get("nowShowing");
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
