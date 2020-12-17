package niji.kovsky.bn.spotify.explorer.commands;


import niji.kovsky.bn.spotify.explorer.ApiService;
import niji.kovsky.bn.spotify.explorer.AuthService;
import niji.kovsky.bn.spotify.explorer.Cache;
import niji.kovsky.bn.spotify.explorer.UserConsole;
import niji.kovsky.bn.spotify.explorer.model.Playlist;

import java.util.Map;

public class CategoriesCommand implements SpotifyExplorerCommand {
    private final AuthService authService;
    private final ApiService apiService;
    private final UserConsole view;
    @SuppressWarnings("rawtypes")
    private final Map<String, Cache> itemCaches;

    public CategoriesCommand(AuthService authService, ApiService apiService, UserConsole view, @SuppressWarnings("rawtypes") Map<String, Cache> itemCaches) {
        this.authService = authService;
        this.apiService = apiService;
        this.view = view;
        this.itemCaches = itemCaches;

        if (this.itemCaches.get("categories") == null) {
            this.itemCaches.put("categories", new Cache<Playlist>(this.view.itemsPerPage()));
        }
    }

    @Override
    public void execute() {
        if (this.authService.isAuthorized()) {
            if (this.itemCaches.get("categories")
                    .getItems()
                    .isEmpty()) {
                //noinspection unchecked
                this.itemCaches.get("categories")
                        .setItems(this.apiService.getCategories(this.authService.getAccessToken()));
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
