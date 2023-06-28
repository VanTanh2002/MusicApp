package com.poupa.vinylmusicplayer.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.poupa.vinylmusicplayer.R;

import org.jetbrains.annotations.NotNull;

public class AutoTruncateTextView extends AppCompatTextView {

    public static final String TAG = AutoTruncateTextView.class.getSimpleName();

    private static final int RETRUNCATE_DELAY = 600;

    private static final String TRUNCATED_MARKER = "\u202F";

    private static final String MARKER_UNTRUNCATED = "\uFEFF";

    private String text;

    public AutoTruncateTextView(Context context) {
        super(context);
        init();
    }

    public AutoTruncateTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoTruncateTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTag(AutoTruncateTextView.TAG);

        // Enable long clicking when touching the text
        setLongClickable(true);

        // Blocks clicks from passing through this view
        setClickable(true);

        // Have to use this instead of maxlines in order for scrolling to work
        setSingleLine();
    }

    public TouchInterceptFrameLayout getTouchInterceptFrameLayout() {
        return (TouchInterceptFrameLayout) findParentRecursively(this, R.id.touch_intercept_framelayout);
    }

    public TouchInterceptFrameLayout getTouchInterceptFrameLayoutByTag() {
        return getRootView().findViewWithTag(TouchInterceptFrameLayout.TAG);
    }

    public ViewParent findParentRecursively(@NotNull View view, int targetId) {
        if (view.getId() == targetId) {
            return (ViewParent)view;
        }
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof View) {
            View parent = (View) view.getParent();
            if (parent == null) {
                return null;
            }
            return findParentRecursively(parent, targetId);
        } else {
            return null;
        }
    }

    public TouchInterceptHorizontalScrollView getTouchInterceptHorizontalScrollView() {
        return (TouchInterceptHorizontalScrollView) getParent();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        String fittedText = getText().toString();

        if (!fittedText.equals("")) {
            int textBoundsWidth = MeasureSpec.getSize(widthMeasureSpec);

            // If getSize return 0 (Android <= 6), get it from the layout
            if (textBoundsWidth == 0) {
                textBoundsWidth = getTouchInterceptFrameLayoutByTag().getMeasuredWidth();
                // If the size is still zero, admit that you've lost
                if (textBoundsWidth == 0) {
                    return;
                }
            }

            final boolean isUntruncated = fittedText.endsWith(MARKER_UNTRUNCATED);
            if (!fittedText.endsWith(TRUNCATED_MARKER) && !isUntruncated) {
                this.text = fittedText;
            }

            if (!isUntruncated && (textBoundsWidth < getPaint().measureText(fittedText))) {
                final String ellipsizedText = TextUtils.ellipsize(fittedText,
                        getPaint(),
                        (float) textBoundsWidth,
                        TextUtils.TruncateAt.END).toString();
                fittedText = ellipsizedText + TRUNCATED_MARKER;
            }

            setText(fittedText);
            initiateTruncateText(text, fittedText);
        }
    }

    public void initiateTruncateText(final String originalText, final String truncatedText) {
        if (!originalText.endsWith(TRUNCATED_MARKER)) {
            this.text = originalText;
        }

        final TouchInterceptHorizontalScrollView scrollView = getTouchInterceptHorizontalScrollView();
        post(() -> {
            if (isTruncated(truncatedText)) {
                if (originalText.equals(truncatedText) && !truncatedText.endsWith(MARKER_UNTRUNCATED)) {
                    scrollView.setScrollable(false);
                } else {
                    scrollView.setScrollable(true);
                    scrollView.setOnEndScrollListener(() -> retruncateScrollText(truncatedText));
                }
            } else if (!truncatedText.endsWith(MARKER_UNTRUNCATED)) {
                scrollView.setScrollable(false);
            }
        });
    }

    public boolean isTruncated(final String text) {
        return text.endsWith("…" + TRUNCATED_MARKER);
    }

    public boolean isUntruncated() {
        return getText().toString().endsWith(MARKER_UNTRUNCATED);
    }

    public void untruncateText() {
        String untruncatedText = text + MARKER_UNTRUNCATED;
        setText(untruncatedText);
    }

    public void retruncateScrollText(final String truncatedText) {
        Animator animator = ObjectAnimator
                .ofInt(getTouchInterceptHorizontalScrollView(), "scrollX", 0)
                .setDuration(RETRUNCATE_DELAY);

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isUntruncated()) {
                    setText(truncatedText);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animator.start();
    }
}
