package niji.kovsky.bn.spotify.explorer;

import niji.kovsky.bn.spotify.explorer.model.MusicItem;

import java.util.Scanner;

public class UserConsole implements AutoCloseable {

    private static final String EOL;
    private static final Scanner scanner;
    private final int itemsPerPage;

    static {
        scanner = new Scanner(System.in);
        EOL = System.getProperty("line.separator");
    }

    public UserConsole(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int itemsPerPage() {
        return itemsPerPage;
    }

    public void display(Cache.Page<? extends MusicItem> itemPage) {
        if (itemPage == null) {
            errorMsg("Pages are not initialized. Try fetching items from Spotify ('new', 'categories', 'featured'");
            return;
        }

        if (itemPage.list()
                .isEmpty()) {
            System.out.println("No more pages");
            return;
        }

        itemPage.list()
                .forEach(musicItem -> System.out.println(musicItem.description()));
        System.out.printf("--- page %d of %d ---%n", itemPage.currentPage(), itemPage.lastPage());
    }

    public void display(String msg) {
        System.out.println(msg);
    }

    public void errorMsg(String errorMsg) {
        System.err.println("ERROR: " + errorMsg);
        try {
            Thread.sleep(0, 10);
        } catch (InterruptedException ignored) {
        }
    }

    public String getCommand() {
        System.out.print(EOL +
                         "> ");
        return scanner.nextLine();
    }

    @Override
    public void close() {
        scanner.close();
    }

}
