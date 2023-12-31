// Generated by view binder compiler. Do not edit!
package com.poupa.vinylmusicplayer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.poupa.vinylmusicplayer.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentFlatPlayerPlaybackControlsBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final RelativeLayout playerMediaControllerContainer;

  @NonNull
  public final ImageButton playerNextButton;

  @NonNull
  public final ImageButton playerPlayPauseButton;

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

  private FragmentFlatPlayerPlaybackControlsBinding(@NonNull LinearLayout rootView,
      @NonNull RelativeLayout playerMediaControllerContainer, @NonNull ImageButton playerNextButton,
      @NonNull ImageButton playerPlayPauseButton, @NonNull ImageButton playerPrevButton,
      @NonNull SeekBar playerProgressSlider, @NonNull ImageButton playerRepeatButton,
      @NonNull ImageButton playerShuffleButton, @NonNull TextView playerSongCurrentProgress,
      @NonNull TextView playerSongTotalTime) {
    this.rootView = rootView;
    this.playerMediaControllerContainer = playerMediaControllerContainer;
    this.playerNextButton = playerNextButton;
    this.playerPlayPauseButton = playerPlayPauseButton;
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
  public static FragmentFlatPlayerPlaybackControlsBinding inflate(
      @NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentFlatPlayerPlaybackControlsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_flat_player_playback_controls, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentFlatPlayerPlaybackControlsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
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

      id = R.id.player_play_pause__button;
      ImageButton playerPlayPauseButton = ViewBindings.findChildViewById(rootView, id);
      if (playerPlayPauseButton == null) {
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

      return new FragmentFlatPlayerPlaybackControlsBinding((LinearLayout) rootView,
          playerMediaControllerContainer, playerNextButton, playerPlayPauseButton, playerPrevButton,
          playerProgressSlider, playerRepeatButton, playerShuffleButton, playerSongCurrentProgress,
          playerSongTotalTime);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
