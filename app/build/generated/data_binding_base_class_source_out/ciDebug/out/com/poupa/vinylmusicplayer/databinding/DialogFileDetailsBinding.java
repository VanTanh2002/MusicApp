// Generated by view binder compiler. Do not edit!
package com.poupa.vinylmusicplayer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public final class DialogFileDetailsBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView discographyInfo;

  @NonNull
  public final TextView filesystemInfo;

  private DialogFileDetailsBinding(@NonNull LinearLayout rootView,
      @NonNull TextView discographyInfo, @NonNull TextView filesystemInfo) {
    this.rootView = rootView;
    this.discographyInfo = discographyInfo;
    this.filesystemInfo = filesystemInfo;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogFileDetailsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogFileDetailsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_file_details, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogFileDetailsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.discography_info;
      TextView discographyInfo = ViewBindings.findChildViewById(rootView, id);
      if (discographyInfo == null) {
        break missingId;
      }

      id = R.id.filesystem_info;
      TextView filesystemInfo = ViewBindings.findChildViewById(rootView, id);
      if (filesystemInfo == null) {
        break missingId;
      }

      return new DialogFileDetailsBinding((LinearLayout) rootView, discographyInfo, filesystemInfo);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
