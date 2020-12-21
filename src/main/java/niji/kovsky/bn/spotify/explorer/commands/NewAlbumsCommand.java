package niji.kovsky.bn.spotify.explorer.commands;

import niji.kovsky.bn.spotify.explorer.AuthSpotifyService;
import niji.kovsky.bn.spotify.explorer.Cache;
import niji.kovsky.bn.spotify.explorer.MusicSpotifyService;
import niji.kovsky.bn.spotify.explorer.UserConsole;
import niji.kovsky.bn.spotify.explorer.model.Album;

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
