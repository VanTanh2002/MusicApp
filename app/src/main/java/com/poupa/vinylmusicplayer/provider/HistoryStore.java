package com.poupa.vinylmusicplayer.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HistoryStore extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "history.db";
    private static final int VERSION = 1;
    @Nullable
    private static HistoryStore sInstance = null;

    public HistoryStore(final Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(@NonNull final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + RecentStoreColumns.NAME + " ("
                + RecentStoreColumns.ID + " LONG NOT NULL," + RecentStoreColumns.TIME_PLAYED
                + " LONG NOT NULL);");
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecentStoreColumns.NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecentStoreColumns.NAME);
        onCreate(db);
    }

    @NonNull
    public static synchronized HistoryStore getInstance(@NonNull final Context context) {
        if (sInstance == null) {
            sInstance = new HistoryStore(context.getApplicationContext());
        }
        return sInstance;
    }

    public void addSongId(final long songId) {
        if (songId == -1) {
            return;
        }

        final SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();

        try {
            // remove previous entries
            removeSongId(database, songId);

            // add the entry
            final ContentValues values = new ContentValues(2);
            values.put(RecentStoreColumns.ID, songId);
            values.put(RecentStoreColumns.TIME_PLAYED, System.currentTimeMillis());
            database.insert(RecentStoreColumns.NAME, null, values);
        } finally {
            database.setTransactionSuccessful();
            database.endTransaction();
        }
    }

    public void removeSongIds(@NonNull ArrayList<Long> missingIds) {
        if (missingIds.isEmpty()) return;

        final SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            for (long id : missingIds) {
                removeSongId(database, id);
            }
        } finally {
            database.setTransactionSuccessful();
            database.endTransaction();
        }
    }

    private void removeSongId(@NonNull final SQLiteDatabase database, final long songId) {
        database.delete(RecentStoreColumns.NAME, RecentStoreColumns.ID + " = ?", new String[]{
                String.valueOf(songId)
        });
    }

    public void clear() {
        final SQLiteDatabase database = getWritableDatabase();
        database.delete(RecentStoreColumns.NAME, null, null);
    }

    public boolean contains(long id) {
        final SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(RecentStoreColumns.NAME,
                new String[]{RecentStoreColumns.ID},
                RecentStoreColumns.ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        boolean containsId = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return containsId;
    }

    @NonNull
    public ArrayList<Long> getRecentIds(long cutoff) {
        try (Cursor cursor = queryRecentIds(cutoff)) {
            return StoreLoader.getIdsFromCursor(cursor, RecentStoreColumns.ID);
        }
    }

    private Cursor queryRecentIds(long cutoff) {
        final boolean noCutoffTime = (cutoff == 0);
        final boolean reverseOrder = (cutoff < 0);
        if (reverseOrder) cutoff = -cutoff;

        final SQLiteDatabase database = getReadableDatabase();

        return database.query(RecentStoreColumns.NAME,
                new String[]{RecentStoreColumns.ID},
                noCutoffTime ? null : RecentStoreColumns.TIME_PLAYED + (reverseOrder ? "<?" : ">?"),
                noCutoffTime ? null : new String[]{String.valueOf(cutoff)},
                null, null,
                RecentStoreColumns.TIME_PLAYED + (reverseOrder ? " ASC" : " DESC"));
    }

    public interface RecentStoreColumns {
        String NAME = "recent_history";

        String ID = "song_id";
        String TIME_PLAYED = "time_played";
    }
}