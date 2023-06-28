package com.poupa.vinylmusicplayer.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SongPlayCountStore extends SQLiteOpenHelper {
    @Nullable
    private static SongPlayCountStore sInstance = null;

    public static final String DATABASE_NAME = "song_play_count.db";
    private static final int VERSION = 3;

    @NonNull
    private static final Interpolator sInterpolator = new AccelerateInterpolator(1.5f);

    private static final int NUM_WEEKS = 52;

    private static final int INTERPOLATOR_HEIGHT = 50;

    private static final int INTERPOLATOR_BASE = 25;

    private static final int ONE_WEEK_IN_MS = 1000 * 60 * 60 * 24 * 7;

    @NonNull
    private static final String WHERE_ID_EQUALS = SongPlayCountColumns.ID + "=?";

    private final int mNumberOfWeeksSinceEpoch;

    public SongPlayCountStore(final Context context) {
        super(context, DATABASE_NAME, null, VERSION);

        long msSinceEpoch = System.currentTimeMillis();
        mNumberOfWeeksSinceEpoch = (int) (msSinceEpoch / ONE_WEEK_IN_MS);

        updateResults();
    }

    @Override
    public void onCreate(@NonNull final SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(SongPlayCountColumns.NAME);
        builder.append("(");
        builder.append(SongPlayCountColumns.ID);
        builder.append(" LONG UNIQUE,");

        for (int i = 0; i < NUM_WEEKS; i++) {
            builder.append(getColumnNameForWeek(i));
            builder.append(" INT DEFAULT 0,");
        }

        builder.append(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX);
        builder.append(" INT NOT NULL,");

        builder.append(SongPlayCountColumns.PLAY_COUNT_SCORE);
        builder.append(" REAL DEFAULT 0);");

        db.execSQL(builder.toString());
    }

    @Override
    public void onUpgrade(@NonNull final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SongPlayCountColumns.NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // If we ever have downgrade, drop the table to be safe
        db.execSQL("DROP TABLE IF EXISTS " + SongPlayCountColumns.NAME);
        onCreate(db);
    }

    @NonNull
    public static synchronized SongPlayCountStore getInstance(@NonNull final Context context) {
        if (sInstance == null) {
            sInstance = new SongPlayCountStore(context.getApplicationContext());
        }
        return sInstance;
    }

    public void bumpPlayCount(final long songId) {
        if (songId == -1) {
            return;
        }

        final SQLiteDatabase database = getWritableDatabase();
        // begin the transaction
        database.beginTransaction();

        // get the cursor of this content inside the transaction
        try (final Cursor cursor = database.query(SongPlayCountColumns.NAME, null, WHERE_ID_EQUALS,
                new String[]{String.valueOf(songId)}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                updateExistingRow(database, cursor, true);
            } else {
                // if we have no existing results, create a new one
                createNewPlayedEntry(database, songId);
            }
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void createNewPlayedEntry(@NonNull final SQLiteDatabase database, final long songId) {
        // no row exists, create a new one
        float newScore = getScoreMultiplierForWeek(0);
        int newPlayCount = 1;

        final ContentValues values = new ContentValues(3);
        values.put(SongPlayCountColumns.ID, songId);
        values.put(SongPlayCountColumns.PLAY_COUNT_SCORE, newScore);
        values.put(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX, mNumberOfWeeksSinceEpoch);
        values.put(getColumnNameForWeek(0), newPlayCount);

        database.insert(SongPlayCountColumns.NAME, null, values);
    }

    private void updateExistingRow(@NonNull final SQLiteDatabase database, @NonNull Cursor cursor, boolean bumpCount) {
        final long id = cursor.getLong(cursor.getColumnIndex(SongPlayCountColumns.ID));
        final String stringId = String.valueOf(id);

        // figure how many weeks since we last updated
        final int lastUpdatedWeek = cursor.getInt(cursor.getColumnIndex(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX));
        int weekDiff = mNumberOfWeeksSinceEpoch - lastUpdatedWeek;

        // if it's more than the number of weeks we track, delete it and create a new entry
        if (Math.abs(weekDiff) >= NUM_WEEKS) {
            // this entry needs to be dropped since it is too outdated
            deleteEntry(database, stringId);
            if (bumpCount) {
                createNewPlayedEntry(database, id);
            }
        } else if (weekDiff != 0) {
            // else, shift the weeks
            int[] playCounts = new int[NUM_WEEKS];

            if (weekDiff > 0) {
                // time is shifted forwards
                for (int i = 0; i < NUM_WEEKS - weekDiff; i++) {
                    playCounts[i + weekDiff] = cursor.getInt(getColumnIndexForWeek(i));
                }
            } else {
                for (int i = 0; i < NUM_WEEKS + weekDiff; i++) {
                    playCounts[i] = cursor.getInt(getColumnIndexForWeek(i - weekDiff));
                }
            }

            // bump the count
            if (bumpCount) {
                playCounts[0]++;
            }

            float score = calculateScore(playCounts);

            if (score < .01f) {
                deleteEntry(database, stringId);
            } else {
                // create the content values
                ContentValues values = new ContentValues(NUM_WEEKS + 2);
                values.put(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX, mNumberOfWeeksSinceEpoch);
                values.put(SongPlayCountColumns.PLAY_COUNT_SCORE, score);

                for (int i = 0; i < NUM_WEEKS; i++) {
                    values.put(getColumnNameForWeek(i), playCounts[i]);
                }

                // update the entry
                database.update(SongPlayCountColumns.NAME, values, WHERE_ID_EQUALS,
                        new String[]{stringId});
            }
        } else if (bumpCount) {
            // else no shifting, just update the scores
            ContentValues values = new ContentValues(2);

            // increase the score by a single score amount
            int scoreIndex = cursor.getColumnIndex(SongPlayCountColumns.PLAY_COUNT_SCORE);
            float score = cursor.getFloat(scoreIndex) + getScoreMultiplierForWeek(0);
            values.put(SongPlayCountColumns.PLAY_COUNT_SCORE, score);

            // increase the play count by 1
            values.put(getColumnNameForWeek(0), cursor.getInt(getColumnIndexForWeek(0)) + 1);

            // update the entry
            database.update(SongPlayCountColumns.NAME, values, WHERE_ID_EQUALS,
                    new String[]{stringId});
        }
    }

    public void clear() {
        final SQLiteDatabase database = getWritableDatabase();
        database.delete(SongPlayCountColumns.NAME, null, null);
    }

    public Cursor getTopPlayedResults(int numResults) {
        final SQLiteDatabase database = getReadableDatabase();
        return database.query(SongPlayCountColumns.NAME, new String[]{SongPlayCountColumns.ID},
                null, null, null, null, SongPlayCountColumns.PLAY_COUNT_SCORE + " DESC",
                (numResults <= 0 ? null : String.valueOf(numResults)));
    }

    private synchronized void updateResults() {
        final SQLiteDatabase database = getWritableDatabase();

        database.beginTransaction();

        int oldestWeekWeCareAbout = mNumberOfWeeksSinceEpoch - NUM_WEEKS + 1;
        // delete rows we don't care about anymore
        database.delete(SongPlayCountColumns.NAME, SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX
                + " < " + oldestWeekWeCareAbout, null);

        // get the remaining rows
        try (final Cursor cursor = database.query(SongPlayCountColumns.NAME,
                null, null, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                // for each row, update it
                do {
                    updateExistingRow(database, cursor, false);
                } while (cursor.moveToNext());
            }
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void deleteEntry(@NonNull final SQLiteDatabase database, final String stringId) {
        database.delete(SongPlayCountColumns.NAME, WHERE_ID_EQUALS, new String[]{stringId});
    }

    private static float calculateScore(@Nullable final int[] playCounts) {
        if (playCounts == null) {
            return 0;
        }

        float score = 0;
        for (int i = 0; i < Math.min(playCounts.length, NUM_WEEKS); i++) {
            score += playCounts[i] * getScoreMultiplierForWeek(i);
        }

        return score;
    }

    @NonNull
    private static String getColumnNameForWeek(final int week) {
        return SongPlayCountColumns.WEEK_PLAY_COUNT + week;
    }

    private static float getScoreMultiplierForWeek(final int week) {
        return sInterpolator.getInterpolation(1 - (week / (float) NUM_WEEKS)) * INTERPOLATOR_HEIGHT
                + INTERPOLATOR_BASE;
    }

    private static int getColumnIndexForWeek(final int week) {
        // ID, followed by the weeks columns
        return 1 + week;
    }

    public interface SongPlayCountColumns {

        String NAME = "song_play_count";

        String ID = "song_id";
        String WEEK_PLAY_COUNT = "week";
        String LAST_UPDATED_WEEK_INDEX = "week_index";
        String PLAY_COUNT_SCORE = "play_count_score";
    }
}
