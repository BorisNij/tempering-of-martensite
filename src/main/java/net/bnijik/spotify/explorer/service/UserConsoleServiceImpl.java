package net.bnijik.spotify.explorer.service;

import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.model.MusicItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * Serves as a passive View based on {@code System.in} and {@code System.out}.
 * Using {@code System.in}, receives user actions as command strings.
 * Using {@code System.out}, outputs messages and a sub-list of pre-cached
 * music items (i.e. item MusicItemPage) to the user. Using {@code System.err},
 * outputs error messages to the user.
 */
@Service
public class UserConsoleServiceImpl implements UserConsoleService, AutoCloseable {

    private static final Scanner scanner;
    private final int itemsPerPage;

    static {
        scanner = new Scanner(System.in);
    }

    public UserConsoleServiceImpl(@Value("${app.items-per-page}") int itemsPerPage) {
        this.itemsPerPage = itemsPerPage < UserConsoleService.MINIMUM_ITEMS_PER_PAGE
                ? UserConsoleService.DEFAULT_ITEMS_PER_PAGE
                : itemsPerPage;
    }

    @Override
    public int itemsPerPage() {
        return itemsPerPage;
    }

    @Override
    public void display(MusicItemCache.MusicItemPage<? extends MusicItem> itemPage) {
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

    @Override
    public void display(String msg) {
        System.out.println(msg);
    }

    @Override
    public void errorMsg(String errorMsg) {
        System.err.println("ERROR: " + errorMsg);
    }

    @Override
    public String getUserCommandString() {
        System.out.println();
        System.out.print("> ");
        final String commandString = scanner.nextLine();
        return commandString == null ? "" : commandString;
    }

    @Override
    public void close() {
        scanner.close();
    }

}
