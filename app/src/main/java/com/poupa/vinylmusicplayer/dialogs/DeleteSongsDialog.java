package com.poupa.vinylmusicplayer.dialogs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.poupa.vinylmusicplayer.R;
import com.poupa.vinylmusicplayer.helper.MusicPlayerRemote;
import com.poupa.vinylmusicplayer.misc.DialogAsyncTask;
import com.poupa.vinylmusicplayer.model.Song;
import com.poupa.vinylmusicplayer.ui.activities.saf.SAFGuideActivity;
import com.poupa.vinylmusicplayer.util.MusicUtil;
import com.poupa.vinylmusicplayer.util.SAFUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeleteSongsDialog extends DialogFragment {

    private DeleteSongsAsyncTask deleteSongsTask;
    private ArrayList<Song> songsToRemove;
    private Song currentSong;

    @NonNull
    public static DeleteSongsDialog create(Song song) {
        ArrayList<Song> list = new ArrayList<>();
        list.add(song);
        return create(list);
    }

    @NonNull
    public static DeleteSongsDialog create(ArrayList<Song> songs) {
        DeleteSongsDialog dialog = new DeleteSongsDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList("songs", songs);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ArrayList<Song> songs = getArguments().getParcelableArrayList("songs");
        int title;
        CharSequence content;
        if (songs.size() > 1) {
            title = R.string.delete_songs_title;
            content = Html.fromHtml(getString(R.string.delete_x_songs, songs.size()));
        } else {
            title = R.string.delete_song_title;
            content = Html.fromHtml(getString(R.string.delete_song_x, songs.get(0).title));
        }
        return new MaterialDialog.Builder(getActivity())
                .title(title)
                .content(content)
                .positiveText(R.string.delete_action)
                .negativeText(android.R.string.cancel)
                .autoDismiss(false)
                .onPositive((dialog, which) -> {
                    if ((songs.size() == 1) && MusicPlayerRemote.isPlaying(songs.get(0))) {
                        MusicPlayerRemote.playNextSong(false);
                    }

                    songsToRemove = songs;
                    deleteSongsTask = new DeleteSongsAsyncTask(DeleteSongsDialog.this);
                    deleteSongsTask.execute(new DeleteSongsAsyncTask.LoadingInfo(songs, null));
                })
                .onNegative((materialDialog, dialogAction) -> dismiss())
                .build();
    }

    private void deleteSongs(List<Song> songs, List<Uri> safUris) {
        MusicUtil.deleteTracks(getActivity(), songs, safUris, this::dismiss);
    }

    private void deleteSongsKitkat() {
        if (songsToRemove.size() < 1) {
            dismiss();
            return;
        }

        currentSong = songsToRemove.remove(0);

        if (!SAFUtil.isSAFRequired(currentSong)) {
            deleteSongs(Collections.singletonList(currentSong), null);
            deleteSongsKitkat();
        } else {
            Toast.makeText(getActivity(), String.format(getString(R.string.saf_pick_file), currentSong.data), Toast.LENGTH_LONG).show();
            SAFUtil.openFilePicker(this);
        }
    }



    private static class DeleteSongsAsyncTask extends DialogAsyncTask<DeleteSongsAsyncTask.LoadingInfo, Integer, Void> {
        private final WeakReference<DeleteSongsDialog> dialog;
        private final WeakReference<FragmentActivity> activity;

        public DeleteSongsAsyncTask(DeleteSongsDialog dialog) {
            super(dialog.getActivity());
            this.dialog = new WeakReference<>(dialog);
            this.activity = new WeakReference<>(dialog.getActivity());
        }

        @Override
        protected Void doInBackground(LoadingInfo... params) {
            try {
                LoadingInfo info = params[0];

                DeleteSongsDialog dialog = this.dialog.get();
                FragmentActivity activity = this.activity.get();

                if (dialog == null || activity == null)
                    return null;

                if (!info.isIntent) {
                    if (!SAFUtil.isSAFRequiredForSongs(info.songs)) {
                        dialog.deleteSongs(info.songs, null);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (SAFUtil.isSDCardAccessGranted(activity)) {
                                dialog.deleteSongs(info.songs, null);
                            }
                        } else {
                            dialog.deleteSongsKitkat();
                        }
                    }
                } else {
                    switch (info.requestCode) {
                        case SAFUtil.REQUEST_SAF_PICK_TREE:
                            if (info.resultCode == Activity.RESULT_OK) {
                                SAFUtil.saveTreeUri(activity, info.intent);
                                dialog.deleteSongs(dialog.songsToRemove, null);
                            }
                            break;

                        case SAFUtil.REQUEST_SAF_PICK_FILE:
                            if (info.resultCode == Activity.RESULT_OK) {
                                dialog.deleteSongs(Collections.singletonList(dialog.currentSong), Collections.singletonList(info.intent.getData()));
                            }
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected Dialog createDialog(@NonNull Context context) {
            return new MaterialDialog.Builder(context)
                    .title(R.string.deleting_songs)
                    .cancelable(false)
                    .progress(true, 0)
                    .build();
        }

        public static class LoadingInfo {
            public final boolean isIntent;

            public List<Song> songs;
            public List<Uri> safUris;

            public int requestCode;
            public int resultCode;
            public Intent intent;

            public LoadingInfo(List<Song> songs, List<Uri> safUris) {
                this.isIntent = false;
                this.songs = songs;
                this.safUris = safUris;
            }

            public LoadingInfo(int requestCode, int resultCode, Intent intent) {
                this.isIntent = true;
                this.requestCode = requestCode;
                this.resultCode = resultCode;
                this.intent = intent;
            }
        }
    }
}
