package com.poupa.vinylmusicplayer.helper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.poupa.vinylmusicplayer.R;
import com.poupa.vinylmusicplayer.dialogs.BottomSheetDialog.BottomSheetDialogWithButtons;
import com.poupa.vinylmusicplayer.discog.Discography;
import com.poupa.vinylmusicplayer.misc.queue.IndexedSong;
import com.poupa.vinylmusicplayer.model.Song;
import com.poupa.vinylmusicplayer.service.MusicService;
import com.poupa.vinylmusicplayer.util.PreferenceUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;

public class MusicPlayerRemote {

    public static final String TAG = MusicPlayerRemote.class.getSimpleName();

    @Nullable
    public static MusicService musicService;

    private static final WeakHashMap<Context, ServiceBinder> mConnectionMap = new WeakHashMap<>();

    public static ServiceToken bindToService(@NonNull final Context context,
                                             final ServiceConnection callback) {
        Activity realActivity = ((Activity) context).getParent();
        if (realActivity == null) {
            realActivity = (Activity) context;
        }

        ContextCompat.startForegroundService(realActivity, new Intent(realActivity, MusicService.class));

        final ServiceBinder binder = new ServiceBinder(callback);

        if (realActivity.bindService(new Intent().setClass(realActivity, MusicService.class), binder, Context.BIND_AUTO_CREATE)) {
            mConnectionMap.put(realActivity, binder);
            return new ServiceToken(realActivity);
        }
        return null;
    }

    public static void unbindFromService(@Nullable final ServiceToken token) {
        if (token == null) {
            return;
        }
        final ContextWrapper mContextWrapper = token.mWrappedContext;
        final ServiceBinder mBinder = mConnectionMap.remove(mContextWrapper);
        if (mBinder == null) {
            return;
        }
        mContextWrapper.unbindService(mBinder);
        if (mConnectionMap.isEmpty() && musicService != null) {
            if (!musicService.isPlaying()) {
                musicService.quit();
            }
            musicService = null;
        }
    }

    public static final class ServiceBinder implements ServiceConnection {
        private final ServiceConnection mCallback;

        public ServiceBinder(final ServiceConnection callback) {
            mCallback = callback;
        }

        @Override
        public void onServiceConnected(final ComponentName className, final IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            if (mCallback != null) {
                mCallback.onServiceConnected(className, service);
            }
        }

        @Override
        public void onServiceDisconnected(final ComponentName className) {
            if (mCallback != null) {
                mCallback.onServiceDisconnected(className);
            }
            musicService = null;
        }
    }

    public static final class ServiceToken {
        public final ContextWrapper mWrappedContext;

        public ServiceToken(final ContextWrapper context) {
            mWrappedContext = context;
        }
    }

    public static void playSongAt(final int position, boolean skippedLast) {
        if (musicService != null) {
            musicService.playSongAt(position, skippedLast);
        }
    }

    public static void setPosition(final int position) {
        if (musicService != null) {
            musicService.setPosition(position);
        }
    }

    public static void pauseSong() {
        if (musicService != null) {
            musicService.pause();
        }
    }

    public static void playNextSong(boolean skippedLast) {
        if (musicService != null) {
            musicService.playNextSong(skippedLast);
        }
    }

    public static void playPreviousSong(boolean skippedLast) {
        if (musicService != null) {
            musicService.playPreviousSong(skippedLast);
        }
    }

    public static void back(boolean skippedLast) {
        if (musicService != null) {
            musicService.back(skippedLast);
        }
    }

    public static boolean isPlaying() {
        return musicService != null && musicService.isPlaying();
    }

    public static boolean isPlaying(@NonNull Song song) {
        return musicService != null && musicService.isPlaying(song);
    }

    public static void resumePlaying() {
        if (musicService != null) {
            musicService.play();
        }
    }

    public static void openQueue(final ArrayList<Song> queue, final int startPosition, final boolean startPlaying) {
        if (!tryToHandleOpenPlayingQueue(queue, startPosition, startPlaying) && musicService != null) {
            if (!PreferenceUtil.getInstance().rememberShuffle()) {
                setShuffleMode(MusicService.SHUFFLE_MODE_NONE);
            }
            musicService.openQueue(queue, startPosition, startPlaying);
        }
    }

    public static void openAndShuffleQueue(final ArrayList<Song> queue, boolean startPlaying) {
        if (!tryToHandleOpenPlayingQueue(queue, 0, startPlaying) && musicService != null) {
            musicService.openQueue(queue, MusicService.RANDOM_START_POSITION_ON_SHUFFLE, startPlaying, MusicService.SHUFFLE_MODE_SHUFFLE);
        }
    }

    private static void removeDuplicateBeforeQueuing (final Song song) {
        removeDuplicateBeforeQueuing(new ArrayList<>(Collections.singletonList(song)));
    }

    private static void removeDuplicateBeforeQueuing (final ArrayList<Song> songsToAdd) {
        if (musicService == null) {return;}
        
        final ArrayList<Song> currentQueue = musicService.getPlayingQueue();
        if (currentQueue.isEmpty()) {
            return;
        }

        final List<Song> remainingSongsInQueue = currentQueue.subList(
                musicService.getPosition() + 1,
                currentQueue.size()
        );

        final ArrayList<Song> songsToRemove = new ArrayList<>();
        for (final Song song : remainingSongsInQueue) {
            for (final Song songToAdd : songsToAdd) {
                if (song.isQuickEqual(songToAdd)) {
                    songsToRemove.add(song);
                    break;
                }
            }
        }

        musicService.removeSongs(songsToRemove);
    }

    public static void enqueueSongsWithConfirmation(final @NonNull Context context, final ArrayList<Song> queue, int positionInQueue) {
        if (musicService == null) {return;}

        if (tryToHandleOpenPlayingQueue(queue, positionInQueue, true)) {
            return;
        }

        final ArrayList<Song> currentQueue = musicService.getPlayingQueue();
        if (currentQueue.isEmpty()) {
            openQueue(queue, positionInQueue, true);
            return;
        }

        final int adjustedPosition = Math.max(positionInQueue, 0);
        final ArrayList<Song> songsToAdd = new ArrayList<>(queue.subList(adjustedPosition, queue.size()));

        final Runnable showToastEnqueued = () -> {
            int count = songsToAdd.size();
            final String toast = (count == 1)
                    ? musicService.getResources().getString(R.string.added_title_to_playing_queue)
                    : musicService.getResources().getString(R.string.added_x_titles_to_playing_queue, count);

            Toast.makeText(musicService, toast, Toast.LENGTH_SHORT).show();
        };

        final List<BottomSheetDialogWithButtons.ButtonInfo> possibleActions = Arrays.asList(
                new BottomSheetDialogWithButtons.ButtonInfo(
                        context.getString(R.string.action_play),
                        () -> {
                            openQueue(queue, positionInQueue, true);
                        },
                        ContextCompat.getDrawable(context, R.drawable.ic_play_arrow_white_24dp)),
                new BottomSheetDialogWithButtons.ButtonInfo(
                        context.getString(R.string.action_play_next),
                        () -> {
                            playNext(songsToAdd);
                        },
                        ContextCompat.getDrawable(context, R.drawable.ic_redo_white_24dp)),
                new BottomSheetDialogWithButtons.ButtonInfo(
                        context.getString(R.string.action_add_to_playing_queue),
                        () -> {
                            enqueue(songsToAdd);
                        },
                        ContextCompat.getDrawable(context, R.drawable.ic_library_add_white_24dp))
        );

        final int songCount = songsToAdd.size();
        final String message = (songCount == 1)
                ? context.getResources().getString(R.string.about_to_add_title_to_playing_queue)
                : context.getResources().getString(R.string.about_to_add_x_titles_to_playing_queue, songCount);

        BottomSheetDialogWithButtons songActionDialog = BottomSheetDialogWithButtons.newInstance();
        songActionDialog.setTitle(message)
                .setButtonList(possibleActions)
                .show( ((AppCompatActivity) context).getSupportFragmentManager(), "songActionDialog");

    }

    private static boolean tryToHandleOpenPlayingQueue(final ArrayList<Song> queue, final int startPosition, final boolean startPlaying) {
        if (getPlayingQueue() == queue) {
            if (startPlaying) {playSongAt(startPosition, isPlaying());}
            else {setPosition(startPosition);}
            return true;
        }
        return false;
    }

    @NonNull
    public static Song getCurrentSong() {
        if (musicService != null) {
            return musicService.getCurrentSong();
        }
        return Song.EMPTY_SONG;
    }

    public static IndexedSong getCurrentIndexedSong() {
        if (musicService != null) {
            return musicService.getCurrentIndexedSong();
        }
        return IndexedSong.EMPTY_INDEXED_SONG;
    }

    public static int getPosition() {
        if (musicService != null) {
            return musicService.getPosition();
        }
        return -1;
    }

    public static ArrayList<Song> getPlayingQueue() {
        if (musicService != null) {
            return musicService.getPlayingQueue();
        }
        return new ArrayList<>();
    }

    public static int getSongProgressMillis() {
        if (musicService != null) {
            return musicService.getSongProgressMillis();
        }
        return -1;
    }

    public static int getSongDurationMillis() {
        if (musicService != null) {
            return musicService.getSongDurationMillis();
        }
        return -1;
    }

    public static void seekTo(int millis) {
        if (musicService != null) {
            musicService.seek(millis);
        }
    }

    public static int getRepeatMode() {
        if (musicService != null) {
            return musicService.getRepeatMode();
        }
        return MusicService.REPEAT_MODE_NONE;
    }

    public static int getShuffleMode() {
        if (musicService != null) {
            return musicService.getShuffleMode();
        }
        return MusicService.SHUFFLE_MODE_NONE;
    }

    public static void cycleRepeatMode() {
        if (musicService != null) {
            musicService.cycleRepeatMode();
        }
    }

    public static void toggleShuffleMode() {
        if (musicService != null) {
            musicService.toggleShuffle();
        }
    }

    public static void setShuffleMode(final int shuffleMode) {
        if (musicService != null) {
            musicService.setShuffleMode(shuffleMode);
        }
    }

    public static void playNext(Song song) {
        if (musicService != null) {
            if (getPlayingQueue().size() > 0) {
                removeDuplicateBeforeQueuing(song);
                musicService.addSongAfter(getPosition(), song);
            } else {
                ArrayList<Song> queue = new ArrayList<>();
                queue.add(song);
                openQueue(queue, 0, false);
            }
            Toast.makeText(musicService, musicService.getResources().getString(R.string.added_title_to_playing_queue), Toast.LENGTH_SHORT).show();
        }
    }

    public static void playNext(@NonNull ArrayList<Song> songs) {
        if (musicService != null) {
            if (getPlayingQueue().size() > 0) {
                removeDuplicateBeforeQueuing(songs);
                musicService.addSongsAfter(getPosition(), songs);
            } else {
                openQueue(songs, 0, false);
            }
            final String toast = (songs.size() == 1)
                    ? musicService.getResources().getString(R.string.added_title_to_playing_queue)
                    : musicService.getResources().getString(R.string.added_x_titles_to_playing_queue, songs.size());
            Toast.makeText(musicService, toast, Toast.LENGTH_SHORT).show();
        }
    }

    public static void enqueue(Song song) {
        if (musicService != null) {
            if (getPlayingQueue().size() > 0) {
                removeDuplicateBeforeQueuing(song);
                musicService.addSong(song);
            } else {
                ArrayList<Song> queue = new ArrayList<>();
                queue.add(song);
                openQueue(queue, 0, false);
            }
            Toast.makeText(musicService, musicService.getResources().getString(R.string.added_title_to_playing_queue), Toast.LENGTH_SHORT).show();
        }
    }

    public static void enqueue(@NonNull ArrayList<Song> songs) {
        if (musicService != null) {
            if (getPlayingQueue().size() > 0) {
                removeDuplicateBeforeQueuing(songs);
                musicService.addSongs(songs);
            } else {
                openQueue(songs, 0, false);
            }
            final String toast = songs.size() == 1 ? musicService.getResources().getString(R.string.added_title_to_playing_queue) : musicService.getResources().getString(R.string.added_x_titles_to_playing_queue, songs.size());
            Toast.makeText(musicService, toast, Toast.LENGTH_SHORT).show();
        }
    }

    public static void removeFromQueue(@NonNull List<Song> songs) {
        if (musicService != null) {
            musicService.removeSongs(songs);
        }
    }

    public static void removeFromQueue(int position) {
        if (musicService != null && position >= 0 && position < getPlayingQueue().size()) {
            musicService.removeSong(position);
        }
    }

    public static IndexedSong getIndexedSongAt(int position) {
        if (musicService != null && position >= 0 && position < getPlayingQueue().size()) {
            return musicService.getIndexedSongAt(position);
        }
        return IndexedSong.EMPTY_INDEXED_SONG;
    }

    public static void moveSong(int from, int to) {
        if (musicService != null && from >= 0 && to >= 0 && from < getPlayingQueue().size() && to < getPlayingQueue().size()) {
            musicService.moveSong(from, to);
        }
    }

    public static void addSongBackTo(int to, @NonNull IndexedSong song) {
        if (musicService != null) {
            musicService.addSongBackTo(to, song);
        }
    }

    public static void clearQueue() {
        if (musicService != null) {
            musicService.clearQueue();
        }
    }

    public static int getAudioSessionId() {
        if (musicService != null) {
            return musicService.getAudioSessionId();
        }
        return -1;
    }

    @NonNull
    public static String getQueueInfoString() {
        if (musicService != null) {
            return musicService.getQueueInfoString();
        }
        return "";
    }

    public static void playFromUri(Uri uri) {
        if (musicService != null) {
            Song song = Song.EMPTY_SONG;

            // Get by id
            if (uri.getScheme() != null && uri.getAuthority() != null) {
                String songId = null;
                if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                    if (uri.getAuthority().equals("com.android.providers.media.documents")) {
                        songId = getSongIdFromMediaProvider(uri);
                    } else if (uri.getAuthority().equals("media")) {
                        songId = uri.getLastPathSegment();
                    }
                }
                if (!TextUtils.isEmpty(songId)) {
                    try {
                        song = Discography.getInstance().getSong(Integer.parseInt(songId));
                    } catch (NumberFormatException ignored) {}
                }
            }

            if (song.equals(Song.EMPTY_SONG)) {
                File songFile = null;
                if (uri.getAuthority() != null && uri.getAuthority().equals("com.android.externalstorage.documents")) {
                    songFile = new File(Environment.getExternalStorageDirectory(), uri.getPath().split(":", 2)[1]);
                }
                if (songFile == null) {
                    String path = getFilePathFromUri(musicService, uri);
                    if (path != null)
                        songFile = new File(path);
                }
                if (songFile == null && uri.getPath() != null) {
                    songFile = new File(uri.getPath());
                }
                if (songFile != null) {
                    song = Discography.getInstance().getSongByPath(songFile.getAbsolutePath());
                }
            }

            if (!song.equals(Song.EMPTY_SONG)) {
                openQueue(new ArrayList<>(List.of(song)), 0, true);
            } else {
                Log.e(TAG, "No song found for URI: " + uri);
            }
        }
    }

    @Nullable
    private static String getFilePathFromUri(Context context, Uri uri)
    {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static String getSongIdFromMediaProvider(Uri uri) {
        return DocumentsContract.getDocumentId(uri).split(":")[1];
    }
}
