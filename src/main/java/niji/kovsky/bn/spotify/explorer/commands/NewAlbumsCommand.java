package niji.kovsky.bn.spotify.explorer.commands;

import niji.kovsky.bn.spotify.explorer.ApiService;
import niji.kovsky.bn.spotify.explorer.AuthService;
import niji.kovsky.bn.spotify.explorer.Cache;
import niji.kovsky.bn.spotify.explorer.UserConsole;
import niji.kovsky.bn.spotify.explorer.model.Album;

import java.util.Map;

@SuppressWarnings("rawtypes")
public class NewAlbumsCommand implements SpotifyExplorerCommand {
    private final AuthService authService;
    private final ApiService apiService;
    private final UserConsole view;
    private final Map<String, Cache> itemCaches;

    public NewAlbumsCommand(AuthService authService, ApiService apiService, UserConsole view, Map<String, Cache> itemCaches) {
        this.authService = authService;
        this.apiService = apiService;
        this.view = view;
        this.itemCaches = itemCaches;

        if (this.itemCaches.get("new") == null) {
            this.itemCaches.put("new", new Cache<Album>(this.view.itemsPerPage()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute() {
        if (this.authService.isAuthorized()) {
            if (this.itemCaches.get("new")
                    .getItems()
                    .isEmpty()) {
                this.itemCaches.get("new")
                        .setItems(this.apiService.getNewAlbums(this.authService.getAccessToken()));
            }
            this.itemCaches.put("nowShowing", this.itemCaches.get("new"));
            this.view.display(this.itemCaches.get("nowShowing")
                                      .currentPage());
        } else {
            this.view.display("Please authenticate with Spotify first ('auth')");
        }
    }
}
