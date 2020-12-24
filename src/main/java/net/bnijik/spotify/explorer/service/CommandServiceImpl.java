package net.bnijik.spotify.explorer.service;

import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.service.commands.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Gets a {@link SpotifyExplorerCommand} from a dedicated hash map, or produces a new
 * {@link SpotifyExplorerCommand} and puts in the hash map if the {@code Command} was
 * called for the first time.
 */
public class CommandServiceImpl implements CommandService {
    private final UserConsoleService view;
    private final AuthSpotifyServiceImpl authSpotifyService;
    private final MusicSpotifyService musicSpotifyService;
    @SuppressWarnings("rawtypes")
    private final Map<String, MusicItemCache> itemCaches;

    private final Map<String, SpotifyExplorerCommand> commandMap;
    private SpotifyExplorerCommand command;

    public CommandServiceImpl(UserConsoleService view, AuthSpotifyServiceImpl authSpotifyService, MusicSpotifyService musicSpotifyService, @SuppressWarnings("rawtypes") Map<String, MusicItemCache> itemCaches) {
        this.view = view;
        this.authSpotifyService = authSpotifyService;
        this.musicSpotifyService = musicSpotifyService;
        this.commandMap = new HashMap<>();
        this.itemCaches = itemCaches;
    }

    @Override
    public SpotifyExplorerCommand provideAuthCommand() {
        if (commandMap.get("auth") != null) {
            command = commandMap.get("auth");
        } else {
            command = new AuthCommand(authSpotifyService, view);
            commandMap.put("auth", command);
        }
        return command;
    }

    @Override
    public SpotifyExplorerCommand provideNewAlbumsCommand() {
        if (commandMap.get("new") != null) {
            command = commandMap.get("new");
        } else {
            command = new NewAlbumsCommand(authSpotifyService, musicSpotifyService, view, itemCaches);
            commandMap.put("new", command);
        }
        return command;
    }

    @Override
    public SpotifyExplorerCommand provideFeaturedPlaylistsCommand() {
        if (commandMap.get("featured") != null) {
            command = commandMap.get("featured");
        } else {
            command = new FeaturedPlaylistsCommand(authSpotifyService, musicSpotifyService, view, itemCaches);
            commandMap.put("featured", command);
        }
        return command;
    }

    @Override
    public SpotifyExplorerCommand provideCategoriesCommand() {
        if (commandMap.get("categories") != null) {
            command = commandMap.get("categories");
        } else {
            command = new CategoriesCommand(authSpotifyService, musicSpotifyService, view, itemCaches);
            commandMap.put("categories", command);
        }
        return command;
    }

    @Override
    public SpotifyExplorerCommand provideCategoryPlaylistsCommand(String userInput) {
        if (commandMap.get(userInput) != null) {
            command = commandMap.get(userInput);
        } else {
            command = new CategoryPlaylistsCommand(authSpotifyService, musicSpotifyService, view, itemCaches, userInput);
            commandMap.put(userInput, command);
        }
        return command;
    }

    @Override
    public SpotifyExplorerCommand provideNextPageCommand() {
        if (commandMap.get("next") != null) {
            command = commandMap.get("next");
        } else {
            command = new NextPageCommand(authSpotifyService, view, itemCaches);
            commandMap.put("next", command);
        }
        return command;
    }

    @Override
    public SpotifyExplorerCommand providePrevPageCommand() {
        if (commandMap.get("prev") != null) {
            command = commandMap.get("prev");
        } else {
            command = new PrevPageCommand(authSpotifyService, view, itemCaches);
            commandMap.put("prev", command);
        }
        return command;
    }

    @Override
    public SpotifyExplorerCommand provideInvalidCommand() {
        return () -> view.errorMsg("Invalid command. Please try again.");
    }
}
