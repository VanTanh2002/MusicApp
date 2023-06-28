// Generated by view binder compiler. Do not edit!
package com.poupa.vinylmusicplayer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.poupa.vinylmusicplayer.R;
import com.poupa.vinylmusicplayer.views.StatusBarView;
import java.lang.NullPointerException;
import java.lang.Override;

public final class StatusBarBinding implements ViewBinding {
  @NonNull
  private final StatusBarView rootView;

  @NonNull
  public final StatusBarView statusBar;

  private StatusBarBinding(@NonNull StatusBarView rootView, @NonNull StatusBarView statusBar) {
    this.rootView = rootView;
    this.statusBar = statusBar;
  }

  @Override
  @NonNull
  public StatusBarView getRoot() {
    return rootView;
  }

  @NonNull
  public static StatusBarBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static StatusBarBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.status_bar, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static StatusBarBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    StatusBarView statusBar = (StatusBarView) rootView;

    return new StatusBarBinding((StatusBarView) rootView, statusBar);
  }
}
