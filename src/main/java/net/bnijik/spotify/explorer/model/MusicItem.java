package net.bnijik.spotify.explorer.model;

/**
 * The supertype of all music items in this app. Implemented by {@link Album}, {@link Category} and {@link Playlist}.
 */
public interface MusicItem {

    String description();
}
