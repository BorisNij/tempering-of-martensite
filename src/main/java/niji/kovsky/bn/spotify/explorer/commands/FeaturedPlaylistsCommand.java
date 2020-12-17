package niji.kovsky.bn.spotify.explorer.commands;

import niji.kovsky.bn.spotify.explorer.ApiService;
import niji.kovsky.bn.spotify.explorer.AuthService;
import niji.kovsky.bn.spotify.explorer.Cache;
import niji.kovsky.bn.spotify.explorer.UserConsole;
import niji.kovsky.bn.spotify.explorer.model.Playlist;

import java.util.Map;

@SuppressWarnings("rawtypes")
public class FeaturedPlaylistsCommand implements SpotifyExplorerCommand {
    private final AuthService authService;
    private final ApiService apiService;
    private final UserConsole view;
    private final Map<String, Cache> itemCaches;

    public FeaturedPlaylistsCommand(AuthService authService, ApiService apiService, UserConsole view, Map<String, Cache> itemCaches) {
        this.authService = authService;
        this.apiService = apiService;
        this.view = view;
        this.itemCaches = itemCaches;

        if (this.itemCaches.get("featured") == null) {
            this.itemCaches.put("featured", new Cache<Playlist>(this.view.itemsPerPage()));
        }
    }

    @Override
    public void execute() {
        if (this.authService.isAuthorized()) {
            if (this.itemCaches.get("featured")
                    .getItems()
                    .isEmpty()) {
                //noinspection unchecked
                this.itemCaches.get("featured")
                        .setItems(this.apiService.getPlaylists(this.authService.getAccessToken()));
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
