package niji.kovsky.bn.spotify.explorer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import niji.kovsky.bn.spotify.explorer.model.Album;
import niji.kovsky.bn.spotify.explorer.model.Category;
import niji.kovsky.bn.spotify.explorer.model.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses JSON strings to produce the Access Token or to generate a list of {@link niji.kovsky.bn.spotify.explorer.model.MusicItem}.
 * Uses the {@link com.google.gson} library for parsing.
 */
public class JsonStringResponseParserImpl implements ApiResponseParser<String> {
    @Override
    public List<Album> parseNewAlbums(String newAlbumsJson) {
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
        return albumList;
    }

    @Override
    public List<Category> parseCategories(String categoriesJson) {
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
        return categoryList;
    }

    @Override
    public List<Playlist> parsePlaylists(String playlistsResponseBody, String parentCategory) {
        List<Playlist> featuredList = new ArrayList<>();
        JsonArray playlists = getArrayFromJson(playlistsResponseBody, "playlists");

        for (JsonElement ps : playlists) {
            String name = getElementAsString(ps, "name");
            String url = getUrlAsString(ps);
            featuredList.add(new Playlist(name, url, parentCategory));
        }
        return featuredList;
    }

    @Override
    public String parseAccessToken(String tokenJson) {
        return JsonParser.parseString(tokenJson)
                .getAsJsonObject()
                .get("access_token")
                .getAsString();
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
}
