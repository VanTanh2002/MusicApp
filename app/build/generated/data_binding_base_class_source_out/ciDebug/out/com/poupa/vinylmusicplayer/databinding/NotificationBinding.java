// Generated by view binder compiler. Do not edit!
package com.poupa.vinylmusicplayer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.poupa.vinylmusicplayer.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class NotificationBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageButton actionFav;

  @NonNull
  public final ImageButton actionNext;

  @NonNull
  public final ImageButton actionPlayPause;

  @NonNull
  public final ImageButton actionPrev;

  @NonNull
  public final FrameLayout iconGroup;

  @NonNull
  public final ImageView image;

  @NonNull
  public final LinearLayout mediaActions;

  @NonNull
  public final LinearLayout mediaTitles;

  @NonNull
  public final LinearLayout root;

  @NonNull
  public final TextView text;

  @NonNull
  public final TextView title;

  private NotificationBinding(@NonNull LinearLayout rootView, @NonNull ImageButton actionFav,
      @NonNull ImageButton actionNext, @NonNull ImageButton actionPlayPause,
      @NonNull ImageButton actionPrev, @NonNull FrameLayout iconGroup, @NonNull ImageView image,
      @NonNull LinearLayout mediaActions, @NonNull LinearLayout mediaTitles,
      @NonNull LinearLayout root, @NonNull TextView text, @NonNull TextView title) {
    this.rootView = rootView;
    this.actionFav = actionFav;
    this.actionNext = actionNext;
    this.actionPlayPause = actionPlayPause;
    this.actionPrev = actionPrev;
    this.iconGroup = iconGroup;
    this.image = image;
    this.mediaActions = mediaActions;
    this.mediaTitles = mediaTitles;
    this.root = root;
    this.text = text;
    this.title = title;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static NotificationBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static NotificationBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.notification, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static NotificationBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.action_fav;
      ImageButton actionFav = ViewBindings.findChildViewById(rootView, id);
      if (actionFav == null) {
        break missingId;
      }

      id = R.id.action_next;
      ImageButton actionNext = ViewBindings.findChildViewById(rootView, id);
      if (actionNext == null) {
        break missingId;
      }

      id = R.id.action_play_pause;
      ImageButton actionPlayPause = ViewBindings.findChildViewById(rootView, id);
      if (actionPlayPause == null) {
        break missingId;
      }

      id = R.id.action_prev;
      ImageButton actionPrev = ViewBindings.findChildViewById(rootView, id);
      if (actionPrev == null) {
        break missingId;
      }

      id = R.id.icon_group;
      FrameLayout iconGroup = ViewBindings.findChildViewById(rootView, id);
      if (iconGroup == null) {
        break missingId;
      }

      id = R.id.image;
      ImageView image = ViewBindings.findChildViewById(rootView, id);
      if (image == null) {
        break missingId;
      }

      id = R.id.media_actions;
      LinearLayout mediaActions = ViewBindings.findChildViewById(rootView, id);
      if (mediaActions == null) {
        break missingId;
      }

      id = R.id.media_titles;
      LinearLayout mediaTitles = ViewBindings.findChildViewById(rootView, id);
      if (mediaTitles == null) {
        break missingId;
      }

      LinearLayout root = (LinearLayout) rootView;

      id = R.id.text;
      TextView text = ViewBindings.findChildViewById(rootView, id);
      if (text == null) {
        break missingId;
      }

      id = R.id.title;
      TextView title = ViewBindings.findChildViewById(rootView, id);
      if (title == null) {
        break missingId;
      }

      return new NotificationBinding((LinearLayout) rootView, actionFav, actionNext,
          actionPlayPause, actionPrev, iconGroup, image, mediaActions, mediaTitles, root, text,
          title);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
