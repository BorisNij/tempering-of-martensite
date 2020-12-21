package net.bnijik.spotify.explorer.commands;

import net.bnijik.spotify.explorer.AuthSpotifyService;
import net.bnijik.spotify.explorer.Cache;
import net.bnijik.spotify.explorer.MusicSpotifyService;
import net.bnijik.spotify.explorer.UserConsole;
import net.bnijik.spotify.explorer.model.Category;
import net.bnijik.spotify.explorer.model.Playlist;

import java.util.Map;

public class CategoryPlaylistsCommand implements SpotifyExplorerCommand {
    private final AuthSpotifyService authSpotifyService;
    private final MusicSpotifyService musicSpotifyService;
    private final UserConsole view;
    private final String commandString;
    @SuppressWarnings("rawtypes")
    private final Map<String, Cache> itemCaches;

    public CategoryPlaylistsCommand(AuthSpotifyService authSpotifyService, MusicSpotifyService musicSpotifyService, UserConsole view, @SuppressWarnings("rawtypes") Map<String, Cache> itemCaches, String commandString) {
        this.authSpotifyService = authSpotifyService;
        this.musicSpotifyService = musicSpotifyService;
        this.view = view;
        this.commandString = commandString;
        this.itemCaches = itemCaches;

        if (this.itemCaches.get(commandString) == null) {
            this.itemCaches.put(commandString, new Cache<Playlist>(this.view.itemsPerPage()));
        }
    }

    @Override
    public void execute() {
        if (this.authSpotifyService.isAuthorized()) {
            if (this.itemCaches.get(commandString)
                    .getItems()
                    .isEmpty()) {

                //noinspection unchecked
                final Cache<Category> categoryCache = this.itemCaches.get("categories");
                final String soughtCategory = commandString.substring(commandString.indexOf(' ') + 1);
                Category category = categoryCache.getByName(soughtCategory);

                if (category == null) {
                    this.view.errorMsg("Cannot find a Category with the name " + soughtCategory + ". Please try again.");
                    return;
                }

                //noinspection unchecked
                this.itemCaches.get(commandString)
                        .setItems(this.musicSpotifyService.getPlaylists(category, this.authSpotifyService.getAccessToken()));
            }
            this.itemCaches.put("nowShowing", this.itemCaches.get(commandString));
            //noinspection unchecked
            this.view.display(this.itemCaches.get("nowShowing")
                                      .currentPage());
        } else {
            this.view.display("Please authenticate with Spotify first ('auth')");
        }
    }
}
