package net.bnijik.spotify.explorer.service;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandService<Command> {
    private final Map<String, Command> commandMap = new HashMap<>();

    public Map<String, Command> commandMap() {
        return commandMap;
    }

    public abstract Command provideAuthCommand(String commandId);

    public abstract Command provideNewAlbumsCommand(String commandId);

    public abstract Command provideFeaturedPlaylistsCommand(String commandId);

    public abstract Command provideCategoriesCommand(String commandId);

    public abstract Command provideCategoryPlaylistsCommand(String commandId);

    public abstract Command provideNextPageCommand(String commandId);

    public abstract Command providePrevPageCommand(String commandId);

    public abstract Command provideInvalidCommand(String commandId);
}
