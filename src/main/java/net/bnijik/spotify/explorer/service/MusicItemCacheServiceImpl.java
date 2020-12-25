package net.bnijik.spotify.explorer.service;

import net.bnijik.spotify.explorer.data.MusicItemCache;
import net.bnijik.spotify.explorer.model.Album;
import net.bnijik.spotify.explorer.model.Category;
import net.bnijik.spotify.explorer.model.MusicItem;
import net.bnijik.spotify.explorer.model.Playlist;

public class MusicItemCacheServiceImpl extends CacheService<MusicItemCache<? extends MusicItem>> {
    @Override
    public MusicItemCache<Album> provideNewAlbumsCache(String cacheId, int itemsPerPage) {
        //noinspection unchecked
        return (MusicItemCache<Album>) super.cacheMap()
                .computeIfAbsent(cacheId, (newAlbumsCache) -> new MusicItemCache<>(itemsPerPage));
    }

    @Override
    public MusicItemCache<Playlist> providePlaylistsCache(String cacheId, int itemsPerPage) {
        //noinspection unchecked
        return (MusicItemCache<Playlist>) super.cacheMap()
                .computeIfAbsent(cacheId, (featuredPlaylistsCache) -> new MusicItemCache<>(itemsPerPage));
    }

    @Override
    public MusicItemCache<Category> provideCategoriesCache(String cacheId, int itemsPerPage) {
        //noinspection unchecked
        return (MusicItemCache<Category>) super.cacheMap()
                .computeIfAbsent(cacheId, (categoriesCache) -> new MusicItemCache<>(itemsPerPage));
    }

}
