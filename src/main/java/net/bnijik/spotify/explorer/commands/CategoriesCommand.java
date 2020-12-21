package net.bnijik.spotify.explorer.commands;


import net.bnijik.spotify.explorer.AuthSpotifyService;
import net.bnijik.spotify.explorer.Cache;
import net.bnijik.spotify.explorer.MusicSpotifyService;
import net.bnijik.spotify.explorer.UserConsole;
import net.bnijik.spotify.explorer.model.Playlist;

import java.util.Map;

public class CategoriesCommand implements SpotifyExplorerCommand {
    private final AuthSpotifyService authSpotifyService;
    private final MusicSpotifyService musicSpotifyService;
    private final UserConsole view;
    @SuppressWarnings("rawtypes")
    private final Map<String, Cache> itemCaches;

    public CategoriesCommand(AuthSpotifyService authSpotifyService, MusicSpotifyService musicSpotifyService, UserConsole view, @SuppressWarnings("rawtypes") Map<String, Cache> itemCaches) {
        this.authSpotifyService = authSpotifyService;
        this.musicSpotifyService = musicSpotifyService;
        this.view = view;
        this.itemCaches = itemCaches;

        if (this.itemCaches.get("categories") == null) {
            this.itemCaches.put("categories", new Cache<Playlist>(this.view.itemsPerPage()));
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