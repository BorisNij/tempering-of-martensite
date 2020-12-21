package net.bnijik.spotify.explorer;

import net.bnijik.spotify.explorer.model.Album;
import net.bnijik.spotify.explorer.model.MusicItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CacheTest<T extends MusicItem> {

    @Test
    @DisplayName("pageOf() should produce a Page instance preloaded with specified item list")
    void pageOf_ShouldProduceAPageInstancePreloadedWithSpecifiedItems() {
        //noinspection unchecked
        final T musicItem = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");
        final Cache.Page<T> musicItemPage = Cache.pageOf(
                List.of(musicItem, musicItem),
                1,
                1);

        assertAll(() -> assertNotNull(musicItemPage),
                  () -> assertNotNull(musicItemPage.list()),
                  () -> assertEquals(2, musicItemPage.list()
                          .size()));
    }

    @Test
    @DisplayName("pageOf() should return null on invalid input or on null input")
    void pageOf_shouldReturnNullOnInvalidInputOrOnNullInput() {
        //noinspection unchecked
        final T musicItem = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");
        final List<T> tItems = List.of(musicItem, musicItem);
        assertAll(
                () -> assertNull(Cache.pageOf(null, 2, 2)),
                () -> assertNull(Cache.pageOf(tItems, 0, 1)),
                () -> assertNull(Cache.pageOf(tItems, -1, 1)),
                () -> assertNull(Cache.pageOf(tItems, 1, 0)),
                () -> assertNull(Cache.pageOf(tItems, 1, -1))
        );
    }

    @Test
    @DisplayName("pageOf() should return empty unmodifiable Page instance if supplied with empty list")
    void pageOf_ShouldReturnEmptyPageIfSuppliedWithEmptyList() {
        final List<T> emptyListOfT = Collections.emptyList();
        final Cache.Page<T> expectedEmptyPage = Cache.pageOf(emptyListOfT, 1, 2);
        //noinspection unchecked
        final T musicItem = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");

        assertAll(() -> assertNotNull(expectedEmptyPage),
                  () -> assertNotNull(expectedEmptyPage.list()),
                  () -> assertTrue(expectedEmptyPage.list()
                                           .isEmpty()),
                  () -> assertEquals(1, expectedEmptyPage.currentPage()),
                  () -> assertEquals(1, expectedEmptyPage.lastPage()),
                  () -> assertThrows(UnsupportedOperationException.class,
                                     () -> expectedEmptyPage.list()
                                             .add(musicItem))
        );
    }

    @Test
    @DisplayName("pageOf() should return empty unmodifiable Page instance if supplied pageNum is > lastPage")
    void pageOf_ShouldReturnEmptyPageIfSuppliedPageNumSmallerThanLastPage() {
        //noinspection unchecked
        final T musicItem = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");
        final List<T> tItems = List.of(musicItem, musicItem);
        final Cache.Page<T> expectedEmptyPage = Cache.pageOf(tItems, 3, 2);

        assertAll(() -> assertNotNull(expectedEmptyPage),
                  () -> assertNotNull(expectedEmptyPage.list()),
                  () -> assertTrue(expectedEmptyPage.list()
                                           .isEmpty()),
                  () -> assertEquals(1, expectedEmptyPage.currentPage()),
                  () -> assertEquals(1, expectedEmptyPage.lastPage()),
                  () -> assertThrows(UnsupportedOperationException.class,
                                     () -> expectedEmptyPage.list()
                                             .add(musicItem))
        );
    }

    @Test
    @DisplayName("emptyPage() should return an empty unmodifiable Page instance")
    void emptyPage_ShouldReturnEmptyPage() {
        //noinspection unchecked
        final T musicItem = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");
        final Cache.Page<T> expectedEmptyPage = Cache.emptyPage();
        assertAll(() -> assertNotNull(expectedEmptyPage),
                  // () -> assertTrue(musicItemPage instanceof Cache.Page),
                  () -> assertNotNull(expectedEmptyPage.list()),
                  () -> assertTrue(expectedEmptyPage.list()
                                           .isEmpty()),
                  () -> assertThrows(UnsupportedOperationException.class,
                                     () -> expectedEmptyPage.list()
                                             .add(musicItem)));
    }

    @Test
    @DisplayName("getByName() should return first music item from this Cache with specified name")
    void getByName_ShouldReturnMusicItemFromCacheWithSpecifiedName() {
        //noinspection unchecked
        final T album1 = (T) new Album("name1", "a1", "http");
        //noinspection unchecked
        final T album2 = (T) new Album("name2", "b1, b2", "http");
        //noinspection unchecked
        final T album3 = (T) new Album("name2", "b1, b2", "http");

        Cache<T> cache = new Cache<>(5);
        cache.setItems(List.of(album1, album2, album3));
        assertEquals(album2, cache.getByName("name2"));
    }

    @Test
    @DisplayName("getByName should return null if item with specified name was not found in this Cache")
    void getByName_ShouldReturnNullIfItemWithSpecifiedNameWasNotFoundInThisCache() {
        //noinspection unchecked
        final T album1 = (T) new Album("name1", "a1", "http");
        //noinspection unchecked
        final T album2 = (T) new Album("name2", "b1, b2", "http");

        Cache<T> cache = new Cache<>(5);
        cache.setItems(List.of(album1, album2));
        assertNull(cache.getByName("nonExistentName"));
    }

    @Test
    @DisplayName("currentPage() should return instance of Page preloaded with specified number of items")
    void currentPage_ShouldReturnInstanceOfPagePreloadedWithSpecifiedNumberOfItems() {
        //noinspection unchecked
        final T album1 = (T) new Album("name1", "a1", "http");
        //noinspection unchecked
        final T album2 = (T) new Album("name2", "b1, b2", "http");
        //noinspection unchecked
        final T album3 = (T) new Album("name3", "c1, c2", "http");
        //noinspection unchecked
        final T album4 = (T) new Album("name4", "d1, d2, d3", "http");
        //noinspection unchecked
        final T album5 = (T) new Album("name5", "e1, e2", "http");

        Cache<T> cache = new Cache<>(3);
        cache.setItems(List.of(album1, album2, album3, album4, album5));
        Cache.Page<T> testedPage = cache.currentPage();

        assertAll(() -> assertEquals(1, testedPage.currentPage()),
                  () -> assertEquals(2, testedPage.lastPage()),
                  () -> assertEquals(List.of(album1, album2, album3), testedPage.list())
        );
    }

    @Test
    @DisplayName("currentPage() item count should be capped by the specified number of items")
    void currentPage_ItemCountShouldBeCappedByTheSpecifiedNumberOfItems() {
        //noinspection unchecked
        final T album1 = (T) new Album("name1", "a1", "http");
        //noinspection unchecked
        final T album2 = (T) new Album("name2", "b1, b2", "http");
        //noinspection unchecked
        final T album3 = (T) new Album("name3", "c1, c2", "http");
        //noinspection unchecked
        final T album4 = (T) new Album("name4", "d1, d2, d3", "http");
        //noinspection unchecked
        final T album5 = (T) new Album("name5", "e1, e2", "http");

        Cache<T> cache = new Cache<>(3);
        cache.setItems(List.of(album1, album2, album3, album4, album5));
        Cache.Page<T> testedPage = cache.currentPage();

        assertAll(() -> assertNotNull(testedPage),
                  () -> assertEquals(List.of(album1, album2, album3), testedPage.list())
        );
    }

    @Test
    @DisplayName("currentPage() item count should be capped by the number of items in this Cache")
    void currentPage_ItemCountShouldBeCappedByTheNumberOfItemsInThisCache() {
        //noinspection unchecked
        final T album1 = (T) new Album("name1", "a1", "http");
        //noinspection unchecked
        final T album2 = (T) new Album("name2", "b1, b2", "http");
        //noinspection unchecked
        final T album3 = (T) new Album("name3", "c1, c2", "http");
        //noinspection unchecked
        final T album4 = (T) new Album("name4", "d1, d2, d3", "http");
        //noinspection unchecked
        final T album5 = (T) new Album("name5", "e1, e2", "http");

        Cache<T> cache = new Cache<>(14);
        final List<T> items = List.of(album1, album2, album3, album4, album5);
        cache.setItems(items);
        Cache.Page<T> testedPage = cache.currentPage();

        assertAll(() -> assertNotNull(testedPage),
                  () -> assertEquals(items, testedPage.list())
        );
    }

    @Test
    @DisplayName("currentPage() should return the last non-empty Page of items after nextPage() reached last Page")
    void currentPage_ShouldReturnTheLastNonEmptyPageOfItemsAfterNextPageReachedLastPage() {
        //noinspection unchecked
        final T album1 = (T) new Album("name1", "a1", "http");
        //noinspection unchecked
        final T album2 = (T) new Album("name2", "b1, b2", "http");
        //noinspection unchecked
        final T album3 = (T) new Album("name3", "c1, c2", "http");
        //noinspection unchecked
        final T album4 = (T) new Album("name4", "d1, d2, d3", "http");
        //noinspection unchecked
        final T album5 = (T) new Album("name5", "e1, e2", "http");

        Cache<T> cache = new Cache<>(3);
        cache.setItems(List.of(album1, album2, album3, album4, album5));
        cache.nextPage();
        cache.nextPage();
        cache.nextPage();

        assertAll(() -> assertNotNull(cache.currentPage()),
                  () -> assertEquals(List.of(album4, album5), cache.currentPage()
                          .list()),
                  () -> assertEquals(2, cache.currentPage()
                          .lastPage())
        );
    }

    @Test
    @DisplayName("currentPage() should return empty unmodifiable instance of Page if Cache items not set")
    void currentPage_ShouldReturnEmptyUnmodifiableInstanceOfPageIfCacheItemsNotSet() {
        Cache<T> cache = new Cache<>(3);
        Cache.Page<T> expectedEmptyPage = cache.currentPage();
        //noinspection unchecked
        final T musicItem = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");

        assertAll(() -> assertNotNull(expectedEmptyPage),
                  () -> assertNotNull(expectedEmptyPage.list()),
                  () -> assertTrue(expectedEmptyPage.list()
                                           .isEmpty()),
                  () -> assertEquals(1, expectedEmptyPage.currentPage()),
                  () -> assertEquals(1, expectedEmptyPage.lastPage()),
                  () -> assertThrows(UnsupportedOperationException.class,
                                     () -> expectedEmptyPage.list()
                                             .add(musicItem))
        );
    }

    @Test
    @DisplayName("nextPage() should return an instance of Page preloaded with specified number of items from this Cache")
    void nextPage_ShouldReturnAnInstanceOfPagePreloadedWithSpecifiedNumberOfItems() {
        //noinspection unchecked
        final T album1 = (T) new Album("name1", "a1", "http");
        //noinspection unchecked
        final T album2 = (T) new Album("name2", "b1, b2", "http");
        //noinspection unchecked
        final T album3 = (T) new Album("name3", "c1, c2", "http");
        //noinspection unchecked
        final T album4 = (T) new Album("name4", "d1, d2, d3", "http");
        //noinspection unchecked
        final T album5 = (T) new Album("name5", "e1, e2", "http");

        Cache<T> cache = new Cache<>(2);
        cache.setItems(List.of(album1, album2, album3, album4, album5));
        Cache.Page<T> testedPage = cache.nextPage();

        assertAll(() -> assertNotNull(testedPage),
                  () -> assertEquals(2, testedPage.currentPage()),
                  () -> assertEquals(3, testedPage.lastPage()),
                  () -> assertEquals(List.of(album3, album4), testedPage.list())
        );
    }

    @Test
    @DisplayName("nextPage() item count should be capped by the number of items in this Cache when returning the last Page")
    void nextPage_ItemCountShouldBeCappedByTheNumberOfItemsInThisCacheWhenReturningLastPage() {
        //noinspection unchecked
        final T album1 = (T) new Album("name1", "a1", "http");
        //noinspection unchecked
        final T album2 = (T) new Album("name2", "b1, b2", "http");
        //noinspection unchecked
        final T album3 = (T) new Album("name3", "c1, c2", "http");
        //noinspection unchecked
        final T album4 = (T) new Album("name4", "d1, d2, d3", "http");
        //noinspection unchecked
        final T album5 = (T) new Album("name5", "e1, e2", "http");

        Cache<T> cache = new Cache<>(3);
        cache.setItems(List.of(album1, album2, album3, album4, album5));
        Cache.Page<T> testedPage = cache.nextPage();

        assertAll(() -> assertNotNull(testedPage),
                  () -> assertEquals(2, testedPage.currentPage()),
                  () -> assertEquals(2, testedPage.lastPage()),
                  () -> assertEquals(List.of(album4, album5), testedPage.list())
        );
    }

    @Test
    @DisplayName("nextPage() should return empty unmodifiable instance of Page if called after last Page")
    void nextPage_ShouldReturnEmptyUnmodifiableInstanceOfPageIfCalledAfterLastPage() {
        //noinspection unchecked
        final T musicItem1 = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");
        //noinspection unchecked
        final T musicItem2 = (T) new Album("album2", "[artist_a2]", "https://url.of.a2");

        Cache<T> cache = new Cache<>(3);
        cache.setItems(List.of(musicItem1, musicItem2));
        cache.nextPage();
        Cache.Page<T> expectedEmptyPage = cache.nextPage();

        assertAll(() -> assertNotNull(expectedEmptyPage),
                  () -> assertNotNull(expectedEmptyPage.list()),
                  () -> assertTrue(expectedEmptyPage.list()
                                           .isEmpty()),
                  () -> assertEquals(1, expectedEmptyPage.currentPage()),
                  () -> assertEquals(1, expectedEmptyPage.lastPage()),
                  () -> assertThrows(UnsupportedOperationException.class,
                                     () -> expectedEmptyPage.list()
                                             .add(musicItem1))
        );
    }

    @Test
    @DisplayName("nextPage() should return empty unmodifiable instance of Page if Cache items not set")
    void nextPage_ShouldReturnEmptyUnmodifiableInstanceOfPageIfCacheItemsNotSet() {
        Cache<T> cache = new Cache<>(3);
        Cache.Page<T> expectedEmptyPage = cache.nextPage();
        //noinspection unchecked
        final T musicItem = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");

        assertAll(() -> assertNotNull(expectedEmptyPage),
                  () -> assertNotNull(expectedEmptyPage.list()),
                  () -> assertTrue(expectedEmptyPage.list()
                                           .isEmpty()),
                  () -> assertEquals(1, expectedEmptyPage.currentPage()),
                  () -> assertEquals(1, expectedEmptyPage.lastPage()),
                  () -> assertThrows(UnsupportedOperationException.class,
                                     () -> expectedEmptyPage.list()
                                             .add(musicItem))
        );
    }

    @Test
    @DisplayName("prevPage() should return empty unmodifiable instance of Page if called on first Page")
    void prevPage_ShouldReturnEmptyUnmodifiableInstanceOfPageIfCalledOnFirstPage() {
        //noinspection unchecked
        final T musicItem1 = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");
        //noinspection unchecked
        final T musicItem2 = (T) new Album("album2", "[artist_a2]", "https://url.of.a2");

        Cache<T> cache = new Cache<>(3);
        cache.setItems(List.of(musicItem1, musicItem2));
        Cache.Page<T> expectedEmptyPage = cache.prevPage();

        assertAll(() -> assertNotNull(expectedEmptyPage),
                  () -> assertNotNull(expectedEmptyPage.list()),
                  () -> assertTrue(expectedEmptyPage.list()
                                           .isEmpty()),
                  () -> assertEquals(1, expectedEmptyPage.currentPage()),
                  () -> assertEquals(1, expectedEmptyPage.lastPage()),
                  () -> assertThrows(UnsupportedOperationException.class,
                                     () -> expectedEmptyPage.list()
                                             .add(musicItem1))
        );
    }

    @Test
    @DisplayName("prevPage() should return an instance of Page preloaded with specified number of items from this Cache")
    void prevPage_ShouldReturnAnInstanceOfPagePreloadedWithSpecifiedNumberOfItems() {
        //noinspection unchecked
        final T album1 = (T) new Album("name1", "a1", "http");
        //noinspection unchecked
        final T album2 = (T) new Album("name2", "b1, b2", "http");
        //noinspection unchecked
        final T album3 = (T) new Album("name3", "c1, c2", "http");
        //noinspection unchecked
        final T album4 = (T) new Album("name4", "d1, d2, d3", "http");
        //noinspection unchecked
        final T album5 = (T) new Album("name5", "e1, e2", "http");

        Cache<T> cache = new Cache<>(2);
        cache.setItems(List.of(album1, album2, album3, album4, album5));
        cache.nextPage();

        Cache.Page<T> testedPage = cache.prevPage();

        assertAll(() -> assertNotNull(testedPage),
                  () -> assertEquals(1, testedPage.currentPage()),
                  () -> assertEquals(3, testedPage.lastPage()),
                  () -> assertEquals(List.of(album1, album2), testedPage.list())
        );
    }

    @Test
    @DisplayName("prevPage() should return empty unmodifiable instance of Page if Cache items not set")
    void prevPage_ShouldReturnEmptyUnmodifiableInstanceOfPageIfCacheItemsNotSet() {
        Cache<T> cache = new Cache<>(3);
        Cache.Page<T> expectedEmptyPage = cache.prevPage();
        //noinspection unchecked
        final T musicItem = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");

        assertAll(() -> assertNotNull(expectedEmptyPage),
                  () -> assertNotNull(expectedEmptyPage.list()),
                  () -> assertTrue(expectedEmptyPage.list()
                                           .isEmpty()),
                  () -> assertEquals(1, expectedEmptyPage.currentPage()),
                  () -> assertEquals(1, expectedEmptyPage.lastPage()),
                  () -> assertThrows(UnsupportedOperationException.class,
                                     () -> expectedEmptyPage.list()
                                             .add(musicItem))
        );
    }
}