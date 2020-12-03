package com.kovsky.bn.commands;

import com.kovsky.bn.UserConsole;

public class InvalidCommand implements SpotifyExplorerCommand {
    private final UserConsole view;

    public InvalidCommand(UserConsole view) {
        this.view = view;
    }

    @Override
    public void execute() {
        this.view.errorMsg("Invalid command. Please try again.");
    }
}
