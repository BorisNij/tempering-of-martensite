package niji.kovsky.bn.spotify.explorer.commands;

import niji.kovsky.bn.spotify.explorer.ApiService;
import niji.kovsky.bn.spotify.explorer.AuthService;
import niji.kovsky.bn.spotify.explorer.Cache;
import niji.kovsky.bn.spotify.explorer.UserConsole;
import niji.kovsky.bn.spotify.explorer.model.Category;
import niji.kovsky.bn.spotify.explorer.model.Playlist;

import java.util.Map;

@SuppressWarnings("rawtypes")
public class CategoryPlaylistsCommand implements SpotifyExplorerCommand {
    private final AuthService authService;
    private final ApiService apiService;
    private final UserConsole view;
    private final String commandString;
    private final Map<String, Cache> itemCaches;

    public CategoryPlaylistsCommand(AuthService authService, ApiService apiService, UserConsole view, Map<String, Cache> itemCaches, String commandString) {
        this.authService = authService;
        this.apiService = apiService;
        this.view = view;
        this.commandString = commandString;
        this.itemCaches = itemCaches;

        if (this.itemCaches.get(commandString) == null) {
            this.itemCaches.put(commandString, new Cache<Playlist>(this.view.itemsPerPage()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute() {
        if (this.authService.isAuthorized()) {
            if (this.itemCaches.get(commandString)
                    .getItems()
                    .isEmpty()) {

                final Cache<Category> categoryCache = this.itemCaches.get("categories");
                final String soughtCategory = commandString.substring(commandString.indexOf(' ') + 1);
                Category category = categoryCache.getByName(soughtCategory);

                if (category == null) {
                    this.view.errorMsg("Cannot find a Category with the name " + soughtCategory + ". Please try again.");
                    return;
                }

                this.itemCaches.get(commandString)
                        .setItems(this.apiService.getPlaylists(category, this.authService.getAccessToken()));
            }
            this.itemCaches.put("nowShowing", this.itemCaches.get(commandString));
            this.view.display(this.itemCaches.get("nowShowing")
                                      .currentPage());
        } else {
            this.view.display("Please authenticate with Spotify first ('auth')");
        }
    }
}
