package net.bnijik.spotify.explorer.commands;


import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.model.Playlist;
import net.bnijik.spotify.explorer.service.AuthSpotifyServiceImpl;
import net.bnijik.spotify.explorer.service.MusicSpotifyService;
import net.bnijik.spotify.explorer.service.UserConsoleService;

import java.util.Map;

public class CategoriesCommand implements SpotifyExplorerCommand {
    private final AuthSpotifyServiceImpl authSpotifyService;
    private final MusicSpotifyService musicSpotifyService;
    private final UserConsoleService view;
    @SuppressWarnings("rawtypes")
    private final Map<String, MusicItemCache> itemCaches;

    public CategoriesCommand(AuthSpotifyServiceImpl authSpotifyService, MusicSpotifyService musicSpotifyService, UserConsoleService view, @SuppressWarnings("rawtypes") Map<String, MusicItemCache> itemCaches) {
        this.authSpotifyService = authSpotifyService;
        this.musicSpotifyService = musicSpotifyService;
        this.view = view;
        this.itemCaches = itemCaches;

        if (this.itemCaches.get("categories") == null) {
            this.itemCaches.put("categories", new MusicItemCache<Playlist>(this.view.itemsPerPage()));
        }
    }

    @Override
    public void execute() {
        if (this.authSpotifyService.isAuthorized()) {
            if (this.itemCaches.get("categories")
                    .getItems()
                    .isEmpty()) {
                //noinspection unchecked
                this.itemCaches.get("categories")
                        .setItems(this.musicSpotifyService.getCategories(this.authSpotifyService.getAccessToken()));
            }
            this.itemCaches.put("nowShowing", this.itemCaches.get("categories"));
            //noinspection unchecked
            this.view.display(this.itemCaches.get("nowShowing")
                                      .currentPage());
        } else {
            this.view.display("Please authenticate with Spotify first ('auth')");
        }
    }
}
