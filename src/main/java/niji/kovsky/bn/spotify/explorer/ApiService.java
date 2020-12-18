package niji.kovsky.bn.spotify.explorer;

import niji.kovsky.bn.spotify.explorer.model.Album;
import niji.kovsky.bn.spotify.explorer.model.Category;
import niji.kovsky.bn.spotify.explorer.model.Playlist;

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
 * Uses an {@link ApiResponseParser} to parse the received JSON string and
 * produce a list of music items (i.e. list of {@link Album}, list of {@link Category}
 * list of {@link Playlist} objects. Each {@code public} method returns an unmodifiable
 * view of such a list.<p>
 */
public class ApiService {

    //TODO: move properties to Resources
    private static final String SCHEME = "https://";
    private static final String API_HOST = "api.spotify.com";
    private static final String CATEGORIES_PATH = "/v1/browse/categories";
    private static final String NEW_ALBUMS_PATH = "/v1/browse/new-releases";
    private static final String FEATURED_PATH = "/v1/browse/featured-playlists";
    private static final String QUERY = "?limit=50";

    private final ApiResponseParser<String> responseParser;

    public ApiService(ApiResponseParser<String> responseParser) {
        this.responseParser = responseParser;
    }

    public List<Album> getNewAlbums(String accessToken) {
        String uri = SCHEME + API_HOST + NEW_ALBUMS_PATH + QUERY;
        String newAlbumsJson = getJson(uri, accessToken);

        List<Album> albumList = this.responseParser.parseNewAlbums(newAlbumsJson);
        return Collections.unmodifiableList(albumList);
    }

    public List<Category> getCategories(String accessToken) {
        String uri = SCHEME + API_HOST + CATEGORIES_PATH + QUERY;
        String categoriesJson = getJson(uri, accessToken);

        List<Category> categoryList = this.responseParser.parseCategories(categoriesJson);
        return Collections.unmodifiableList(categoryList);
    }

    public List<Playlist> getPlaylists(String accessToken) {
        String uri = SCHEME + API_HOST + FEATURED_PATH + QUERY;
        String featuredPlaylistsJson = getJson(uri, accessToken);

        final List<Playlist> featuredPlaylists = this.responseParser.parsePlaylists(featuredPlaylistsJson, "featured");
        return Collections.unmodifiableList(featuredPlaylists);
    }

    public List<Playlist> getPlaylists(Category category, String accessToken) {
        String uri = SCHEME + API_HOST + CATEGORIES_PATH + "/" + category.getId() + "/playlists" + QUERY;
        String categoryPlaylistsJson = getJson(uri, accessToken);

        final List<Playlist> categoryPlaylists = this.responseParser.parsePlaylists(categoryPlaylistsJson, category.getName());
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
