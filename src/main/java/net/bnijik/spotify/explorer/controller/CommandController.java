package net.bnijik.spotify.explorer.controller;

import net.bnijik.spotify.explorer.commands.CommandProvider;
import net.bnijik.spotify.explorer.commands.NextPageCommand;
import net.bnijik.spotify.explorer.commands.PrevPageCommand;
import net.bnijik.spotify.explorer.commands.SpotifyExplorerCommand;
import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.service.AuthSpotifyServiceImpl;
import net.bnijik.spotify.explorer.service.MusicSpotifyService;
import net.bnijik.spotify.explorer.service.UserConsoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Processes user command-strings received via the View into {@link SpotifyExplorerCommand} objects.
 * Passes the View, Service and music item {@code MusicItemCache} objects
 * to the {@code SpotifyExplorerCommand} as arguments. Executes the {@code SpotifyExplorerCommand}
 * that corresponds to the user command-string.<p>
 * <p>
 * Instantiates and maintains the following objects:
 * <ul>
 *     <li> A hash map of (Album, Category or Playlist) music item Caches</li>
 *     <li> A <i>now-showing</i> MusicItemCache entry in the hash map holding the cache that currently
 *          presents its items to the user. The {@link NextPageCommand} and
 *          {@link PrevPageCommand} update the {@code MusicItemPage} of this cache</li>
 *     <li> A {@link CommandProvider}</li>
 * </ul>
 */
@Component
public class CommandController {
    private final UserConsoleService view;
    private final CommandProvider commandProvider;

    @Autowired
    public CommandController(UserConsoleService view, AuthSpotifyServiceImpl authSpotifyService, MusicSpotifyService musicSpotifyService) {
        //noinspection rawtypes
        Map<String, MusicItemCache> itemCaches = new HashMap<>();
        //noinspection rawtypes
        itemCaches.put("nowShowing", new MusicItemCache(view.itemsPerPage()));
        this.commandProvider = new CommandProvider(view, authSpotifyService, musicSpotifyService, itemCaches);
        this.view = Objects.requireNonNull(view);
    }

    public void start() {
        view.display("Greetings! :)");
        view.display("This is your Spotify Explorer");
        SpotifyExplorerCommand command;

        while (true) {
            String commandString = view.getUserCommandString();

            switch (commandString.toUpperCase()
                    .split("\\s")[0]) {
                case "AUTH":
                    command = commandProvider.provideAuthCommand();
                    break;

                case "NEW":
                    command = commandProvider.provideNewAlbumsCommand();
                    break;

                case "FEATURED":
                    command = commandProvider.provideFeaturedPlaylistsCommand();
                    break;

                case "CATEGORIES":
                    command = commandProvider.provideCategoriesCommand();
                    break;

                case "PLAYLISTS":
                    command = commandProvider.provideCategoryPlaylistsCommand(commandString);
                    break;

                case "NEXT":
                    command = commandProvider.provideNextPageCommand();
                    break;

                case "PREV":
                    command = commandProvider.providePrevPageCommand();
                    break;

                case "EXIT":
                    view.display("Terminating...");
                    return;

                default:
                    command = commandProvider.provideInvalidCommand();
            }
            command.execute();

        }
    }
}
