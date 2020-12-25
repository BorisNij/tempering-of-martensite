package net.bnijik.spotify.explorer.service.commands;

import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.service.AuthSpotifyService;
import net.bnijik.spotify.explorer.service.UserConsoleService;

import java.util.Map;

public class PrevPageCommand implements SpotifyExplorerCommand {
    private final AuthSpotifyService authSpotifyService;
    private final UserConsoleService view;
    @SuppressWarnings("rawtypes")
    private final Map<String, MusicItemCache> itemCaches;

    public PrevPageCommand(AuthSpotifyService authSpotifyService, UserConsoleService view, @SuppressWarnings("rawtypes") Map<String, MusicItemCache> itemCaches) {
        this.authSpotifyService = authSpotifyService;
        this.view = view;
        this.itemCaches = itemCaches;
    }

    @Override
    public void execute() {
        if (authSpotifyService.isAuthorized()) {
            @SuppressWarnings("rawtypes") final MusicItemCache nowShowing = itemCaches.get("nowShowing");
            if (nowShowing.getItems()
                    .isEmpty()) {
                view.errorMsg("Item list is not initialized. " +
                              "Try fetching items from Spotify first ('new', 'featured', 'categories')");
                return;
            }
            //noinspection unchecked
            view.display(itemCaches.get("nowShowing")
                                 .prevPage());
        } else {
            view.display("Please authenticate with Spotify first ('auth')");
        }
    }
}
