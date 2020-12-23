package net.bnijik.spotify.explorer;

import net.bnijik.spotify.explorer.configuration.SpringConfiguration;
import net.bnijik.spotify.explorer.controller.CommandController;
import net.bnijik.spotify.explorer.service.UserConsoleService;
import net.bnijik.spotify.explorer.service.UserConsoleServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * App entry point, instantiates the following objects:
 * <ul>
 * <li> {@code UserConsole} - serves as a passive View based on {@code System.in} and {@code System.out},
 * <li> {@code AuthService} - manages authorization through Spotify's OAuth 2.0 endpoint to get an Access Token,
 * <li> {@code ApiService} - consumes Spotify's REST api to fetch music items using the obtained Access Token,
 * <li> {@code SpotifyResponseParser} - deserializes JSON data received from Spotify for storing in a music item {@code MusicItemCache} object,
 * <li> {@code Controller} - processes user command-strings received via the View into
 * {@code SpotifyExplorerCommand} objects. Passes the View, Service and music item {@code MusicItemCache} objects
 * to the {@code SpotifyExplorerCommand} as arguments. Executes the {@code SpotifyExplorerCommand}
 * that corresponds to the user command-string. <p>
 *  </ul>
 *  Before the app terminates, closes the {@code System.in} stream of the View.
 */
public class SpotifyExplorer {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context;
        context = new AnnotationConfigApplicationContext(SpringConfiguration.class);

        UserConsoleService view = context.getBean(UserConsoleServiceImpl.class);
        CommandController controller = context.getBean(CommandController.class);
        controller.start();

        view.close();
        view.display("Bye bye!");
    }
}
