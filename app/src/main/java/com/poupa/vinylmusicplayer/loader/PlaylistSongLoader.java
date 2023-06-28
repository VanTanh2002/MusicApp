package com.poupa.vinylmusicplayer.loader;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.poupa.vinylmusicplayer.discog.Discography;
import com.poupa.vinylmusicplayer.model.Playlist;
import com.poupa.vinylmusicplayer.model.PlaylistSong;
import com.poupa.vinylmusicplayer.model.Song;
import com.poupa.vinylmusicplayer.util.StringUtil;

import java.util.ArrayList;

public class PlaylistSongLoader {
    @NonNull
    public static ArrayList<Song> getPlaylistSongList(@NonNull final Context context, final long playlistId) {
        ArrayList<Song> songs = new ArrayList<>();
        try (Cursor cursor = makePlaylistSongCursor(context, playlistId)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    PlaylistSong song = getPlaylistSongFromCursorImpl(cursor, playlistId);
                    if (!song.equals(Song.EMPTY_SONG)) {
                        songs.add(song);
                    }
                } while (cursor.moveToNext());
            }
            return songs;
        }
    }

    @NonNull
    public static ArrayList<Song> getPlaylistSongList(
            @NonNull final Context context,
            @NonNull final String playlistNameSearchTerm) {

        final String selection = StringUtil.join(MediaStore.Audio.Playlists.NAME, " LIKE '%' || ? ||'%'");
        final Cursor cursor = PlaylistLoader.makePlaylistCursor(context, selection, new String[]{
                playlistNameSearchTerm// 0
        });

        final String lowercaseSearchTerm = playlistNameSearchTerm.toLowerCase();
        Playlist match = null;
        for(Playlist playlist : PlaylistLoader.getAllPlaylists(cursor)) {
            if (match == null) {
                match = playlist;
            } else {
                match = closerMatch(lowercaseSearchTerm, match, playlist);
            }
        }

        if (match == null) {
            return new ArrayList<>();
        }

        return getPlaylistSongList(context, match.id);
    }

    @NonNull private static Playlist closerMatch(
            @NonNull final String playlistNameSearchTerm,
            @NonNull final Playlist first,
            @NonNull final Playlist second) {
        final StringUtil.ClosestMatch match = StringUtil.closestOfMatches(
                playlistNameSearchTerm,
                first.name.toLowerCase(),
                second.name.toLowerCase());
        if (match != StringUtil.ClosestMatch.SECOND) {
            return first;
        } else {
            return second;
        }
    }

    @NonNull
    private static PlaylistSong getPlaylistSongFromCursorImpl(@NonNull Cursor cursor, long playlistId) {
        final long id = cursor.getLong(0);
        final int idInPlaylist = cursor.getInt(1);

        Song song = Discography.getInstance().getSong(id);
        return new PlaylistSong(song, playlistId, idInPlaylist);
    }

    @Nullable
    private static Cursor makePlaylistSongCursor(@NonNull final Context context, final long playlistId) {
        try {
            return context.getContentResolver().query(
                    MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId),
                    new String[]{
                            MediaStore.Audio.Playlists.Members.AUDIO_ID,// 0
                            MediaStore.Audio.Playlists.Members._ID // 1
                    },
                    MediaStore.Audio.AudioColumns.IS_MUSIC + "=1" + " AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''",
                    null,
                    MediaStore.Audio.Playlists.Members.DEFAULT_SORT_ORDER);
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
}
