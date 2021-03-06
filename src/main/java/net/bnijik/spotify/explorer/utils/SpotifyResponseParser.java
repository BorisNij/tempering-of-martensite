package net.bnijik.spotify.explorer.utils;

import net.bnijik.spotify.explorer.model.Album;
import net.bnijik.spotify.explorer.model.Category;
import net.bnijik.spotify.explorer.model.Playlist;

import java.util.List;

/**
 * Stipulates parsing and deserialization of Spotify api responses into lists of POJOs or
 * an Access Token string. Implemented by {@link SpotifyResponseParserImpl}.
 *
 * @param <T> api response type
 */
public interface SpotifyResponseParser<T> {

    List<Album> parseNewAlbums(T newAlbumsResponseBody);

    List<Category> parseCategories(T categoriesResponseBody);

    List<Playlist> parsePlaylists(T playlistsResponseBody, String parentCategory);

    String parseAccessToken(T tokenResponseBody);
}
