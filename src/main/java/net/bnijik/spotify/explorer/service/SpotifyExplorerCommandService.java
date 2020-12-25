package net.bnijik.spotify.explorer.service;

import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.service.commands.*;

import java.util.Map;

/**
 * Gets a {@link SpotifyExplorerCommand} from a dedicated hash map, or produces a new
 * {@link SpotifyExplorerCommand} and puts in the hash map if the {@code Command} was
 * called for the first time.
 */
public class SpotifyExplorerCommandService extends CommandService<SpotifyExplorerCommand> {
    private final UserConsoleService view;
    private final AuthSpotifyServiceImpl authSpotifyService;
    private final MusicSpotifyService musicSpotifyService;

    @SuppressWarnings("rawtypes")
    private final Map<String, MusicItemCache> itemCaches;

    public SpotifyExplorerCommandService(UserConsoleService view, AuthSpotifyServiceImpl authSpotifyService, MusicSpotifyService musicSpotifyService, @SuppressWarnings("rawtypes") Map<String, MusicItemCache> itemCaches) {
        this.view = view;
        this.authSpotifyService = authSpotifyService;
        this.musicSpotifyService = musicSpotifyService;
        this.itemCaches = itemCaches;
    }

    @Override
    public SpotifyExplorerCommand provideAuthCommand(String commandId) {
        return super.commandMap()
                .computeIfAbsent(commandId, (authCommand) -> new AuthCommand(authSpotifyService, view));
    }

    @Override
    public SpotifyExplorerCommand provideNewAlbumsCommand(String commandId) {
        return super.commandMap()
                .computeIfAbsent(commandId, (newAlbumsCommand) -> new NewAlbumsCommand(authSpotifyService, musicSpotifyService, view, itemCaches));
    }

    @Override
    public SpotifyExplorerCommand provideFeaturedPlaylistsCommand(String commandId) {
        return super.commandMap()
                .computeIfAbsent(commandId, (featuredPlaylistsCommand) -> new FeaturedPlaylistsCommand(authSpotifyService, musicSpotifyService, view, itemCaches));
    }

    @Override
    public SpotifyExplorerCommand provideCategoriesCommand(String commandId) {
        return super.commandMap()
                .computeIfAbsent(commandId, (categoriesCommand) -> new CategoriesCommand(authSpotifyService, musicSpotifyService, view, itemCaches));
    }

    @Override
    public SpotifyExplorerCommand provideCategoryPlaylistsCommand(String commandId) {
        return super.commandMap()
                .computeIfAbsent(commandId, (categoryPlaylistsCommand) -> new CategoryPlaylistsCommand(authSpotifyService, musicSpotifyService, view, itemCaches, commandId));
    }

    @Override
    public SpotifyExplorerCommand provideNextPageCommand(String commandId) {
        return super.commandMap()
                .computeIfAbsent(commandId, (nextPageCommand) -> new NextPageCommand(authSpotifyService, view, itemCaches));
    }

    @Override
    public SpotifyExplorerCommand providePrevPageCommand(String commandId) {
        return super.commandMap()
                .computeIfAbsent(commandId, (prevPageCommand) -> new PrevPageCommand(authSpotifyService, view, itemCaches));
    }

    @Override
    public SpotifyExplorerCommand provideInvalidCommand(String commandId) {
        return () -> view.errorMsg("Invalid command. Please try again.");
    }
}
