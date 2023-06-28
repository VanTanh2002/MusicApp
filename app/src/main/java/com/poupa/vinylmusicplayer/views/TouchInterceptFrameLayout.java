package com.poupa.vinylmusicplayer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TouchInterceptFrameLayout extends FrameLayout {

    public static final String TAG = TouchInterceptFrameLayout.class.getSimpleName();

    public TouchInterceptFrameLayout(@NonNull Context context) {
        this(context, null);
        init();
    }

    public TouchInterceptFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public TouchInterceptFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTag(TouchInterceptFrameLayout.TAG);
    }
}
