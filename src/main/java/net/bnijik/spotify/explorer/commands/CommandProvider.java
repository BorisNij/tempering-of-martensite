package net.bnijik.spotify.explorer.commands;

import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.service.AuthSpotifyServiceImpl;
import net.bnijik.spotify.explorer.service.MusicSpotifyService;
import net.bnijik.spotify.explorer.service.UserConsoleService;

import java.util.HashMap;
import java.util.Map;

/**
 * Gets a {@link SpotifyExplorerCommand} from a dedicated hash map, or produces a new
 * {@link SpotifyExplorerCommand} and puts in the hash map if the {@code Command} was
 * called for the first time.
 */
public class CommandProvider {
    private final UserConsoleService view;
    private final AuthSpotifyServiceImpl authSpotifyService;
    private final MusicSpotifyService musicSpotifyService;
    @SuppressWarnings("rawtypes")
    private final Map<String, MusicItemCache> itemCaches;

    private final Map<String, SpotifyExplorerCommand> commandMap;
    private SpotifyExplorerCommand command;

    public CommandProvider(UserConsoleService view, AuthSpotifyServiceImpl authSpotifyService, MusicSpotifyService musicSpotifyService, @SuppressWarnings("rawtypes") Map<String, MusicItemCache> itemCaches) {
        this.view = view;
        this.authSpotifyService = authSpotifyService;
        this.musicSpotifyService = musicSpotifyService;
        this.commandMap = new HashMap<>();
        this.itemCaches = itemCaches;
    }

    public SpotifyExplorerCommand provideAuthCommand() {
        if (this.commandMap.get("auth") != null) {
            this.command = this.commandMap.get("auth");
        } else {
            this.command = new AuthCommand(this.authSpotifyService, this.view);
            this.commandMap.put("auth", command);
        }
        return this.command;
    }

    public SpotifyExplorerCommand provideNewAlbumsCommand() {
        if (this.commandMap.get("new") != null) {
            this.command = this.commandMap.get("new");
        } else {
            this.command = new NewAlbumsCommand(this.authSpotifyService, this.musicSpotifyService, this.view, this.itemCaches);
            this.commandMap.put("new", command);
        }
        return this.command;
    }

    public SpotifyExplorerCommand provideFeaturedPlaylistsCommand() {
        if (this.commandMap.get("featured") != null) {
            this.command = this.commandMap.get("featured");
        } else {
            this.command = new FeaturedPlaylistsCommand(this.authSpotifyService, this.musicSpotifyService, this.view, this.itemCaches);
            this.commandMap.put("featured", command);
        }
        return this.command;
    }

    public SpotifyExplorerCommand provideCategoriesCommand() {
        if (this.commandMap.get("categories") != null) {
            this.command = this.commandMap.get("categories");
        } else {
            this.command = new CategoriesCommand(this.authSpotifyService, this.musicSpotifyService, this.view, this.itemCaches);
            this.commandMap.put("categories", command);
        }
        return this.command;
    }

    public SpotifyExplorerCommand provideCategoryPlaylistsCommand(String userInput) {
        if (this.commandMap.get(userInput) != null) {
            this.command = this.commandMap.get(userInput);
        } else {
            this.command = new CategoryPlaylistsCommand(this.authSpotifyService, this.musicSpotifyService, this.view, this.itemCaches, userInput);
            this.commandMap.put(userInput, command);
        }
        return this.command;
    }

    public SpotifyExplorerCommand provideNextPageCommand() {
        if (this.commandMap.get("next") != null) {
            this.command = this.commandMap.get("next");
        } else {
            this.command = new NextPageCommand(this.authSpotifyService, this.view, this.itemCaches);
            this.commandMap.put("next", command);
        }
        return this.command;
    }

    public SpotifyExplorerCommand providePrevPageCommand() {
        if (this.commandMap.get("prev") != null) {
            this.command = this.commandMap.get("prev");
        } else {
            this.command = new PrevPageCommand(this.authSpotifyService, this.view, this.itemCaches);
            this.commandMap.put("prev", command);
        }
        return this.command;
    }

    public SpotifyExplorerCommand provideInvalidCommand() {
        return () -> this.view.errorMsg("Invalid command. Please try again.");
    }
}
