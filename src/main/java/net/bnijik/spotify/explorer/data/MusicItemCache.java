package net.bnijik.spotify.explorer.data;

import net.bnijik.spotify.explorer.commands.CategoriesCommand;
import net.bnijik.spotify.explorer.commands.CategoryPlaylistsCommand;
import net.bnijik.spotify.explorer.commands.FeaturedPlaylistsCommand;
import net.bnijik.spotify.explorer.commands.NewAlbumsCommand;
import net.bnijik.spotify.explorer.model.MusicItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds a list of objects implementing the {@link MusicItem} interface.
 * Allows producing a sub-list of these objects, namely an object MusicItemPage
 * with a predefined number of items per page. Provides methods for scrolling
 * back and forth the original list ({@link #prevPage()} and {@link #nextPage()}).<p>
 * <p>
 * The list is populated with Albums, Categories or Playlists
 * (via the {@link #setItems(List) setItems(List) method} in the corresponding Command - <br>
 * {@link NewAlbumsCommand}, <br>
 * {@link CategoriesCommand}, <br>
 * {@link FeaturedPlaylistsCommand} and <br>
 * {@link CategoryPlaylistsCommand}
 *
 * @param <T> bound by Album, Category or Playlist types.
 */
public class MusicItemCache<T extends MusicItem> {

    // shared empty MusicItemPage instance used for empty instances
    @SuppressWarnings("rawtypes")
    private static final MusicItemPage EMPTY_MUSIC_ITEM_PAGE = new EmptyMusicItemPage();
    private static final int DEFAULT_ITEMS_PER_PAGE = 5;
    private static final int MINIMUM_ITEMS_PER_PAGE = 1;

    private List<T> musicItems;
    private final MusicItemPage<T> page;
    private final int itemsPerPage;

    public MusicItemCache(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage < MINIMUM_ITEMS_PER_PAGE
                ? DEFAULT_ITEMS_PER_PAGE
                : itemsPerPage;
        this.musicItems = new ArrayList<>();
        this.page = new MusicItemPage<>();
    }

    public static <T extends MusicItem> MusicItemPage<T> pageOf(List<T> items, int thisPageNum, int lastPageNum) {
        if (items == null || thisPageNum < 1 || lastPageNum < 1) {
            return null;
        }

        if (items.isEmpty() || thisPageNum > lastPageNum) {
            //noinspection unchecked
            return (MusicItemPage<T>) EMPTY_MUSIC_ITEM_PAGE;
        }

        MusicItemPage<T> itemPage = new MusicItemPage<>();
        itemPage.items = items;
        itemPage.currentPage = thisPageNum;
        itemPage.lastPage = lastPageNum;
        return itemPage;
    }

    public static <T extends MusicItem> MusicItemPage<T> emptyPage() {
        //noinspection unchecked
        return (MusicItemPage<T>) EMPTY_MUSIC_ITEM_PAGE;
    }

    public List<T> getItems() {
        return musicItems;
    }

    public T getByName(String itemName) {
        return musicItems.stream()
                .filter(item -> item.description()
                        .contains(itemName))
                .findFirst()
                .orElse(null);
    }

    public void setItems(List<T> musicItems) {
        if (musicItems == null || musicItems.isEmpty()) {
            return;
        }

        this.musicItems = musicItems;
        page.lastPage = calcLastPage();
    }

    public MusicItemPage<T> currentPage() {
        if (musicItems.isEmpty()) {
            //noinspection unchecked
            return (MusicItemPage<T>) EMPTY_MUSIC_ITEM_PAGE;
        }

        if (page.items == null) {
            setPage();
        }
        return page;
    }

    public MusicItemPage<T> nextPage() {
        if (musicItems.isEmpty()) {
            //noinspection unchecked
            return (MusicItemPage<T>) EMPTY_MUSIC_ITEM_PAGE;
        }

        if (page.items == null) {
            setPage();
        }

        if (page.currentPage == page.lastPage) {
            //noinspection unchecked
            return (MusicItemPage<T>) EMPTY_MUSIC_ITEM_PAGE;
        } else {
            page.lastShownItem += itemsPerPage;
            page.currentPage++;
            setPage();
            return page;
        }
    }

    public MusicItemPage<T> prevPage() {
        if (musicItems.isEmpty()) {
            //noinspection unchecked
            return EMPTY_MUSIC_ITEM_PAGE;
        }

        if (page.items == null) {
            setPage();
        }

        if (page.currentPage == 1) {
            //noinspection unchecked
            return EMPTY_MUSIC_ITEM_PAGE;
        } else {
            page.lastShownItem -= itemsPerPage;
            page.currentPage--;
            setPage();
            return page;
        }
    }

    private void setPage() {

        page.items = musicItems.stream()
                .skip(page.lastShownItem)
                .limit(itemsPerPage)
                .collect(Collectors.toList());
    }

    private int calcLastPage() {
        int lastPage = musicItems.size() / itemsPerPage;
        return lastPage * itemsPerPage == musicItems.size() ? lastPage : lastPage + 1;
    }

    public static class MusicItemPage<T extends MusicItem> {

        private List<T> items;
        private int currentPage;
        private int lastShownItem;
        private int lastPage;

        public MusicItemPage() {
            this.currentPage = 1;
            this.lastShownItem = 0;
            this.lastPage = currentPage;
        }

        public List<T> list() {
            return items;
        }

        public int currentPage() {
            return currentPage;
        }

        public int lastPage() {
            return lastPage;
        }
    }

    private static class EmptyMusicItemPage<T extends MusicItem> extends MusicItemPage<T> {
        private final MusicItemPage<T> page;

        private EmptyMusicItemPage() {
            this.page = new MusicItemPage<>();
            this.page.items = Collections.emptyList();
        }

        @Override
        public List<T> list() {
            return page.items;
        }
    }

}
