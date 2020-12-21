package net.bnijik.spotify.explorer.commands;

import net.bnijik.spotify.explorer.AuthSpotifyService;
import net.bnijik.spotify.explorer.Cache;
import net.bnijik.spotify.explorer.MusicSpotifyService;
import net.bnijik.spotify.explorer.UserConsole;
import net.bnijik.spotify.explorer.model.Album;

import java.util.Map;

public class NewAlbumsCommand implements SpotifyExplorerCommand {
    private final AuthSpotifyService authSpotifyService;
    private final MusicSpotifyService musicSpotifyService;
    private final UserConsole view;
    @SuppressWarnings("rawtypes")
    private final Map<String, Cache> itemCaches;

    public NewAlbumsCommand(AuthSpotifyService authSpotifyService, MusicSpotifyService musicSpotifyService, UserConsole view, @SuppressWarnings("rawtypes") Map<String, Cache> itemCaches) {
        this.authSpotifyService = authSpotifyService;
        this.musicSpotifyService = musicSpotifyService;
        this.view = view;
        this.itemCaches = itemCaches;

        if (this.itemCaches.get("new") == null) {
            this.itemCaches.put("new", new Cache<Album>(this.view.itemsPerPage()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute() {
        if (this.authSpotifyService.isAuthorized()) {
            if (this.itemCaches.get("new")
                    .getItems()
                    .isEmpty()) {
                this.itemCaches.get("new")
                        .setItems(this.musicSpotifyService.getNewAlbums(this.authSpotifyService.getAccessToken()));
            }
            this.itemCaches.put("nowShowing", this.itemCaches.get("new"));
            this.view.display(this.itemCaches.get("nowShowing")
                                      .currentPage());
        } else {
            this.view.display("Please authenticate with Spotify first ('auth')");
        }
    }
}
