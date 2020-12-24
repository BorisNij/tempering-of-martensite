package net.bnijik.spotify.explorer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MusicSpotifyConfig {
    @Value("${spotify.music.base-uri}")
    private String baseUri;

    @Value("${spotify.music.categories-path}")
    private String categoriesPath;

    @Value("${spotify.music.new-albums-path}")
    private String newAlbumsPath;

    @Value("${spotify.music.featured-path}")
    private String featuredPath;

    @Value("${spotify.music.query}")
    private String query;

    public String getBaseUri() {
        return baseUri;
    }

    public String getCategoriesPath() {
        return categoriesPath;
    }

    public String getNewAlbumsPath() {
        return newAlbumsPath;
    }

    public String getFeaturedPath() {
        return featuredPath;
    }

    public String getQuery() {
        return query;
    }
}
