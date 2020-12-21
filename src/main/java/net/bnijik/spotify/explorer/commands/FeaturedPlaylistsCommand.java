package net.bnijik.spotify.explorer.commands;

import net.bnijik.spotify.explorer.AuthSpotifyService;
import net.bnijik.spotify.explorer.Cache;
import net.bnijik.spotify.explorer.MusicSpotifyService;
import net.bnijik.spotify.explorer.UserConsole;
import net.bnijik.spotify.explorer.model.Playlist;

import java.util.Map;

@SuppressWarnings("rawtypes")
public class FeaturedPlaylistsCommand implements SpotifyExplorerCommand {
    private final AuthSpotifyService authSpotifyService;
    private final MusicSpotifyService musicSpotifyService;
    private final UserConsole view;
    private final Map<String, Cache> itemCaches;

    public FeaturedPlaylistsCommand(AuthSpotifyService authSpotifyService, MusicSpotifyService musicSpotifyService, UserConsole view, Map<String, Cache> itemCaches) {
        this.authSpotifyService = authSpotifyService;
        this.musicSpotifyService = musicSpotifyService;
        this.view = view;
        this.itemCaches = itemCaches;

        if (this.itemCaches.get("featured") == null) {
            this.itemCaches.put("featured", new Cache<Playlist>(this.view.itemsPerPage()));
        }
    }

    @Override
    public void execute() {
        if (this.authSpotifyService.isAuthorized()) {
            if (this.itemCaches.get("featured")
                    .getItems()
                    .isEmpty()) {
                //noinspection unchecked
                this.itemCaches.get("featured")
                        .setItems(this.musicSpotifyService.getPlaylists(this.authSpotifyService.getAccessToken()));
            }
            this.itemCaches.put("nowShowing", this.itemCaches.get("featured"));
            //noinspection unchecked
            this.view.display(this.itemCaches.get("nowShowing")
                                      .currentPage());
        } else {
            this.view.display("Please authenticate with Spotify first ('auth')");
        }
    }
}
