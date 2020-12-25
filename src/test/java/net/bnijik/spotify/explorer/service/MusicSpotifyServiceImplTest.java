package net.bnijik.spotify.explorer.service;

import net.bnijik.spotify.explorer.configuration.MusicSpotifyConfig;
import net.bnijik.spotify.explorer.model.Album;
import net.bnijik.spotify.explorer.model.Category;
import net.bnijik.spotify.explorer.model.Playlist;
import net.bnijik.spotify.explorer.utils.SpotifyResponseParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MusicSpotifyServiceImplTest {

    @Mock
    private SpotifyResponseParser<String> responseParser;

    @Mock
    private MusicSpotifyConfig musicSpotifyConfig;

    @Mock
    private HttpClient.Builder httpClientBuilder;

    @InjectMocks
    private MusicSpotifyServiceImpl musicSpotifyService;


    @Test
    void getNewAlbums() {
        final Album someAlbum = new Album("albumName", "[artist1, artist2]", "http://spotify.album.url");
        final ArrayList<Album> mutableListWithSomeAlbum = new ArrayList<>();
        mutableListWithSomeAlbum.add(someAlbum);

        when(musicSpotifyConfig.getBaseUri()).thenReturn("http://some.url");

        when(responseParser.parseNewAlbums(null)).thenReturn(mutableListWithSomeAlbum);
        final List<Album> actual = musicSpotifyService.getNewAlbums("dummy token");

        assertEquals(mutableListWithSomeAlbum, actual);
        assertThrows(UnsupportedOperationException.class, () -> actual.add(someAlbum));
    }

    @Test
    void getPlaylists() {
        final Playlist somePlaylist = new Playlist("PlaylistName", "http://spotify.album.url", "some parent category");
        final ArrayList<Playlist> mutableListWithSomePlaylist = new ArrayList<>();
        mutableListWithSomePlaylist.add(somePlaylist);

        when(musicSpotifyConfig.getBaseUri()).thenReturn("http://some.url");

        when(responseParser.parsePlaylists(null, "dummy category name")).thenReturn(mutableListWithSomePlaylist);
        Category someCategory = new Category("dummy category name", "dummy_category_id");
        final List<Playlist> actual = musicSpotifyService.getPlaylists(someCategory, "dummy token");

        assertEquals(mutableListWithSomePlaylist, actual);
        assertThrows(UnsupportedOperationException.class, () -> actual.add(somePlaylist));
    }
}