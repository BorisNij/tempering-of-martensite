package net.bnijik.spotify.explorer.service.commands;

/**
 * A supertype of all {@code Command}s of this app. <br>
 * Implemented by {@link AuthCommand}, {@link CategoriesCommand}, {@link CategoryPlaylistsCommand},
 * {@link FeaturedPlaylistsCommand}, {@link NewAlbumsCommand}, {@link NextPageCommand},
 * {@link PrevPageCommand}
 */
@FunctionalInterface
public interface SpotifyExplorerCommand {

    void execute();
}
