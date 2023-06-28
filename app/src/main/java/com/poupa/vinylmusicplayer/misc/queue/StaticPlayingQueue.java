package com.poupa.vinylmusicplayer.misc.queue;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.poupa.vinylmusicplayer.helper.ShuffleHelper;
import com.poupa.vinylmusicplayer.model.Song;

public class StaticPlayingQueue {

    public static final int REPEAT_MODE_NONE = 0;
    public static final int REPEAT_MODE_ALL = 1;
    public static final int REPEAT_MODE_THIS = 2;

    private int repeatMode;

    public static final int INVALID_POSITION = IndexedSong.INVALID_INDEX;
    public static final int SHUFFLE_MODE_NONE = 0;
    public static final int SHUFFLE_MODE_SHUFFLE = 1;
    private int shuffleMode;

    private int currentPosition;

    private int nextPosition;

    private boolean songsIsStale;
    private final ArrayList<Song> songs;
    private ArrayList<IndexedSong> queue;
    private final ArrayList<IndexedSong> originalQueue;

    private long nextUniqueId;

    public StaticPlayingQueue() {
        songs = new ArrayList<>();
        queue = new ArrayList<>();
        originalQueue = new ArrayList<>();
        shuffleMode = SHUFFLE_MODE_NONE;
        currentPosition = INVALID_POSITION;
        songsIsStale = false;

        restoreUniqueId();
    }

    public StaticPlayingQueue(ArrayList<IndexedSong> restoreQueue, ArrayList<IndexedSong> restoreOriginalQueue, int restoredPosition, int shuffleMode) {
        this.queue = new ArrayList<>(restoreQueue);
        this.originalQueue = new ArrayList<>(restoreOriginalQueue);
        this.shuffleMode = shuffleMode;

        currentPosition = restoredPosition;

        for (int i = restoreQueue.size() - 1; i >= 0; --i) {
            if (restoreQueue.get(i).id == Song.EMPTY_SONG.id) {
                remove(i);
            }
        }

        songs = new ArrayList<>();
        songsIsStale = true;
        resetSongs();

        restoreUniqueId();
    }

    public void restoreMode(int shuffleMode, int repeatMode) {
        this.shuffleMode = shuffleMode;
        this.repeatMode = repeatMode;
    }

    private void restoreUniqueId() {
        nextUniqueId = 0;
        for (int i = 0; i < queue.size(); i++) {
            long uniqueId = getNextUniqueId();
            queue.get(i).setUniqueId(uniqueId);

            int index = queue.get(i).index;
            if(index < originalQueue.size()) {
                originalQueue.get(index).setUniqueId(uniqueId);
            }
        }
    }

    /* -------------------- queue modification (add, remove, move, ...) -------------------- */

    private long getNextUniqueId() {
        return nextUniqueId++;
    }

    public void add(Song song) {
        long uniqueId = getNextUniqueId();
        queue.add(new IndexedSong((Song)song, queue.size(), uniqueId));
        originalQueue.add(new IndexedSong((Song)song, originalQueue.size(), uniqueId));

        songsIsStale = true;
    }

    public void addAll(@NonNull List<Song> songs) {
        int position = size();
        for (Song song : songs) {
            add(song);
        }

        if (getShuffleMode() == SHUFFLE_MODE_SHUFFLE) {
            ShuffleHelper.makeShuffleList(queue.subList(position, queue.size()), 0);
        }
    }

    private void updateQueueIndexesAfterSongsModification(int position, int occurrence, int previousPosition, int direction) {
        for (int i = 0; i < queue.size(); i++) {
            originalQueue.get(i).index = i;

            if (!(i >= position && i <= position+occurrence) && queue.get(i).index >= previousPosition  ) {
                queue.get(i).index = queue.get(i).index + direction*(occurrence + 1);

                int index = queue.get(i).index;
                if (index < 0) {throw new IllegalArgumentException("Bad index=" + index);}
            }
        }
    }

    private void addOneSong(int position, int previousPosition, Song song) {
        long uniqueId = getNextUniqueId();
        originalQueue.add(previousPosition, new IndexedSong(song, previousPosition, uniqueId));
        queue.add(position, new IndexedSong(song, previousPosition, uniqueId));

        updateQueueIndexesAfterSongsModification(position, 0, previousPosition, +1);

        songsIsStale = true;
    }

    public void addAfter(int position, Song song) {
        int queueSize = queue.size();
        if (position >= queueSize) {
            position = queueSize - 1;
        }
        int previousPosition = queue.get(position).index + 1;
        position = position + 1;

        addOneSong(position, previousPosition, song);

        if (position < this.currentPosition) {
            this.currentPosition++;
        }
    }

    public void addSongBackTo(int position, IndexedSong song) {
        int previousPosition = song.index;

        addOneSong(position, previousPosition, (Song)song);

        if (position <= this.currentPosition) {
            this.currentPosition++;
        }
    }

    public void addAllAfter(int position, @NonNull List<Song> songs) {
        final int queueSize = queue.size();
        if (queueSize == 0) {
            addAll(songs);
            return;
        }

        if (position >= queueSize) {
            position = queueSize - 1;
        }
        int previousPosition = queue.get(position).index + 1;
        position = position + 1;

        int n = songs.size() - 1;
        for (int i = n; i >= 0; i--) {
            int newPosition = previousPosition + i;
            long uniqueId = getNextUniqueId();
            originalQueue.add(previousPosition, new IndexedSong(songs.get(i), newPosition, uniqueId));
            queue.add(position, new IndexedSong(songs.get(i), newPosition, uniqueId));

            if (position <= this.currentPosition) {
                this.currentPosition++;
            }
        }

        updateQueueIndexesAfterSongsModification(position, n, previousPosition, +1);
        if (getShuffleMode() == SHUFFLE_MODE_SHUFFLE) {
            ShuffleHelper.makeShuffleList(queue.subList(position, position + songs.size()), 0);
        }

        songsIsStale = true;
    }

    public void move(int from, int to) {
        if (from == to) return;
        final int currentPosition = this.currentPosition;

        IndexedSong songToMove = queue.remove(from);
        queue.add(to, songToMove);

        if (shuffleMode == SHUFFLE_MODE_NONE) {
            IndexedSong previousSongToMove = originalQueue.remove(from);
            originalQueue.add(to, previousSongToMove);

            for (int i = 0; i < queue.size(); i++) {
                queue.get(i).index = i;
                originalQueue.get(i).index = i;
            }
        }

        if (from > currentPosition && to <= currentPosition) {
            this.currentPosition = currentPosition + 1;
        } else if (from < currentPosition && to >= currentPosition) {
            this.currentPosition = currentPosition - 1;
        } else if (from == currentPosition) {
            this.currentPosition = to;
        }

        songsIsStale = true;
    }

    private int rePosition(int deletedPosition) {
        int position = this.currentPosition;

        if (deletedPosition < position) {
            this.currentPosition = position - 1;
        } else if (deletedPosition == position) { //the current position was deleted
            if (queue.size() > deletedPosition) {
                return position;
            } else {
                return position - 1;
            }
        }

        return INVALID_POSITION;
    }

    public int remove(int position) {
        IndexedSong o = queue.remove(position);
        originalQueue.remove(o.index);

        updateQueueIndexesAfterSongsModification(-1, 0, o.index, -1);

        songsIsStale = true;

        return rePosition(position);
    }

    private int removeAllOccurrences(Song song) {
        int hasPositionChanged = INVALID_POSITION;

        for (int i = queue.size() - 1; i >= 0; i--) {
            if (queue.get(i).id == song.id) {
                int temp = remove(i);
                if (temp != INVALID_POSITION) {
                    hasPositionChanged = temp;
                }
            }
        }

        songsIsStale = true;

        return hasPositionChanged;
    }

    public int removeSongs(@NonNull List<Song> songs) {
        int hasPositionChanged = INVALID_POSITION;

        for (Song song : songs) {
            int temp = removeAllOccurrences(song);
            if (temp != INVALID_POSITION) {
                hasPositionChanged = temp;
            }
        }

        return hasPositionChanged;
    }

    public void clear() {
        songs.clear();
        queue.clear();
        originalQueue.clear();

        restoreUniqueId();
    }

    private void revert() {
        queue = new ArrayList<>(originalQueue);
        songsIsStale = true;
    }

    /* -------------------- queue getter info -------------------- */

    public boolean openQueue(@Nullable final ArrayList<Song> playingQueue, final int startPosition, final boolean startPlaying, int shuffleMode) {
        if ( !(playingQueue != null && !playingQueue.isEmpty() && startPosition >= 0 && startPosition < playingQueue.size()) ) {
            return false;
        }

        clear();
        this.shuffleMode = SHUFFLE_MODE_NONE;
        addAll(playingQueue);

        this.currentPosition = startPosition;
        setShuffle(shuffleMode);

        return true;
    }

    public ArrayList<IndexedSong> getPlayingQueue() {
        return queue;
    }

    public ArrayList<IndexedSong> getOriginalPlayingQueue() {
        return originalQueue;
    }

    private void resetSongs() {
        this.songs.clear();

        for (IndexedSong song : queue) {
            songs.add((Song)song);
        }
        songsIsStale = false;
    }

    public ArrayList<Song> getPlayingQueueSongOnly() {
        if (songsIsStale)
            resetSongs();
        return songs;
    }

    public int size() {
        return queue.size();
    }

    /* -------------------- position method -------------------- */

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int position) {
        if (position >= queue.size())
            return;

        currentPosition = position;
    }

    public void setPositionToNextPosition() {
        currentPosition = nextPosition;
    }

    public void setNextPosition(int position) {
        if (position >= queue.size())
            return;

        nextPosition = position;
    }

    public boolean isLastTrack() {
        return getCurrentPosition() == queue.size() - 1;
    }

    public int getNextPosition(boolean skippedLast) {
        int position = getCurrentPosition() + 1;
        switch (getRepeatMode()) {
            case REPEAT_MODE_ALL:
                if (isLastTrack()) {
                    position = 0;
                }
                break;
            case REPEAT_MODE_THIS:
                if (skippedLast) {
                    if (isLastTrack()) {
                        position = 0;
                    }
                } else {
                    position -= 1;
                }
                break;
            default:
            case REPEAT_MODE_NONE:
                if (isLastTrack()) {
                    position -= 1;
                }
                break;
        }
        return position;
    }

    public int getPreviousPosition(boolean skippedLast) {
        int newPosition = getCurrentPosition() - 1;
        switch (repeatMode) {
            case REPEAT_MODE_ALL:
                if (newPosition < 0) {
                    newPosition = queue.size() - 1;
                }
                break;
            case REPEAT_MODE_THIS:
                if (skippedLast) {
                    if (newPosition < 0) {
                        newPosition = queue.size() - 1;
                    }
                } else {
                    newPosition = getCurrentPosition();
                }
                break;
            default:
            case REPEAT_MODE_NONE:
                if (newPosition < 0) {
                    newPosition = 0;
                }
                break;
        }
        return newPosition;
    }

    /* -------------------- song getter info -------------------- */

    public long getQueueDurationMillis(int position){
        long duration = 0;
        for (int i = position + 1; i < queue.size(); i++)
            duration += queue.get(i).duration;
        return duration;
    }

    /* -------------------- shuffle method -------------------- */

    public void setShuffle(int shuffleMode) {
        if (this.shuffleMode == shuffleMode)
            return;

        switch (shuffleMode) {
            case SHUFFLE_MODE_NONE:
                currentPosition = queue.get(currentPosition).index;
                revert();
                break;
            case SHUFFLE_MODE_SHUFFLE:
                ShuffleHelper.makeShuffleList(queue, currentPosition);
                songsIsStale = true;
                currentPosition = 0;
                break;
        }

        this.shuffleMode = shuffleMode;
    }

    public void toggleShuffle() {
        switch (shuffleMode) {
            case SHUFFLE_MODE_NONE:
                setShuffle(SHUFFLE_MODE_SHUFFLE);
                break;
            case SHUFFLE_MODE_SHUFFLE:
                setShuffle(SHUFFLE_MODE_NONE);
                break;
        }
    }

    public int getShuffleMode() {
        return shuffleMode;
    }

    /* -------------------- repeat method -------------------- */
    public void setRepeatMode(final int repeatMode) {
        if (this.repeatMode == repeatMode)
            return;

        switch (repeatMode) {
            case REPEAT_MODE_NONE:
            case REPEAT_MODE_ALL:
            case REPEAT_MODE_THIS:
                this.repeatMode = repeatMode;
                break;
        }
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void cycleRepeatMode() {
        switch (getRepeatMode()) {
            case REPEAT_MODE_NONE:
                setRepeatMode(REPEAT_MODE_ALL);
                break;
            case REPEAT_MODE_ALL:
                setRepeatMode(REPEAT_MODE_THIS);
                break;
            default:
                setRepeatMode(REPEAT_MODE_NONE);
                break;
        }
    }
}
