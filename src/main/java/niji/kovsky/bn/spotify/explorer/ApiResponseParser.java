package niji.kovsky.bn.spotify.explorer;

import niji.kovsky.bn.spotify.explorer.model.Album;
import niji.kovsky.bn.spotify.explorer.model.Category;
import niji.kovsky.bn.spotify.explorer.model.Playlist;

import java.util.List;

public interface ApiResponseParser<T> {

    List<Album> parseNewAlbums(T newAlbumsResponseBody);

    List<Category> parseCategories(T categoriesResponseBody);

    List<Playlist> parsePlaylists(T featuredPlaylistsResponseBody, String parentCategory);

    String parseAccessToken(T tokenResponseBody);
}
