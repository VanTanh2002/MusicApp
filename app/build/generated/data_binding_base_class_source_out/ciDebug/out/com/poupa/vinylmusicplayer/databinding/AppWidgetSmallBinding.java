// Generated by view binder compiler. Do not edit!
package com.poupa.vinylmusicplayer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
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

public final class AppWidgetSmallBinding implements ViewBinding {
  @NonNull
  private final GridLayout rootView;

  @NonNull
  public final GridLayout appWidgetSmall;

  @NonNull
  public final ImageButton buttonNext;

  @NonNull
  public final ImageButton buttonPrev;

  @NonNull
  public final ImageButton buttonTogglePlayPause;

  @NonNull
  public final ImageView image;

  @NonNull
  public final LinearLayout mediaActions;

  @NonNull
  public final LinearLayout mediaTitles;

  @NonNull
  public final LinearLayout separator;

  @NonNull
  public final TextView text;

  @NonNull
  public final TextView textSeparator;

  @NonNull
  public final TextView title;

  private AppWidgetSmallBinding(@NonNull GridLayout rootView, @NonNull GridLayout appWidgetSmall,
      @NonNull ImageButton buttonNext, @NonNull ImageButton buttonPrev,
      @NonNull ImageButton buttonTogglePlayPause, @NonNull ImageView image,
      @NonNull LinearLayout mediaActions, @NonNull LinearLayout mediaTitles,
      @NonNull LinearLayout separator, @NonNull TextView text, @NonNull TextView textSeparator,
      @NonNull TextView title) {
    this.rootView = rootView;
    this.appWidgetSmall = appWidgetSmall;
    this.buttonNext = buttonNext;
    this.buttonPrev = buttonPrev;
    this.buttonTogglePlayPause = buttonTogglePlayPause;
    this.image = image;
    this.mediaActions = mediaActions;
    this.mediaTitles = mediaTitles;
    this.separator = separator;
    this.text = text;
    this.textSeparator = textSeparator;
    this.title = title;
  }

  @Override
  @NonNull
  public GridLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static AppWidgetSmallBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static AppWidgetSmallBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.app_widget_small, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static AppWidgetSmallBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      GridLayout appWidgetSmall = (GridLayout) rootView;

      id = R.id.button_next;
      ImageButton buttonNext = ViewBindings.findChildViewById(rootView, id);
      if (buttonNext == null) {
        break missingId;
      }

      id = R.id.button_prev;
      ImageButton buttonPrev = ViewBindings.findChildViewById(rootView, id);
      if (buttonPrev == null) {
        break missingId;
      }

      id = R.id.button_toggle_play_pause;
      ImageButton buttonTogglePlayPause = ViewBindings.findChildViewById(rootView, id);
      if (buttonTogglePlayPause == null) {
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

      id = R.id.separator;
      LinearLayout separator = ViewBindings.findChildViewById(rootView, id);
      if (separator == null) {
        break missingId;
      }

      id = R.id.text;
      TextView text = ViewBindings.findChildViewById(rootView, id);
      if (text == null) {
        break missingId;
      }

      id = R.id.text_separator;
      TextView textSeparator = ViewBindings.findChildViewById(rootView, id);
      if (textSeparator == null) {
        break missingId;
      }

      id = R.id.title;
      TextView title = ViewBindings.findChildViewById(rootView, id);
      if (title == null) {
        break missingId;
      }

      return new AppWidgetSmallBinding((GridLayout) rootView, appWidgetSmall, buttonNext,
          buttonPrev, buttonTogglePlayPause, image, mediaActions, mediaTitles, separator, text,
          textSeparator, title);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
