package nij.ikovsky.bn;

import nij.ikovsky.bn.model.MusicItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Cache<T extends MusicItem> {

    // shared empty Page instance used for empty instances
    @SuppressWarnings("rawtypes")
    private static final Page EMPTY_PAGE = new EmptyPage();
    private static final int DEFAULT_ITEMS_PER_PAGE = 5;
    private static final int MINIMUM_ITEMS_PER_PAGE = 1;

    private List<T> musicItems;
    private final Page<T> page;
    private final int itemsPerPage;

    public Cache(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage < MINIMUM_ITEMS_PER_PAGE
                ? DEFAULT_ITEMS_PER_PAGE
                : itemsPerPage;
        this.musicItems = new ArrayList<>();
        this.page = new Page<>();
    }

    public static <T extends MusicItem> Page<T> pageOf(List<T> items, int thisPageNum, int lastPageNum) {
        if (items == null || thisPageNum < 1 || lastPageNum < 1) {
            return null;
        }

        if (items.isEmpty() || thisPageNum > lastPageNum) {
            //noinspection unchecked
            return (Page<T>) EMPTY_PAGE;
        }

        Page<T> itemPage = new Page<>();
        itemPage.items = items;
        itemPage.currentPage = thisPageNum;
        itemPage.lastPage = lastPageNum;
        return itemPage;
    }

    public static <T extends MusicItem> Page<T> emptyPage() {
        //noinspection unchecked
        return (Page<T>) EMPTY_PAGE;
    }

    public List<T> getItems() {
        return this.musicItems;
    }

    public T getByName(String itemName) {
        return this.musicItems.stream()
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
        this.page.lastPage = this.calcLastPage();
    }

    public Page<T> currentPage() {
        if (this.musicItems.isEmpty()) {
            //noinspection unchecked
            return (Page<T>) EMPTY_PAGE;
        }

        if (this.page.items == null) {
            setPage();
        }
        return this.page;
    }

    public Page<T> nextPage() {
        if (this.musicItems.isEmpty()) {
            //noinspection unchecked
            return (Page<T>) EMPTY_PAGE;
        }

        if (this.page.items == null) {
            setPage();
        }

        if (this.page.currentPage == this.page.lastPage) {
            //noinspection unchecked
            return (Page<T>) EMPTY_PAGE;
        } else {
            this.page.lastShownItem += this.itemsPerPage;
            this.page.currentPage++;
            setPage();
            return this.page;
        }
    }

    public Page<T> prevPage() {
        if (this.musicItems.isEmpty()) {
            //noinspection unchecked
            return EMPTY_PAGE;
        }

        if (this.page.items == null) {
            setPage();
        }

        if (this.page.currentPage == 1) {
            //noinspection unchecked
            return EMPTY_PAGE;
        } else {
            this.page.lastShownItem -= this.itemsPerPage;
            this.page.currentPage--;
            setPage();
            return this.page;
        }
    }

    private void setPage() {

        this.page.items = this.musicItems.stream()
                .skip(this.page.lastShownItem)
                .limit(this.itemsPerPage)
                .collect(Collectors.toList());
    }

    private int calcLastPage() {
        int lastPage = this.musicItems.size() / this.itemsPerPage;
        return lastPage * this.itemsPerPage == this.musicItems.size() ? lastPage : lastPage + 1;
    }

    public static class Page<T extends MusicItem> {

        private List<T> items;
        private int currentPage;
        private int lastShownItem;
        private int lastPage;

        public Page() {
            this.currentPage = 1;
            this.lastShownItem = 0;
            this.lastPage = this.currentPage;
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

    private static class EmptyPage<T extends MusicItem> extends Page<T> {
        private final Page<T> page;

        private EmptyPage() {
            this.page = new Page<>();
            this.page.items = Collections.emptyList();
        }

        @Override
        public List<T> list() {
            return this.page.items;
        }
    }

}
