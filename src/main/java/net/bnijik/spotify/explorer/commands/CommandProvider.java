package net.bnijik.spotify.explorer.commands;

import net.bnijik.spotify.explorer.AuthSpotifyService;
import net.bnijik.spotify.explorer.Cache;
import net.bnijik.spotify.explorer.MusicSpotifyService;
import net.bnijik.spotify.explorer.UserConsole;

import java.util.HashMap;
import java.util.Map;

/**
 * Gets a {@link SpotifyExplorerCommand} from a dedicated hash map, or produces a new
 * {@link SpotifyExplorerCommand} and puts in the hash map if the {@code Command} was
 * called for the first time.
 */
public class CommandProvider {
    private final UserConsole view;
    private final AuthSpotifyService authSpotifyService;
    private final MusicSpotifyService musicSpotifyService;
    @SuppressWarnings("rawtypes")
    private final Map<String, Cache> itemCaches;

    private final Map<String, SpotifyExplorerCommand> commandMap;
    private SpotifyExplorerCommand command;

    public CommandProvider(UserConsole view, AuthSpotifyService authSpotifyService, MusicSpotifyService musicSpotifyService, @SuppressWarnings("rawtypes") Map<String, Cache> itemCaches) {
        this.view = view;
        this.authSpotifyService = authSpotifyService;
        this.musicSpotifyService = musicSpotifyService;
        this.commandMap = new HashMap<>();
        this.itemCaches = itemCaches;
    }

    public SpotifyExplorerCommand provideAuthCommand() {
        if (this.commandMap.get("auth") != null) {
            command = this.commandMap.get("auth");
        } else {
            command = new AuthCommand(this.authSpotifyService, this.view);
            this.commandMap.put("auth", command);
        }
        return command;
    }

    public SpotifyExplorerCommand provideNewAlbumsCommand() {
        if (this.commandMap.get("new") != null) {
            command = this.commandMap.get("new");
        } else {
            command = new NewAlbumsCommand(this.authSpotifyService, this.musicSpotifyService, this.view, this.itemCaches);
            this.commandMap.put("new", command);
        }
        return command;
    }

    public SpotifyExplorerCommand provideFeaturedPlaylistsCommand() {
        if (this.commandMap.get("featured") != null) {
            command = this.commandMap.get("featured");
        } else {
            command = new FeaturedPlaylistsCommand(this.authSpotifyService, this.musicSpotifyService, this.view, this.itemCaches);
            this.commandMap.put("featured", command);
        }
        return command;
    }

    public SpotifyExplorerCommand provideCategoriesCommand() {
        if (this.commandMap.get("categories") != null) {
            command = this.commandMap.get("categories");
        } else {
            command = new CategoriesCommand(this.authSpotifyService, this.musicSpotifyService, this.view, this.itemCaches);
            this.commandMap.put("categories", command);
        }
        return command;
    }

    public SpotifyExplorerCommand provideCategoryPlaylistsCommand(String userInput) {
        if (this.commandMap.get(userInput) != null) {
            command = this.commandMap.get(userInput);
        } else {
            command = new CategoryPlaylistsCommand(this.authSpotifyService, this.musicSpotifyService, this.view, this.itemCaches, userInput);
            this.commandMap.put(userInput, command);
        }
        return command;
    }

    public SpotifyExplorerCommand provideNextPageCommand() {
        if (this.commandMap.get("next") != null) {
            command = this.commandMap.get("next");
        } else {
            command = new NextPageCommand(this.authSpotifyService, this.view, this.itemCaches);
            this.commandMap.put("next", command);
        }
        return command;
    }

    public SpotifyExplorerCommand providePrevPageCommand() {
        if (this.commandMap.get("prev") != null) {
            command = this.commandMap.get("prev");
        } else {
            command = new PrevPageCommand(this.authSpotifyService, this.view, this.itemCaches);
            this.commandMap.put("prev", command);
        }
        return command;
    }

    public SpotifyExplorerCommand provideInvalidCommand() {
        return () -> this.view.errorMsg("Invalid command. Please try again.");
    }
}
