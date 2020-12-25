package net.bnijik.spotify.explorer.service;

import net.bnijik.spotify.explorer.configuration.MusicSpotifyConfig;
import net.bnijik.spotify.explorer.model.Album;
import net.bnijik.spotify.explorer.model.Category;
import net.bnijik.spotify.explorer.model.Playlist;
import net.bnijik.spotify.explorer.utils.SpotifyResponseParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

/**
 * Consumes Spotify's REST api to fetch music items using a pre-obtained Access Token.
 * Uses an {@link HttpClient} to send a <i>GET</i> {@link HttpRequest} to Spotify api
 * with the Access Token in the request header, receives a JSON string from Spotify
 * ({@link #getJson(String, String)} method). <p>
 * <p>
 * Uses an {@link SpotifyResponseParser} to parse the received JSON string and
 * produce a list of music items (i.e. list of {@link Album}, list of {@link Category}
 * list of {@link Playlist} objects. Each {@code public} method returns an unmodifiable
 * view of such a list.<p>
 */
@Service
public class MusicSpotifyServiceImpl implements MusicSpotifyService {

    private final SpotifyResponseParser<String> responseParser;
    private final MusicSpotifyConfig musicSpotifyConfig;
    private final HttpRequest.Builder httpRequestBuilder;

    @Autowired
    public MusicSpotifyServiceImpl(SpotifyResponseParser<String> responseParser, MusicSpotifyConfig musicSpotifyConfig, HttpRequest.Builder httpRequestBuilder) {
        this.responseParser = responseParser;
        this.musicSpotifyConfig = musicSpotifyConfig;
        this.httpRequestBuilder = httpRequestBuilder;
    }

    @Override
    public List<Album> getNewAlbums(String accessToken) {
        String uri = musicSpotifyConfig.getBaseUri() + musicSpotifyConfig.getNewAlbumsPath() + musicSpotifyConfig.getQuery();
        String newAlbumsJson = getJson(uri, accessToken);

        List<Album> albumList = responseParser.parseNewAlbums(newAlbumsJson);
        return Collections.unmodifiableList(albumList);
    }

    @Override
    public List<Category> getCategories(String accessToken) {
        String uri = musicSpotifyConfig.getBaseUri() + musicSpotifyConfig.getCategoriesPath() + musicSpotifyConfig.getQuery();
        String categoriesJson = getJson(uri, accessToken);

        List<Category> categoryList = responseParser.parseCategories(categoriesJson);
        return Collections.unmodifiableList(categoryList);
    }

    @Override
    public List<Playlist> getPlaylists(String accessToken) {
        String uri = musicSpotifyConfig.getBaseUri() + musicSpotifyConfig.getFeaturedPath() + musicSpotifyConfig.getQuery();
        String featuredPlaylistsJson = getJson(uri, accessToken);

        final List<Playlist> featuredPlaylists = responseParser.parsePlaylists(featuredPlaylistsJson, "featured");
        return Collections.unmodifiableList(featuredPlaylists);
    }

    @Override
    public List<Playlist> getPlaylists(Category category, String accessToken) {
        String uri = musicSpotifyConfig.getBaseUri() + musicSpotifyConfig.getCategoriesPath() + "/" + category.getId() + "/playlists" + musicSpotifyConfig.getQuery();
        String categoryPlaylistsJson = getJson(uri, accessToken);

        final List<Playlist> categoryPlaylists = responseParser.parsePlaylists(categoryPlaylistsJson, category.getName());
        return Collections.unmodifiableList(categoryPlaylists);
    }

    private String getJson(String uri, String accessToken) {
        HttpRequest httpRequest = httpRequestBuilder
                .header("Authorization", "Bearer " + accessToken)
                .uri(URI.create(uri))
                .GET()
                .build();

        try {
            HttpClient client = HttpClient.newBuilder()
                    .build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (InterruptedException | IOException | NullPointerException e) {
            return null;
        }
    }
}
