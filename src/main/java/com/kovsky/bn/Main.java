package com.kovsky.bn;

public class Main {
    public static void main(String[] args) {

        UserConsole view = new UserConsole(5);
        AuthService authService = new AuthService();
        ApiService apiService = new ApiService();

        new Controller(view, authService, apiService).start();

        view.close();
        view.display("Bye bye!");
    }
}
