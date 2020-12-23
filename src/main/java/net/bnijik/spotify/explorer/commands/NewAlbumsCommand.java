package net.bnijik.spotify.explorer.commands;

import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.model.Album;
import net.bnijik.spotify.explorer.service.AuthSpotifyServiceImpl;
import net.bnijik.spotify.explorer.service.MusicSpotifyService;
import net.bnijik.spotify.explorer.service.UserConsoleService;

import java.util.Map;

public class NewAlbumsCommand implements SpotifyExplorerCommand {
    private final AuthSpotifyServiceImpl authSpotifyService;
    private final MusicSpotifyService musicSpotifyService;
    private final UserConsoleService view;
    @SuppressWarnings("rawtypes")
    private final Map<String, MusicItemCache> itemCaches;

    public NewAlbumsCommand(AuthSpotifyServiceImpl authSpotifyService, MusicSpotifyService musicSpotifyService, UserConsoleService view, @SuppressWarnings("rawtypes") Map<String, MusicItemCache> itemCaches) {
        this.authSpotifyService = authSpotifyService;
        this.musicSpotifyService = musicSpotifyService;
        this.view = view;
        this.itemCaches = itemCaches;

        if (this.itemCaches.get("new") == null) {
            this.itemCaches.put("new", new MusicItemCache<Album>(this.view.itemsPerPage()));
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
