package net.bnijik.spotify.explorer;

import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.model.Album;
import net.bnijik.spotify.explorer.model.MusicItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MusicItemCacheTest<T extends MusicItem> {

    @Test
    @DisplayName("pageOf() should produce a MusicItemPage instance preloaded with specified item list")
    void pageOf_ShouldProduceAPageInstancePreloadedWithSpecifiedItems() {
        //noinspection unchecked
        final T musicItem = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");
        final MusicItemCache.MusicItemPage<T> musicItemPage = MusicItemCache.pageOf(
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
                () -> assertNull(MusicItemCache.pageOf(null, 2, 2)),
                () -> assertNull(MusicItemCache.pageOf(tItems, 0, 1)),
                () -> assertNull(MusicItemCache.pageOf(tItems, -1, 1)),
                () -> assertNull(MusicItemCache.pageOf(tItems, 1, 0)),
                () -> assertNull(MusicItemCache.pageOf(tItems, 1, -1))
        );
    }

    @Test
    @DisplayName("pageOf() should return empty unmodifiable MusicItemPage instance if supplied with empty list")
    void pageOf_ShouldReturnEmptyPageIfSuppliedWithEmptyList() {
        final List<T> emptyListOfT = Collections.emptyList();
        final MusicItemCache.MusicItemPage<T> expectedEmptyPage = MusicItemCache.pageOf(emptyListOfT, 1, 2);
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
    @DisplayName("pageOf() should return empty unmodifiable MusicItemPage instance if supplied pageNum is > lastPage")
    void pageOf_ShouldReturnEmptyPageIfSuppliedPageNumSmallerThanLastPage() {
        //noinspection unchecked
        final T musicItem = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");
        final List<T> tItems = List.of(musicItem, musicItem);
        final MusicItemCache.MusicItemPage<T> expectedEmptyPage = MusicItemCache.pageOf(tItems, 3, 2);

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
    @DisplayName("emptyPage() should return an empty unmodifiable MusicItemPage instance")
    void emptyPage_ShouldReturnEmptyPage() {
        //noinspection unchecked
        final T musicItem = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");
        final MusicItemCache.MusicItemPage<T> expectedEmptyPage = MusicItemCache.emptyPage();
        assertAll(() -> assertNotNull(expectedEmptyPage),
                  // () -> assertTrue(musicItemPage instanceof MusicItemCache.MusicItemPage),
                  () -> assertNotNull(expectedEmptyPage.list()),
                  () -> assertTrue(expectedEmptyPage.list()
                                           .isEmpty()),
                  () -> assertThrows(UnsupportedOperationException.class,
                                     () -> expectedEmptyPage.list()
                                             .add(musicItem)));
    }

    @Test
    @DisplayName("getByName() should return first music item from this MusicItemCache with specified name")
    void getByName_ShouldReturnMusicItemFromCacheWithSpecifiedName() {
        //noinspection unchecked
        final T album1 = (T) new Album("name1", "a1", "http");
        //noinspection unchecked
        final T album2 = (T) new Album("name2", "b1, b2", "http");
        //noinspection unchecked
        final T album3 = (T) new Album("name2", "b1, b2", "http");

        MusicItemCache<T> musicItemCache = new MusicItemCache<>(5);
        musicItemCache.setItems(List.of(album1, album2, album3));
        assertEquals(album2, musicItemCache.getByName("name2"));
    }

    @Test
    @DisplayName("getByName should return null if item with specified name was not found in this MusicItemCache")
    void getByName_ShouldReturnNullIfItemWithSpecifiedNameWasNotFoundInThisCache() {
        //noinspection unchecked
        final T album1 = (T) new Album("name1", "a1", "http");
        //noinspection unchecked
        final T album2 = (T) new Album("name2", "b1, b2", "http");

        MusicItemCache<T> musicItemCache = new MusicItemCache<>(5);
        musicItemCache.setItems(List.of(album1, album2));
        assertNull(musicItemCache.getByName("nonExistentName"));
    }

    @Test
    @DisplayName("currentPage() should return instance of MusicItemPage preloaded with specified number of items")
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

        MusicItemCache<T> musicItemCache = new MusicItemCache<>(3);
        musicItemCache.setItems(List.of(album1, album2, album3, album4, album5));
        MusicItemCache.MusicItemPage<T> testedPage = musicItemCache.currentPage();

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

        MusicItemCache<T> musicItemCache = new MusicItemCache<>(3);
        musicItemCache.setItems(List.of(album1, album2, album3, album4, album5));
        MusicItemCache.MusicItemPage<T> testedPage = musicItemCache.currentPage();

        assertAll(() -> assertNotNull(testedPage),
                  () -> assertEquals(List.of(album1, album2, album3), testedPage.list())
        );
    }

    @Test
    @DisplayName("currentPage() item count should be capped by the number of items in this MusicItemCache")
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

        MusicItemCache<T> musicItemCache = new MusicItemCache<>(14);
        final List<T> items = List.of(album1, album2, album3, album4, album5);
        musicItemCache.setItems(items);
        MusicItemCache.MusicItemPage<T> testedPage = musicItemCache.currentPage();

        assertAll(() -> assertNotNull(testedPage),
                  () -> assertEquals(items, testedPage.list())
        );
    }

    @Test
    @DisplayName("currentPage() should return the last non-empty MusicItemPage of items after nextPage() reached last MusicItemPage")
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

        MusicItemCache<T> musicItemCache = new MusicItemCache<>(3);
        musicItemCache.setItems(List.of(album1, album2, album3, album4, album5));
        musicItemCache.nextPage();
        musicItemCache.nextPage();
        musicItemCache.nextPage();

        assertAll(() -> assertNotNull(musicItemCache.currentPage()),
                  () -> assertEquals(List.of(album4, album5), musicItemCache.currentPage()
                          .list()),
                  () -> assertEquals(2, musicItemCache.currentPage()
                          .lastPage())
        );
    }

    @Test
    @DisplayName("currentPage() should return empty unmodifiable instance of MusicItemPage if MusicItemCache items not set")
    void currentPage_ShouldReturnEmptyUnmodifiableInstanceOfPageIfCacheItemsNotSet() {
        MusicItemCache<T> musicItemCache = new MusicItemCache<>(3);
        MusicItemCache.MusicItemPage<T> expectedEmptyPage = musicItemCache.currentPage();
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
    @DisplayName("nextPage() should return an instance of MusicItemPage preloaded with specified number of items from this MusicItemCache")
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

        MusicItemCache<T> musicItemCache = new MusicItemCache<>(2);
        musicItemCache.setItems(List.of(album1, album2, album3, album4, album5));
        MusicItemCache.MusicItemPage<T> testedPage = musicItemCache.nextPage();

        assertAll(() -> assertNotNull(testedPage),
                  () -> assertEquals(2, testedPage.currentPage()),
                  () -> assertEquals(3, testedPage.lastPage()),
                  () -> assertEquals(List.of(album3, album4), testedPage.list())
        );
    }

    @Test
    @DisplayName("nextPage() item count should be capped by the number of items in this MusicItemCache when returning the last MusicItemPage")
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

        MusicItemCache<T> musicItemCache = new MusicItemCache<>(3);
        musicItemCache.setItems(List.of(album1, album2, album3, album4, album5));
        MusicItemCache.MusicItemPage<T> testedPage = musicItemCache.nextPage();

        assertAll(() -> assertNotNull(testedPage),
                  () -> assertEquals(2, testedPage.currentPage()),
                  () -> assertEquals(2, testedPage.lastPage()),
                  () -> assertEquals(List.of(album4, album5), testedPage.list())
        );
    }

    @Test
    @DisplayName("nextPage() should return empty unmodifiable instance of MusicItemPage if called after last MusicItemPage")
    void nextPage_ShouldReturnEmptyUnmodifiableInstanceOfPageIfCalledAfterLastPage() {
        //noinspection unchecked
        final T musicItem1 = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");
        //noinspection unchecked
        final T musicItem2 = (T) new Album("album2", "[artist_a2]", "https://url.of.a2");

        MusicItemCache<T> musicItemCache = new MusicItemCache<>(3);
        musicItemCache.setItems(List.of(musicItem1, musicItem2));
        musicItemCache.nextPage();
        MusicItemCache.MusicItemPage<T> expectedEmptyPage = musicItemCache.nextPage();

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
    @DisplayName("nextPage() should return empty unmodifiable instance of MusicItemPage if MusicItemCache items not set")
    void nextPage_ShouldReturnEmptyUnmodifiableInstanceOfPageIfCacheItemsNotSet() {
        MusicItemCache<T> musicItemCache = new MusicItemCache<>(3);
        MusicItemCache.MusicItemPage<T> expectedEmptyPage = musicItemCache.nextPage();
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
    @DisplayName("prevPage() should return empty unmodifiable instance of MusicItemPage if called on first MusicItemPage")
    void prevPage_ShouldReturnEmptyUnmodifiableInstanceOfPageIfCalledOnFirstPage() {
        //noinspection unchecked
        final T musicItem1 = (T) new Album("album1", "[artist_a1, artist_a2]", "https://url.of.a1");
        //noinspection unchecked
        final T musicItem2 = (T) new Album("album2", "[artist_a2]", "https://url.of.a2");

        MusicItemCache<T> musicItemCache = new MusicItemCache<>(3);
        musicItemCache.setItems(List.of(musicItem1, musicItem2));
        MusicItemCache.MusicItemPage<T> expectedEmptyPage = musicItemCache.prevPage();

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
    @DisplayName("prevPage() should return an instance of MusicItemPage preloaded with specified number of items from this MusicItemCache")
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

        MusicItemCache<T> musicItemCache = new MusicItemCache<>(2);
        musicItemCache.setItems(List.of(album1, album2, album3, album4, album5));
        musicItemCache.nextPage();

        MusicItemCache.MusicItemPage<T> testedPage = musicItemCache.prevPage();

        assertAll(() -> assertNotNull(testedPage),
                  () -> assertEquals(1, testedPage.currentPage()),
                  () -> assertEquals(3, testedPage.lastPage()),
                  () -> assertEquals(List.of(album1, album2), testedPage.list())
        );
    }

    @Test
    @DisplayName("prevPage() should return empty unmodifiable instance of MusicItemPage if MusicItemCache items not set")
    void prevPage_ShouldReturnEmptyUnmodifiableInstanceOfPageIfCacheItemsNotSet() {
        MusicItemCache<T> musicItemCache = new MusicItemCache<>(3);
        MusicItemCache.MusicItemPage<T> expectedEmptyPage = musicItemCache.prevPage();
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