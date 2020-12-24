package net.bnijik.spotify.explorer.service;

import net.bnijik.spotify.explorer.service.commands.SpotifyExplorerCommand;

public interface CommandService {
    SpotifyExplorerCommand provideAuthCommand();

    SpotifyExplorerCommand provideNewAlbumsCommand();

    SpotifyExplorerCommand provideFeaturedPlaylistsCommand();

    SpotifyExplorerCommand provideCategoriesCommand();

    SpotifyExplorerCommand provideCategoryPlaylistsCommand(String userInput);

    SpotifyExplorerCommand provideNextPageCommand();

    SpotifyExplorerCommand providePrevPageCommand();

    SpotifyExplorerCommand provideInvalidCommand();
}
