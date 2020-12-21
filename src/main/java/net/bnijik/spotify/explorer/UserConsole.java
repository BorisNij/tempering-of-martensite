package net.bnijik.spotify.explorer;

import net.bnijik.spotify.explorer.model.MusicItem;

import java.util.Scanner;

/**
 * Serves as a passive View based on {@code System.in} and {@code System.out}.
 * Using {@code System.in}, receives user actions as command strings.
 * Using {@code System.out}, outputs messages and a sub-list of pre-cached
 * music items (i.e. item Page) to the user. Using {@code System.err},
 * outputs error messages to the user.
 */
public class UserConsole implements AutoCloseable {

    private static final Scanner scanner;
    private final int itemsPerPage;

    static {
        scanner = new Scanner(System.in);
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
    }

    public String getUserCommandString() {
        System.out.println();
        System.out.print("> ");
        return scanner.nextLine();
    }

    @Override
    public void close() {
        scanner.close();
    }

}
