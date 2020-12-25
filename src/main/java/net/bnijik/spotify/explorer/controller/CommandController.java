package net.bnijik.spotify.explorer.controller;

import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.service.AuthSpotifyService;
import net.bnijik.spotify.explorer.service.MusicSpotifyService;
import net.bnijik.spotify.explorer.service.UserConsoleService;
import net.bnijik.spotify.explorer.service.commands.*;
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
 * </ul>
 */
@Component
public class CommandController {
    private final UserConsoleService view;
    private final AuthSpotifyService authSpotifyService;
    private final MusicSpotifyService musicSpotifyService;

    @SuppressWarnings("rawtypes")
    private final Map<String, MusicItemCache> itemCacheMap;
    private final Map<String, SpotifyExplorerCommand> commandMap;


    @Autowired
    public CommandController(UserConsoleService view, AuthSpotifyService authSpotifyService, MusicSpotifyService musicSpotifyService) {
        this.authSpotifyService = Objects.requireNonNull(authSpotifyService);
        this.musicSpotifyService = Objects.requireNonNull(musicSpotifyService);
        this.view = Objects.requireNonNull(view);

        this.commandMap = new HashMap<>();
        this.itemCacheMap = new HashMap<>();
        //noinspection rawtypes
        itemCacheMap.put("nowShowing", new MusicItemCache(view.itemsPerPage()));
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
                    command = commandMap.computeIfAbsent(commandStrings[0], (authCommand) -> new AuthCommand(authSpotifyService, view));
                    break;

                case "NEW":
                    command = commandMap.computeIfAbsent(commandStrings[0], (newAlbumsCommand) -> new NewAlbumsCommand(authSpotifyService, musicSpotifyService, view, itemCacheMap));
                    break;

                case "FEATURED":
                    command = commandMap.computeIfAbsent(commandStrings[0], (featuredPlaylistsCommand) -> new FeaturedPlaylistsCommand(authSpotifyService, musicSpotifyService, view, itemCacheMap));
                    break;

                case "CATEGORIES":
                    command = commandMap.computeIfAbsent(commandStrings[0], (categoriesCommand) -> new CategoriesCommand(authSpotifyService, musicSpotifyService, view, itemCacheMap));
                    break;

                case "PLAYLISTS":
                    command = commandMap.computeIfAbsent(commandString, (categoryPlaylistsCommand) -> new CategoryPlaylistsCommand(authSpotifyService, musicSpotifyService, view, itemCacheMap, commandString));
                    break;

                case "NEXT":
                    command = commandMap.computeIfAbsent(commandStrings[0], (nextPageCommand) -> new NextPageCommand(authSpotifyService, view, itemCacheMap));
                    break;

                case "PREV":
                    command = commandMap.computeIfAbsent(commandStrings[0], (prevPageCommand) -> new PrevPageCommand(authSpotifyService, view, itemCacheMap));
                    break;

                case "EXIT":
                    view.display("Terminating...");
                    return;

                default:
                    command = () -> view.errorMsg("Invalid command. Please try again.");
            }
            command.execute();

        }
    }
}
