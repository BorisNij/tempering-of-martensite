package net.bnijik.spotify.explorer.service;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandProvider<C> {
    private final Map<String, C> commandMap = new HashMap<>();

    public Map<String, C> commandMap() {
        return commandMap;
    }

    public abstract C provideAuthCommand(String commandId);

    public abstract C provideNewAlbumsCommand(String commandId);

    public abstract C provideFeaturedPlaylistsCommand(String commandId);

    public abstract C provideCategoriesCommand(String commandId);

    public abstract C provideCategoryPlaylistsCommand(String commandId);

    public abstract C provideNextPageCommand(String commandId);

    public abstract C providePrevPageCommand(String commandId);

    public abstract C provideInvalidCommand(String commandId);
}
