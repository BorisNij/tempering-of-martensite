package niji.kovsky.bn.spotify.explorer;

import niji.kovsky.bn.spotify.explorer.commands.CommandProvider;
import niji.kovsky.bn.spotify.explorer.commands.SpotifyExplorerCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Processes user command-strings received via the View into {@link SpotifyExplorerCommand} objects.
 * Passes the View, Service and music item {@code Cache} objects
 * to the {@code SpotifyExplorerCommand} as arguments. Executes the {@code SpotifyExplorerCommand}
 * that corresponds to the user command-string.<p>
 * <p>
 * Instantiates and maintains the following objects:
 * <ul>
 *     <li> A hash map of (Album, Category or Playlist) music item Caches</li>
 *     <li> A <i>now-showing</i> Cache entry in the hash map holding the cache that currently
 *          presents its items to the user. The {@link niji.kovsky.bn.spotify.explorer.commands.NextPageCommand} and
 *          {@link niji.kovsky.bn.spotify.explorer.commands.PrevPageCommand} update the {@code Page} of this cache</li>
 *     <li> A {@link CommandProvider}</li>
 * </ul>
 */
public class CommandController {
    private final UserConsole view;
    private final CommandProvider commandProvider;

    public CommandController(UserConsole view, AuthSpotifyService authSpotifyService, MusicSpotifyService musicSpotifyService) {
        //noinspection rawtypes
        Map<String, Cache> itemCaches = new HashMap<>();
        //noinspection rawtypes
        itemCaches.put("nowShowing", new Cache(view.itemsPerPage()));
        this.commandProvider = new CommandProvider(view, authSpotifyService, musicSpotifyService, itemCaches);
        this.view = Objects.requireNonNull(view);
    }

    public void start() {
        this.view.display("Greetings! :)");
        this.view.display("This is your Spotify Explorer");
        SpotifyExplorerCommand command;

        while (true) {
            String commandString = this.view.getUserCommandString();

            switch (commandString.toUpperCase()
                    .split(" ")[0]) {
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
                    this.view.display("Terminating...");
                    return;

                default:
                    command = commandProvider.provideInvalidCommand();
            }
            command.execute();

        }
    }
}
