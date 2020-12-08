package nij.ikovsky.bn;

import nij.ikovsky.bn.commands.CommandProvider;
import nij.ikovsky.bn.commands.SpotifyExplorerCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("rawtypes")
public class Controller {
    private final UserConsole view;
    private final CommandProvider commandProvider;

    public Controller(UserConsole view, AuthService authService, ApiService apiService) {
        Map<String, Cache> itemCaches = new HashMap<>();
        itemCaches.put("nowShowing", new Cache(view.itemsPerPage()));
        this.commandProvider = new CommandProvider(view, authService, apiService, itemCaches);
        this.view = Objects.requireNonNull(view);
    }

    public void start() {
        this.view.display("Greetings! :)");
        this.view.display("This is your Spotify Explorer");
        SpotifyExplorerCommand command;

        while (true) {
            String commandString = this.view.getCommand();

            switch (commandString.toLowerCase()
                    .split(" ")[0]) {
                case "auth":
                    command = commandProvider.provideAuthCommand();
                    break;

                case "new":
                    command = commandProvider.provideNewAlbumsCommand();
                    break;

                case "featured":
                    command = commandProvider.provideFeaturedPlaylistsCommand();
                    break;

                case "categories":
                    command = commandProvider.provideCategoriesCommand();
                    break;

                case "playlists":
                    command = commandProvider.provideCategoryPlaylistsCommand(commandString);
                    break;

                case "next":
                    command = commandProvider.provideNextPageCommand();
                    break;

                case "prev":
                    command = commandProvider.providePrevPageCommand();
                    break;

                case "exit":
                    this.view.display("Terminating...");
                    return;

                default:
                    command = commandProvider.provideInvalidCommand();
            }
            command.execute();

        }
    }
}
