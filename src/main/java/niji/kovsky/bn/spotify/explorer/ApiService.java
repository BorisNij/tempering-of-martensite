package niji.kovsky.bn.spotify.explorer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import niji.kovsky.bn.spotify.explorer.model.Album;
import niji.kovsky.bn.spotify.explorer.model.Category;
import niji.kovsky.bn.spotify.explorer.model.Playlist;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiService {

    private static final String SCHEME = "https://";
    private static final String API_HOST = "api.spotify.com";
    private static final String CATEGORIES_PATH = "/v1/browse/categories";
    private static final String NEW_ALBUMS_PATH = "/v1/browse/new-releases";
    private static final String FEATURED_PATH = "/v1/browse/featured-playlists";
    private static final String QUERY = "?limit=50";

    public List<Album> getNewAlbums(String accessToken) {
        String uri = SCHEME + API_HOST + NEW_ALBUMS_PATH + QUERY;
        String newAlbumsJson = getJson(uri, accessToken);

        if (newAlbumsJson == null) {
            return null;
        }

        List<Album> albumList = new ArrayList<>();
        JsonArray albumElements = getArrayFromJson(newAlbumsJson, "albums");

        for (JsonElement al : albumElements) {
            String name = getElementAsString(al, "name");
            String url = getUrlAsString(al);
            JsonArray artistElements = al.getAsJsonObject()
                    .getAsJsonArray("artists");

            List<String> artistList = new ArrayList<>();
            for (JsonElement ar : artistElements) {
                artistList.add(getElementAsString(ar, "name"));
            }
            albumList.add(new Album(name, artistList.toString(), url));
        }
        return Collections.unmodifiableList(albumList);
    }

    public List<Category> getCategories(String accessToken) {
        String uri = SCHEME + API_HOST + CATEGORIES_PATH + QUERY;
        String categoriesJson = getJson(uri, accessToken);

        if (categoriesJson == null) {
            return null;
        }

        List<Category> categoryList = new ArrayList<>();
        JsonArray categoryElements = getArrayFromJson(categoriesJson, "categories");

        for (JsonElement ca : categoryElements) {
            String name = getElementAsString(ca, "name");
            String id = getElementAsString(ca, "id");
            categoryList.add(new Category(name, id));
        }
        return Collections.unmodifiableList(categoryList);
    }

    public List<Playlist> getPlaylists(String accessToken) {
        String uri = SCHEME + API_HOST + FEATURED_PATH + QUERY;
        String featuredPlaylistsJson = getJson(uri, accessToken);

        return Collections.unmodifiableList(getPlaylists(featuredPlaylistsJson, "featured"));
    }

    public List<Playlist> getPlaylists(Category category, String accessToken) {
        String uri = SCHEME + API_HOST + CATEGORIES_PATH + "/" + category.getId() + "/playlists" + QUERY;
        String categoryPlaylistsJson = getJson(uri, accessToken);

        return Collections.unmodifiableList(getPlaylists(categoryPlaylistsJson, category.getName()));
    }

    private List<Playlist> getPlaylists(String playlistsJson, String parentCategory) {
        List<Playlist> featuredList = new ArrayList<>();
        JsonArray playlists = getArrayFromJson(playlistsJson, "playlists");

        for (JsonElement ps : playlists) {
            String name = getElementAsString(ps, "name");
            String url = getUrlAsString(ps);
            featuredList.add(new Playlist(name, url, parentCategory));
        }
        return featuredList;
    }

    private String getUrlAsString(JsonElement element) {
        return element.getAsJsonObject()
                .getAsJsonObject("external_urls")
                .get("spotify")
                .getAsString();
    }

    private String getElementAsString(JsonElement element, String elementType) {
        return element.getAsJsonObject()
                .get(elementType)
                .getAsString();
    }

    private JsonArray getArrayFromJson(String json, String spotifyItem) {
        return JsonParser.parseString(json)
                .getAsJsonObject()
                .getAsJsonObject(spotifyItem)
                .getAsJsonArray("items");

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

            assert response != null;
            return response.body();

        } catch (InterruptedException | IOException e) {
            return "Error response";
        }
    }
}
