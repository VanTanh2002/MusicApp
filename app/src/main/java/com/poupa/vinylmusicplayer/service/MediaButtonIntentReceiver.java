package com.poupa.vinylmusicplayer.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;

import androidx.core.content.ContextCompat;

import com.poupa.vinylmusicplayer.BuildConfig;

import static com.poupa.vinylmusicplayer.service.MusicService.VINYL_MUSIC_PLAYER_PACKAGE_NAME;

public class MediaButtonIntentReceiver extends BroadcastReceiver {
    private static final boolean DEBUG = BuildConfig.DEBUG;
    public static final String TAG = MediaButtonIntentReceiver.class.getSimpleName();

    private static final int MSG_HEADSET_DOUBLE_CLICK_TIMEOUT = 2;

    private static final int DOUBLE_CLICK = 400;

    private static WakeLock mWakeLock = null;
    private static int mClickCounter = 0;
    private static long mLastClickTime = 0;

    @SuppressLint("HandlerLeak")
    private static final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {
            if (msg.what == MSG_HEADSET_DOUBLE_CLICK_TIMEOUT) {
                final int clickCount = msg.arg1;
                final String command;

                if (DEBUG) Log.v(TAG, "Handling headset click, count = " + clickCount);
                switch (clickCount) {
                    case 1:
                        command = MusicService.ACTION_TOGGLE_PAUSE;
                        break;
                    case 2:
                        command = MusicService.ACTION_SKIP;
                        break;
                    case 3:
                        command = MusicService.ACTION_REWIND;
                        break;
                    default:
                        command = null;
                        break;
                }

                if (command != null) {
                    final Context context = (Context) msg.obj;
                    startService(context, command);
                }
            }
            releaseWakeLockIfHandlerIdle();
        }
    };

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (DEBUG) Log.v(TAG, "Received intent: " + intent);
        if (handleIntent(context, intent) && isOrderedBroadcast()) {
            abortBroadcast();
        }
    }

    public static boolean handleIntent(final Context context, final Intent intent) {
        final String intentAction = intent.getAction();
        if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            final KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event == null) {
                return false;
            }

            final int keycode = event.getKeyCode();
            final int action = event.getAction();
            final long eventTime = event.getEventTime() != 0 ?
                    event.getEventTime() : System.currentTimeMillis();

            String command = null;
            switch (keycode) {
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    command = MusicService.ACTION_STOP;
                    break;
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    command = MusicService.ACTION_TOGGLE_PAUSE;
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    command = MusicService.ACTION_SKIP;
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    command = MusicService.ACTION_REWIND;
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    command = MusicService.ACTION_PAUSE;
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    command = MusicService.ACTION_PLAY;
                    break;
            }
            if (command != null) {
                if (action == KeyEvent.ACTION_DOWN) {
                    if (event.getRepeatCount() == 0) {

                        if (keycode == KeyEvent.KEYCODE_HEADSETHOOK || keycode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                            if (eventTime - mLastClickTime >= DOUBLE_CLICK) {
                                mClickCounter = 0;
                            }

                            mClickCounter++;
                            if (DEBUG) Log.v(TAG, "Got headset click, count = " + mClickCounter);
                            mHandler.removeMessages(MSG_HEADSET_DOUBLE_CLICK_TIMEOUT);

                            Message msg = mHandler.obtainMessage(
                                    MSG_HEADSET_DOUBLE_CLICK_TIMEOUT, mClickCounter, 0, context);

                            long delay = mClickCounter < 3 ? DOUBLE_CLICK : 0;
                            if (mClickCounter >= 3) {
                                mClickCounter = 0;
                            }
                            mLastClickTime = eventTime;
                            acquireWakeLockAndSendMessage(context, msg, delay);
                        } else {
                            startService(context, command);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void startService(Context context, String command) {
        final Intent intent = new Intent(context, MusicService.class);
        intent.setAction(command);
        ContextCompat.startForegroundService(context, intent);
    }

    private static void acquireWakeLockAndSendMessage(Context context, Message msg, long delay) {
        if (mWakeLock == null) {
            Context appContext = context.getApplicationContext();
            PowerManager pm = (PowerManager) appContext.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, VINYL_MUSIC_PLAYER_PACKAGE_NAME + ":HeadsetButton");
            mWakeLock.setReferenceCounted(false);
        }
        if (DEBUG) Log.v(TAG, "Acquiring wake lock and sending " + msg.what);
        mWakeLock.acquire(10000);

        mHandler.sendMessageDelayed(msg, delay);
    }

    private static void releaseWakeLockIfHandlerIdle() {
        if (mHandler.hasMessages(MSG_HEADSET_DOUBLE_CLICK_TIMEOUT)) {
            if (DEBUG) Log.v(TAG, "Handler still has messages pending, not releasing wake lock");
            return;
        }

        if (mWakeLock != null) {
            if (DEBUG) Log.v(TAG, "Releasing wake lock");
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}
