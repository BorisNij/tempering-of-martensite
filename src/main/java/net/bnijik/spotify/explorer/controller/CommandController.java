package net.bnijik.spotify.explorer.controller;

import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.service.*;
import net.bnijik.spotify.explorer.service.commands.NextPageCommand;
import net.bnijik.spotify.explorer.service.commands.PrevPageCommand;
import net.bnijik.spotify.explorer.service.commands.SpotifyExplorerCommand;
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
 *     <li> A {@link SpotifyExplorerCommandService}</li>
 * </ul>
 */
@Component
public class CommandController {
    private final UserConsoleService view;
    private final CommandService<SpotifyExplorerCommand> commandService;

    @Autowired
    public CommandController(UserConsoleService view, AuthSpotifyServiceImpl authSpotifyService, MusicSpotifyService musicSpotifyService) {
        //noinspection rawtypes
        Map<String, MusicItemCache> itemCaches = new HashMap<>();
        //noinspection rawtypes
        itemCaches.put("nowShowing", new MusicItemCache(view.itemsPerPage()));
        this.commandService = new SpotifyExplorerCommandService(view, authSpotifyService, musicSpotifyService, itemCaches);
        this.view = Objects.requireNonNull(view);
    }

    public void start() {
        view.display("Greetings! :)");
        view.display("This is your Spotify Explorer");
        SpotifyExplorerCommand command;

        while (true) {
            final String commandString = view.getUserCommandString();
            final String[] commandStrings = commandString.toUpperCase()
                    .split("\\s");

            switch (commandStrings[0]) {
                case "AUTH":
                    command = commandService.provideAuthCommand(commandStrings[0]);
                    break;

                case "NEW":
                    command = commandService.provideNewAlbumsCommand(commandStrings[0]);
                    break;

                case "FEATURED":
                    command = commandService.provideFeaturedPlaylistsCommand(commandStrings[0]);
                    break;

                case "CATEGORIES":
                    command = commandService.provideCategoriesCommand(commandStrings[0]);
                    break;

                case "PLAYLISTS":
                    command = commandService.provideCategoryPlaylistsCommand(commandString);
                    break;

                case "NEXT":
                    command = commandService.provideNextPageCommand(commandStrings[0]);
                    break;

                case "PREV":
                    command = commandService.providePrevPageCommand(commandStrings[0]);
                    break;

                case "EXIT":
                    view.display("Terminating...");
                    return;

                default:
                    command = commandService.provideInvalidCommand("INVALID");
            }
            command.execute();

        }
    }
}
