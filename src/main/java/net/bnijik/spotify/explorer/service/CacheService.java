package net.bnijik.spotify.explorer.service;

import java.util.HashMap;
import java.util.Map;

public abstract class CacheService<Cache> {
    private final Map<String, Cache> cacheMap = new HashMap<>();

    public Map<String, Cache> cacheMap() {
        return cacheMap;
    }

    public abstract Cache provideNewAlbumsCache(String cacheId, int itemsPerPage);

    public abstract Cache providePlaylistsCache(String cacheId, int itemsPerPage);

    public abstract Cache provideCategoriesCache(String cacheId, int itemsPerPage);

}
