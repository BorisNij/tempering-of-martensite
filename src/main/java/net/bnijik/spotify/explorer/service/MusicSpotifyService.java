package net.bnijik.spotify.explorer.service;

import net.bnijik.spotify.explorer.model.Album;
import net.bnijik.spotify.explorer.model.Category;
import net.bnijik.spotify.explorer.model.Playlist;

import java.util.List;

public interface MusicSpotifyService {
    List<Album> getNewAlbums(String accessToken);

    List<Category> getCategories(String accessToken);

    List<Playlist> getPlaylists(String accessToken);

    List<Playlist> getPlaylists(Category category, String accessToken);
}
