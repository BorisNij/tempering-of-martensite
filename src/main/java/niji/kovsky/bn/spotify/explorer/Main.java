package niji.kovsky.bn.spotify.explorer;

public class Main {
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
