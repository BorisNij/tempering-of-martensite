package niji.kovsky.bn.spotify.explorer;

/**
 * App entry point, instantiates the following objects:
 * <ul>
 * <li> {@code UserConsole} - serves as a passive View based on {@code System.in} and {@code System.out},
 * <li> {@code AuthService} - manages authorization through Spotify's OAuth 2.0 endpoint to get an Access Token,
 * <li> {@code ApiService} - consumes Spotify's REST api to fetch music items using the obtained Access Token,
 * <li> {@code ApiResponseParser} - deserializes JSON data received from Spotify for storing in a music item {@code Cache} object,
 * <li> {@code Controller} - processes user command-strings received via the View into
 * {@code SpotifyExplorerCommand} objects. Passes the View, Service and music item {@code Cache} objects
 * to the {@code SpotifyExplorerCommand} as arguments. Executes the {@code SpotifyExplorerCommand}
 * that corresponds to the user command-string. <p>
 *  </ul>
 *  Before the app terminates, closes the {@code System.in} stream of the View.
 */
public class SpotifyExplorer {
    public static void main(String[] args) {

        UserConsole view = new UserConsole(5);
        ApiResponseParser<String> responseParser = new JsonStringResponseParserImpl();
        AuthService authService = new AuthService(responseParser);
        ApiService apiService = new ApiService(responseParser);

        new Controller(view, authService, apiService).start();

        view.close();
        view.display("Bye bye!");
    }
}
