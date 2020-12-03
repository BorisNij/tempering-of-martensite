package com.kovsky.bn.commands;

import com.kovsky.bn.ApiService;
import com.kovsky.bn.AuthService;
import com.kovsky.bn.Cache;
import com.kovsky.bn.UserConsole;
import com.kovsky.bn.model.Playlist;

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
    @SuppressWarnings("unchecked")
    public void execute() {
        if (this.authService.isAuthorized()) {
            if (this.itemCaches.get("featured")
                    .getItems()
                    .isEmpty()) {
                this.itemCaches.get("featured")
                        .setItems(this.apiService.getPlaylists(this.authService.getAccessToken()));
            }
            this.itemCaches.put("nowShowing", this.itemCaches.get("featured"));
            this.view.display(this.itemCaches.get("nowShowing")
                                      .currentPage());
        } else {
            this.view.display("Please authenticate with Spotify first ('auth')");
        }
    }
}
