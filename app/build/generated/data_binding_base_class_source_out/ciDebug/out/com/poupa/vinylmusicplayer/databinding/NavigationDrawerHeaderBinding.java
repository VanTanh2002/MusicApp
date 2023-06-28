// Generated by view binder compiler. Do not edit!
package com.poupa.vinylmusicplayer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.percentlayout.widget.PercentFrameLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.poupa.vinylmusicplayer.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class NavigationDrawerHeaderBinding implements ViewBinding {
  @NonNull
  private final PercentFrameLayout rootView;

  @NonNull
  public final PercentFrameLayout header;

  @NonNull
  public final ImageView image;

  @NonNull
  public final TextView text;

  @NonNull
  public final TextView title;

  private NavigationDrawerHeaderBinding(@NonNull PercentFrameLayout rootView,
      @NonNull PercentFrameLayout header, @NonNull ImageView image, @NonNull TextView text,
      @NonNull TextView title) {
    this.rootView = rootView;
    this.header = header;
    this.image = image;
    this.text = text;
    this.title = title;
  }

  @Override
  @NonNull
  public PercentFrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static NavigationDrawerHeaderBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static NavigationDrawerHeaderBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.navigation_drawer_header, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static NavigationDrawerHeaderBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      PercentFrameLayout header = (PercentFrameLayout) rootView;

      id = R.id.image;
      ImageView image = ViewBindings.findChildViewById(rootView, id);
      if (image == null) {
        break missingId;
      }

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

      return new NavigationDrawerHeaderBinding((PercentFrameLayout) rootView, header, image, text,
          title);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}