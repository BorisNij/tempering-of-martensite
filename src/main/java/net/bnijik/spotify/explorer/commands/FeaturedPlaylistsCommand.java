package net.bnijik.spotify.explorer.commands;

import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.model.Playlist;
import net.bnijik.spotify.explorer.service.AuthSpotifyServiceImpl;
import net.bnijik.spotify.explorer.service.MusicSpotifyService;
import net.bnijik.spotify.explorer.service.UserConsoleService;

import java.util.Map;

@SuppressWarnings("rawtypes")
public class FeaturedPlaylistsCommand implements SpotifyExplorerCommand {
    private final AuthSpotifyServiceImpl authSpotifyService;
    private final MusicSpotifyService musicSpotifyService;
    private final UserConsoleService view;
    private final Map<String, MusicItemCache> itemCaches;

    public FeaturedPlaylistsCommand(AuthSpotifyServiceImpl authSpotifyService, MusicSpotifyService musicSpotifyService, UserConsoleService view, Map<String, MusicItemCache> itemCaches) {
        this.authSpotifyService = authSpotifyService;
        this.musicSpotifyService = musicSpotifyService;
        this.view = view;
        this.itemCaches = itemCaches;

        if (this.itemCaches.get("featured") == null) {
            this.itemCaches.put("featured", new MusicItemCache<Playlist>(this.view.itemsPerPage()));
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
