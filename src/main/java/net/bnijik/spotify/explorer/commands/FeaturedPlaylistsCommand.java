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

        if (itemCaches.get("featured") == null) {
            itemCaches.put("featured", new MusicItemCache<Playlist>(view.itemsPerPage()));
        }
    }

    @Override
    public void execute() {
        if (authSpotifyService.isAuthorized()) {
            if (itemCaches.get("featured")
                    .getItems()
                    .isEmpty()) {
                //noinspection unchecked
                itemCaches.get("featured")
                        .setItems(musicSpotifyService.getPlaylists(authSpotifyService.getAccessToken()));
            }
            itemCaches.put("nowShowing", itemCaches.get("featured"));
            //noinspection unchecked
            view.display(itemCaches.get("nowShowing")
                                 .currentPage());
        } else {
            view.display("Please authenticate with Spotify first ('auth')");
        }
    }
}
