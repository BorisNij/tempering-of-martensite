package net.bnijik.spotify.explorer.service;

import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.model.MusicItem;

public interface UserConsoleService {

    int DEFAULT_ITEMS_PER_PAGE = 5;
    int MINIMUM_ITEMS_PER_PAGE = 1;

    int itemsPerPage();

    void display(MusicItemCache.MusicItemPage<? extends MusicItem> itemPage);

    void display(String msg);

    void errorMsg(String errorMsg);

    String getUserCommandString();

    void close();
}
