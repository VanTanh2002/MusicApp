package com.poupa.vinylmusicplayer.loader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.poupa.vinylmusicplayer.discog.Discography;
import com.poupa.vinylmusicplayer.model.Genre;
import com.poupa.vinylmusicplayer.model.Song;
import com.poupa.vinylmusicplayer.sort.SongSortOrder;
import com.poupa.vinylmusicplayer.util.StringUtil;

import java.util.ArrayList;

public class GenreLoader {
    @NonNull
    public static ArrayList<Genre> getAllGenres() {
        return Discography.getInstance().getAllGenres(
                (g1, g2) -> StringUtil.compareIgnoreAccent(g1.name, g2.name)
        );
    }

    @NonNull
    public static ArrayList<Song> getSongs(final long genreId) {
        ArrayList<Song> songs = Discography.getInstance().getSongsForGenre(genreId, SongSortOrder.BY_ALBUM);
        if (songs == null) {return new ArrayList<>();}
        return songs;
    }

    @NonNull
    public static ArrayList<Song> getGenreSongsByName(final String genreNameSearchTerm) {
        final Genre genre = getGenreByName(genreNameSearchTerm);
        if (genre == null) {
            return new ArrayList<>();
        }
        return getSongs(genre.id);
    }

    @Nullable
    private static Genre getGenreByName(final String genreNameSearchTerm) {
        final String lowercaseSearchTerm = genreNameSearchTerm.toLowerCase();
        final ArrayList<Genre> genres = getAllGenres();
        Genre match = null;
        for(Genre genre : genres) {
            if (genre.name.toLowerCase().contains(lowercaseSearchTerm)) {
                if (match == null) {
                    match = genre;
                } else {
                    match = closerMatch(lowercaseSearchTerm, match, genre);
                }
            }
        }
        return match;
    }

    @NonNull private static Genre closerMatch(
            @NonNull final String genreNameSearchTerm,
            @NonNull final Genre first,
            @NonNull final Genre second) {
        final StringUtil.ClosestMatch match = StringUtil.closestOfMatches(
                genreNameSearchTerm,
                first.name.toLowerCase(),
                second.name.toLowerCase());
        if (match != StringUtil.ClosestMatch.SECOND) {
            return first;
        } else {
            return second;
        }
    }

}
