package net.bnijik.spotify.explorer.service.commands;


import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.model.Playlist;
import net.bnijik.spotify.explorer.service.AuthSpotifyService;
import net.bnijik.spotify.explorer.service.MusicSpotifyService;
import net.bnijik.spotify.explorer.service.UserConsoleService;

import java.util.Map;

public class CategoriesCommand implements SpotifyExplorerCommand {
    private final AuthSpotifyService authSpotifyService;
    private final MusicSpotifyService musicSpotifyService;
    private final UserConsoleService view;
    @SuppressWarnings("rawtypes")
    private final Map<String, MusicItemCache> itemCaches;

    public CategoriesCommand(AuthSpotifyService authSpotifyService, MusicSpotifyService musicSpotifyService, UserConsoleService view, @SuppressWarnings("rawtypes") Map<String, MusicItemCache> itemCaches) {
        this.authSpotifyService = authSpotifyService;
        this.musicSpotifyService = musicSpotifyService;
        this.view = view;
        this.itemCaches = itemCaches;

        if (itemCaches.get("categories") == null) {
            itemCaches.put("categories", new MusicItemCache<Playlist>(view.itemsPerPage()));
        }
    }

    @Override
    public void execute() {
        if (authSpotifyService.isAuthorized()) {
            if (itemCaches.get("categories")
                    .getItems()
                    .isEmpty()) {
                //noinspection unchecked
                itemCaches.get("categories")
                        .setItems(musicSpotifyService.getCategories(authSpotifyService.getAccessToken()));
            }
            itemCaches.put("nowShowing", itemCaches.get("categories"));
            //noinspection unchecked
            view.display(itemCaches.get("nowShowing")
                                 .currentPage());
        } else {
            view.display("Please authenticate with Spotify first ('auth')");
        }
    }
}
