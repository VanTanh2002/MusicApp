// Generated by view binder compiler. Do not edit!
package com.poupa.vinylmusicplayer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.card.MaterialCardView;
import com.poupa.vinylmusicplayer.R;
import com.poupa.vinylmusicplayer.views.AutoTruncateTextView;
import com.poupa.vinylmusicplayer.views.TouchInterceptFrameLayout;
import com.poupa.vinylmusicplayer.views.TouchInterceptHorizontalScrollView;
import com.poupa.vinylmusicplayer.views.WidthFitSquareLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemGridBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final ImageView image;

  @NonNull
  public final MaterialCardView imageBorderTheme;

  @NonNull
  public final WidthFitSquareLayout imageContainer;

  @NonNull
  public final LinearLayout paletteColorContainer;

  @NonNull
  public final AutoTruncateTextView text;

  @NonNull
  public final TouchInterceptHorizontalScrollView textScrollview;

  @NonNull
  public final AutoTruncateTextView title;

  @NonNull
  public final TouchInterceptHorizontalScrollView titleScrollview;

  @NonNull
  public final TouchInterceptFrameLayout touchInterceptFramelayout;

  private ItemGridBinding(@NonNull FrameLayout rootView, @NonNull ImageView image,
      @NonNull MaterialCardView imageBorderTheme, @NonNull WidthFitSquareLayout imageContainer,
      @NonNull LinearLayout paletteColorContainer, @NonNull AutoTruncateTextView text,
      @NonNull TouchInterceptHorizontalScrollView textScrollview,
      @NonNull AutoTruncateTextView title,
      @NonNull TouchInterceptHorizontalScrollView titleScrollview,
      @NonNull TouchInterceptFrameLayout touchInterceptFramelayout) {
    this.rootView = rootView;
    this.image = image;
    this.imageBorderTheme = imageBorderTheme;
    this.imageContainer = imageContainer;
    this.paletteColorContainer = paletteColorContainer;
    this.text = text;
    this.textScrollview = textScrollview;
    this.title = title;
    this.titleScrollview = titleScrollview;
    this.touchInterceptFramelayout = touchInterceptFramelayout;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemGridBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemGridBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_grid, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemGridBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.image;
      ImageView image = ViewBindings.findChildViewById(rootView, id);
      if (image == null) {
        break missingId;
      }

      id = R.id.imageBorderTheme;
      MaterialCardView imageBorderTheme = ViewBindings.findChildViewById(rootView, id);
      if (imageBorderTheme == null) {
        break missingId;
      }

      id = R.id.image_container;
      WidthFitSquareLayout imageContainer = ViewBindings.findChildViewById(rootView, id);
      if (imageContainer == null) {
        break missingId;
      }

      id = R.id.palette_color_container;
      LinearLayout paletteColorContainer = ViewBindings.findChildViewById(rootView, id);
      if (paletteColorContainer == null) {
        break missingId;
      }

      id = R.id.text;
      AutoTruncateTextView text = ViewBindings.findChildViewById(rootView, id);
      if (text == null) {
        break missingId;
      }

      id = R.id.text_scrollview;
      TouchInterceptHorizontalScrollView textScrollview = ViewBindings.findChildViewById(rootView, id);
      if (textScrollview == null) {
        break missingId;
      }

      id = R.id.title;
      AutoTruncateTextView title = ViewBindings.findChildViewById(rootView, id);
      if (title == null) {
        break missingId;
      }

      id = R.id.title_scrollview;
      TouchInterceptHorizontalScrollView titleScrollview = ViewBindings.findChildViewById(rootView, id);
      if (titleScrollview == null) {
        break missingId;
      }

      id = R.id.touch_intercept_framelayout;
      TouchInterceptFrameLayout touchInterceptFramelayout = ViewBindings.findChildViewById(rootView, id);
      if (touchInterceptFramelayout == null) {
        break missingId;
      }

      return new ItemGridBinding((FrameLayout) rootView, image, imageBorderTheme, imageContainer,
          paletteColorContainer, text, textScrollview, title, titleScrollview,
          touchInterceptFramelayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
