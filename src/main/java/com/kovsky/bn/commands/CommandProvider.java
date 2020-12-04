package com.kovsky.bn.commands;

import com.kovsky.bn.ApiService;
import com.kovsky.bn.AuthService;
import com.kovsky.bn.Cache;
import com.kovsky.bn.UserConsole;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class CommandProvider {
    private final UserConsole view;
    private final AuthService authService;
    private final ApiService apiService;
    private final Map<String, Cache> itemCaches;

    private final Map<String, SpotifyExplorerCommand> commandMap;
    private SpotifyExplorerCommand command;

    public CommandProvider(UserConsole view, AuthService authService, ApiService apiService, Map<String, Cache> itemCaches) {
        this.view = view;
        this.authService = authService;
        this.apiService = apiService;
        this.commandMap = new HashMap<>();
        this.itemCaches = itemCaches;
    }

    public SpotifyExplorerCommand provideAuthCommand() {
        if (this.commandMap.get("auth") != null) {
            command = this.commandMap.get("auth");
        } else {
            command = new AuthCommand(this.authService, this.view);
            this.commandMap.put("auth", command);
        }
        return command;
    }

    public SpotifyExplorerCommand provideNewAlbumsCommand() {
        if (this.commandMap.get("new") != null) {
            command = this.commandMap.get("new");
        } else {
            command = new NewAlbumsCommand(this.authService, this.apiService, this.view, this.itemCaches);
            this.commandMap.put("new", command);
        }
        return command;
    }

    public SpotifyExplorerCommand provideFeaturedPlaylistsCommand() {
        if (this.commandMap.get("featured") != null) {
            command = this.commandMap.get("featured");
        } else {
            command = new FeaturedPlaylistsCommand(this.authService, this.apiService, this.view, this.itemCaches);
            this.commandMap.put("featured", command);
        }
        return command;
    }

    public SpotifyExplorerCommand provideCategoriesCommand() {
        if (this.commandMap.get("categories") != null) {
            command = this.commandMap.get("categories");
        } else {
            command = new CategoriesCommand(this.authService, this.apiService, this.view, this.itemCaches);
            this.commandMap.put("categories", command);
        }
        return command;
    }

    public SpotifyExplorerCommand provideCategoryPlaylistsCommand(String userInput) {
        if (this.commandMap.get(userInput) != null) {
            command = this.commandMap.get(userInput);
        } else {
            command = new CategoryPlaylistsCommand(this.authService, this.apiService, this.view, this.itemCaches, userInput);
            this.commandMap.put(userInput, command);
        }
        return command;
    }

    public SpotifyExplorerCommand provideNextPageCommand() {
        if (this.commandMap.get("next") != null) {
            command = this.commandMap.get("next");
        } else {
            command = new NextPageCommand(this.authService, this.view, this.itemCaches);
            this.commandMap.put("next", command);
        }
        return command;
    }

    public SpotifyExplorerCommand providePrevPageCommand() {
        if (this.commandMap.get("prev") != null) {
            command = this.commandMap.get("prev");
        } else {
            command = new PrevPageCommand(this.authService, this.view, this.itemCaches);
            this.commandMap.put("prev", command);
        }
        return command;
    }

    public SpotifyExplorerCommand provideInvalidCommand() {
        if (this.commandMap.get("invalidCommand") != null) {
            command = this.commandMap.get("invalidCommand");
        } else {
            command = new InvalidCommand(this.view);
            this.commandMap.put("invalidCommand", command);
        }
        return command;
    }
}
