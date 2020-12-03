package com.kovsky.bn;

import com.kovsky.bn.commands.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("rawtypes")
public class Controller {
    private final UserConsole view;
    private final AuthService authService;
    private final ApiService apiService;

    private final Map<String, SpotifyExplorerCommand> commandMap;

    private final Map<String, Cache> itemCaches;

    public Controller(UserConsole view, AuthService authService, ApiService apiService) {
        this.view = Objects.requireNonNull(view);
        this.authService = Objects.requireNonNull(authService);
        this.apiService = Objects.requireNonNull(apiService);

        this.commandMap = new HashMap<>();
        this.itemCaches = new HashMap<>();

        this.itemCaches.put("nowShowing", new Cache(this.view.itemsPerPage()));
    }

    public void start() {
        this.view.display("Greetings! :)");
        this.view.display("This is your Spotify Explorer");
        SpotifyExplorerCommand invalidCommand = new InvalidCommand(this.view);
        SpotifyExplorerCommand command;

        while (true) {
            String commandString = this.view.getCommand();
            String[] userCommands = commandString
                    .split(" ");

            switch (userCommands[0].toLowerCase()) {
                case "auth":
                    if (this.commandMap.get("auth") != null) {
                        command = this.commandMap.get("auth");
                    } else {
                        command = new AuthCommand(this.authService, this.view);
                        this.commandMap.put("auth", command);
                    }
                    break;

                case "new":
                    if (this.commandMap.get("new") != null) {
                        command = this.commandMap.get("new");
                    } else {
                        command = new NewAlbumsCommand(this.authService, this.apiService, this.view, this.itemCaches);
                        this.commandMap.put("new", command);
                    }
                    break;

                case "featured":
                    if (this.commandMap.get("featured") != null) {
                        command = this.commandMap.get("featured");
                    } else {
                        command = new FeaturedPlaylistsCommand(this.authService, this.apiService, this.view, this.itemCaches);
                        this.commandMap.put("featured", command);
                    }
                    break;

                case "categories":
                    if (this.commandMap.get("categories") != null) {
                        command = this.commandMap.get("categories");
                    } else {
                        command = new CategoriesCommand(this.authService, this.apiService, this.view, this.itemCaches);
                        this.commandMap.put("categories", command);
                    }
                    break;

                case "playlists":
                    if (this.commandMap.get(commandString) != null) {
                        command = this.commandMap.get(commandString);
                    } else {
                        command = new CategoryPlaylistsCommand(this.authService, this.apiService, this.view, this.itemCaches, commandString);
                        this.commandMap.put(commandString, command);
                    }
                    break;

                case "next":
                    if (this.commandMap.get("next") != null) {
                        command = this.commandMap.get("next");
                    } else {
                        command = new NextPageCommand(this.authService, this.view, this.itemCaches);
                        this.commandMap.put("next", command);
                    }
                    break;

                case "prev":
                    if (this.commandMap.get("prev") != null) {
                        command = this.commandMap.get("prev");
                    } else {
                        command = new PrevPageCommand(this.authService, this.view, this.itemCaches);
                        this.commandMap.put("prev", command);
                    }
                    break;

                case "exit":
                    this.view.display("Terminating...");
                    return;

                default:
                    command = invalidCommand;
            }
            command.execute();

        }
    }
}
