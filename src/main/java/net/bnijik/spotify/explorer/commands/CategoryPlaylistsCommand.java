package net.bnijik.spotify.explorer.commands;

import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.model.Category;
import net.bnijik.spotify.explorer.model.Playlist;
import net.bnijik.spotify.explorer.service.AuthSpotifyServiceImpl;
import net.bnijik.spotify.explorer.service.MusicSpotifyService;
import net.bnijik.spotify.explorer.service.UserConsoleService;

import java.util.Map;

public class CategoryPlaylistsCommand implements SpotifyExplorerCommand {
    private final AuthSpotifyServiceImpl authSpotifyService;
    private final MusicSpotifyService musicSpotifyService;
    private final UserConsoleService view;
    private final String commandString;
    @SuppressWarnings("rawtypes")
    private final Map<String, MusicItemCache> itemCaches;

    public CategoryPlaylistsCommand(AuthSpotifyServiceImpl authSpotifyService, MusicSpotifyService musicSpotifyService, UserConsoleService view, @SuppressWarnings("rawtypes") Map<String, MusicItemCache> itemCaches, String commandString) {
        this.authSpotifyService = authSpotifyService;
        this.musicSpotifyService = musicSpotifyService;
        this.view = view;
        this.commandString = commandString;
        this.itemCaches = itemCaches;

        if (itemCaches.get(commandString) == null) {
            itemCaches.put(commandString, new MusicItemCache<Playlist>(view.itemsPerPage()));
        }
    }

    @Override
    public void execute() {
        if (authSpotifyService.isAuthorized()) {
            if (itemCaches.get(commandString)
                    .getItems()
                    .isEmpty()) {

                //noinspection unchecked
                final MusicItemCache<Category> categoryMusicItemCache = itemCaches.get("categories");
                final String soughtCategory = commandString.substring(commandString.indexOf(' ') + 1);
                Category category = categoryMusicItemCache.getByName(soughtCategory);

                if (category == null) {
                    view.errorMsg("Cannot find a Category with the name " + soughtCategory + ". Please try again.");
                    return;
                }

                //noinspection unchecked
                itemCaches.get(commandString)
                        .setItems(musicSpotifyService.getPlaylists(category, authSpotifyService.getAccessToken()));
            }
            itemCaches.put("nowShowing", itemCaches.get(commandString));
            //noinspection unchecked
            view.display(itemCaches.get("nowShowing")
                                 .currentPage());
        } else {
            view.display("Please authenticate with Spotify first ('auth')");
        }
    }
}
