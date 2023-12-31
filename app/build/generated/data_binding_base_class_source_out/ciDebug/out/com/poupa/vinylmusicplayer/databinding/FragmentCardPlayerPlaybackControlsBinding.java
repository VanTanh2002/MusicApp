// Generated by view binder compiler. Do not edit!
package com.poupa.vinylmusicplayer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.poupa.vinylmusicplayer.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentCardPlayerPlaybackControlsBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Space dummyFab;

  @NonNull
  public final LinearLayout playerFooterFrame;

  @NonNull
  public final RelativeLayout playerMediaControllerContainer;

  @NonNull
  public final ImageButton playerNextButton;

  @NonNull
  public final FloatingActionButton playerPlayPauseFab;

  @NonNull
  public final ImageButton playerPrevButton;

  @NonNull
  public final SeekBar playerProgressSlider;

  @NonNull
  public final ImageButton playerRepeatButton;

  @NonNull
  public final ImageButton playerShuffleButton;

  @NonNull
  public final TextView playerSongCurrentProgress;

  @NonNull
  public final TextView playerSongTotalTime;

  private FragmentCardPlayerPlaybackControlsBinding(@NonNull LinearLayout rootView,
      @NonNull Space dummyFab, @NonNull LinearLayout playerFooterFrame,
      @NonNull RelativeLayout playerMediaControllerContainer, @NonNull ImageButton playerNextButton,
      @NonNull FloatingActionButton playerPlayPauseFab, @NonNull ImageButton playerPrevButton,
      @NonNull SeekBar playerProgressSlider, @NonNull ImageButton playerRepeatButton,
      @NonNull ImageButton playerShuffleButton, @NonNull TextView playerSongCurrentProgress,
      @NonNull TextView playerSongTotalTime) {
    this.rootView = rootView;
    this.dummyFab = dummyFab;
    this.playerFooterFrame = playerFooterFrame;
    this.playerMediaControllerContainer = playerMediaControllerContainer;
    this.playerNextButton = playerNextButton;
    this.playerPlayPauseFab = playerPlayPauseFab;
    this.playerPrevButton = playerPrevButton;
    this.playerProgressSlider = playerProgressSlider;
    this.playerRepeatButton = playerRepeatButton;
    this.playerShuffleButton = playerShuffleButton;
    this.playerSongCurrentProgress = playerSongCurrentProgress;
    this.playerSongTotalTime = playerSongTotalTime;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentCardPlayerPlaybackControlsBinding inflate(
      @NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentCardPlayerPlaybackControlsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_card_player_playback_controls, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentCardPlayerPlaybackControlsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.dummy_fab;
      Space dummyFab = ViewBindings.findChildViewById(rootView, id);
      if (dummyFab == null) {
        break missingId;
      }

      LinearLayout playerFooterFrame = (LinearLayout) rootView;

      id = R.id.player_media_controller_container;
      RelativeLayout playerMediaControllerContainer = ViewBindings.findChildViewById(rootView, id);
      if (playerMediaControllerContainer == null) {
        break missingId;
      }

      id = R.id.player_next_button;
      ImageButton playerNextButton = ViewBindings.findChildViewById(rootView, id);
      if (playerNextButton == null) {
        break missingId;
      }

      id = R.id.player_play_pause_fab;
      FloatingActionButton playerPlayPauseFab = ViewBindings.findChildViewById(rootView, id);
      if (playerPlayPauseFab == null) {
        break missingId;
      }

      id = R.id.player_prev_button;
      ImageButton playerPrevButton = ViewBindings.findChildViewById(rootView, id);
      if (playerPrevButton == null) {
        break missingId;
      }

      id = R.id.player_progress_slider;
      SeekBar playerProgressSlider = ViewBindings.findChildViewById(rootView, id);
      if (playerProgressSlider == null) {
        break missingId;
      }

      id = R.id.player_repeat_button;
      ImageButton playerRepeatButton = ViewBindings.findChildViewById(rootView, id);
      if (playerRepeatButton == null) {
        break missingId;
      }

      id = R.id.player_shuffle_button;
      ImageButton playerShuffleButton = ViewBindings.findChildViewById(rootView, id);
      if (playerShuffleButton == null) {
        break missingId;
      }

      id = R.id.player_song_current_progress;
      TextView playerSongCurrentProgress = ViewBindings.findChildViewById(rootView, id);
      if (playerSongCurrentProgress == null) {
        break missingId;
      }

      id = R.id.player_song_total_time;
      TextView playerSongTotalTime = ViewBindings.findChildViewById(rootView, id);
      if (playerSongTotalTime == null) {
        break missingId;
      }

      return new FragmentCardPlayerPlaybackControlsBinding((LinearLayout) rootView, dummyFab,
          playerFooterFrame, playerMediaControllerContainer, playerNextButton, playerPlayPauseFab,
          playerPrevButton, playerProgressSlider, playerRepeatButton, playerShuffleButton,
          playerSongCurrentProgress, playerSongTotalTime);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
