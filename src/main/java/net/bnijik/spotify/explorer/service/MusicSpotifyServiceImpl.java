package net.bnijik.spotify.explorer.service;

import net.bnijik.spotify.explorer.model.Album;
import net.bnijik.spotify.explorer.model.Category;
import net.bnijik.spotify.explorer.model.Playlist;
import net.bnijik.spotify.explorer.utils.SpotifyResponseParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private final SpotifyResponseParser<String> responseParser;

    @Autowired
    public MusicSpotifyServiceImpl(SpotifyResponseParser<String> responseParser) {
        this.responseParser = responseParser;
    }

    @Override
    public List<Album> getNewAlbums(String accessToken) {
        String uri = baseUri + newAlbumsPath + query;
        String newAlbumsJson = getJson(uri, accessToken);

        List<Album> albumList = responseParser.parseNewAlbums(newAlbumsJson);
        return Collections.unmodifiableList(albumList);
    }

    @Override
    public List<Category> getCategories(String accessToken) {
        String uri = baseUri + categoriesPath + query;
        String categoriesJson = getJson(uri, accessToken);

        List<Category> categoryList = responseParser.parseCategories(categoriesJson);
        return Collections.unmodifiableList(categoryList);
    }

    @Override
    public List<Playlist> getPlaylists(String accessToken) {
        String uri = baseUri + featuredPath + query;
        String featuredPlaylistsJson = getJson(uri, accessToken);

        final List<Playlist> featuredPlaylists = responseParser.parsePlaylists(featuredPlaylistsJson, "featured");
        return Collections.unmodifiableList(featuredPlaylists);
    }

    @Override
    public List<Playlist> getPlaylists(Category category, String accessToken) {
        String uri = baseUri + categoriesPath + "/" + category.getId() + "/playlists" + query;
        String categoryPlaylistsJson = getJson(uri, accessToken);

        final List<Playlist> categoryPlaylists = responseParser.parsePlaylists(categoryPlaylistsJson, category.getName());
        return Collections.unmodifiableList(categoryPlaylists);
    }

    private String getJson(String uri, String accessToken) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
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
