package com.kovsky.bn;

import com.kovsky.bn.model.MusicItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Cache<T extends MusicItem> {

    // shared empty Page instance used for empty instances
    @SuppressWarnings("rawtypes")
    private static final Page EMPTY_PAGE = new EmptyPage();

    private List<T> musicItems;
    private final Page<T> page;
    private final int itemsPerPage;

    public Cache(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
        this.musicItems = new ArrayList<>();
        this.page = new Page<>();
        resetPage();
    }

    private void resetPage() {
        this.page.currentPage = 1;
        this.page.lastShownItem = 0;
        this.page.lastPage = this.page.currentPage;
    }

    public static <T extends MusicItem> Page<T> pageOf(List<T> items, int thisPageNum, int lastPageNum) {
        Page<T> itemPage = new Page<>();
        itemPage.items = items;
        itemPage.lastShownItem = thisPageNum;
        itemPage.lastPage = lastPageNum;
        return itemPage;
    }

    @SuppressWarnings("unchecked")
    public static <T extends MusicItem> Page<T> emptyPage() {
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

        if (this.page.items == null) {
            setPage();
        }
        return this.page;
    }

    @SuppressWarnings("unchecked")
    public Page<T> nextPage() {

        if (this.page.items == null) {
            return null;
        }

        if (this.page.currentPage == this.page.lastPage) {
            return EMPTY_PAGE;
        } else {
            this.page.lastShownItem += this.itemsPerPage;
            this.page.currentPage++;
            setPage();
            return this.page;
        }
    }

    @SuppressWarnings("unchecked")
    public Page<T> prevPage() {

        if (this.page.items == null) {
            return null;
        }

        if (this.page.currentPage == 1) {
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
        private int currentPage = 1;
        private int lastShownItem;
        private int lastPage;

        public List<T> list() {
            return Collections.unmodifiableList(items);
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

        public EmptyPage() {
            this.page = new Page<>();
            this.page.items = Collections.emptyList();
        }

        @Override
        public List<T> list() {
            return this.page.list();
        }
    }

}
