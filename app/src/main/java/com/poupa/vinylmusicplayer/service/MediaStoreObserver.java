package com.poupa.vinylmusicplayer.service;

import android.database.ContentObserver;
import android.os.Handler;

public class MediaStoreObserver extends ContentObserver implements Runnable {
    private static final long REFRESH_DELAY = 500;
    private final Handler mHandler;
    private final MusicService mMusicService;

    public MediaStoreObserver(MusicService musicService, Handler handler) {
        super(handler);
        mHandler = handler;
        mMusicService = musicService;
    }

    @Override
    public void onChange(boolean selfChange) {
        mHandler.removeCallbacks(this);
        mHandler.postDelayed(this, REFRESH_DELAY);
    }

    @Override
    public void run() {
        mMusicService.handleAndSendChangeInternal(MusicService.MEDIA_STORE_CHANGED);
    }
}