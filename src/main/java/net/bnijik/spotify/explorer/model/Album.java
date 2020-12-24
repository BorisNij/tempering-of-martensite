package net.bnijik.spotify.explorer.model;

import java.util.Objects;

public class Album implements MusicItem {

    private final String albumName;
    private final String artistNames;
    private final String url;

    public Album(String albumName, String artistNames, String url) {
        this.albumName = albumName;
        this.artistNames = artistNames;
        this.url = url;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtistNames() {
        return artistNames;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String description() {
        return
                albumName + "\n" +
                artistNames + "\n" +
                url + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;

        if (!Objects.equals(albumName, album.albumName)) return false;
        return Objects.equals(artistNames, album.artistNames);
    }

    @Override
    public int hashCode() {
        int result = albumName != null ? albumName.hashCode() : 0;
        result = 31 * result + (artistNames != null ? artistNames.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Album{" +
               "albumName='" + albumName + '\'' +
               ", artistNames='" + artistNames + '\'' +
               ", url='" + url + '\'' +
               '}';
    }
}
