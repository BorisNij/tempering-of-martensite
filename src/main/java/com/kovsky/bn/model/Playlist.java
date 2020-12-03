package com.kovsky.bn.model;

public class Playlist implements MusicItem {

    private final String name;
    private final String url;
    private final String category;

    public Playlist(String name, String url, String category) {
        this.name = name;
        this.url = url;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String description() {
        return
                this.name + "\n" +
                this.url + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Playlist playlist = (Playlist) o;

        if (!name.equals(playlist.name)) return false;
        return url.equals(playlist.url);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Playlist{" +
               "name='" + name + '\'' +
               ", url='" + url + '\'' +
               '}';
    }
}
