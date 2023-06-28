package com.poupa.vinylmusicplayer.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

public class TouchInterceptHorizontalScrollView extends HorizontalScrollView {

    private GestureDetector mDetector;
    private boolean mIsScrolling = false;

    public static final String TAG = TouchInterceptHorizontalScrollView.class.getSimpleName();

    private static final int ON_END_SCROLL_DELAY = 1000;

    private long lastScrollUpdate = -1;
    private boolean scrollable;
    private Rect scrollViewRect;
    private OnEndScrollListener onEndScrollListener;

    // Whether user is interacting with this again and to cancel text retruncate
    private boolean cancel;

    public TouchInterceptHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        lastScrollUpdate = -1;
        scrollable = true;
        scrollViewRect = new Rect();
        setLongClickable(false);
        setTag(TouchInterceptHorizontalScrollView.TAG);
        setHorizontalScrollBarEnabled(false);
        mDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChild(getTouchInterceptTextView(), widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec,
                                int parentHeightMeasureSpec) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();

        final int horizontalPadding = getPaddingLeft() + getPaddingRight();
        final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(0, MeasureSpec.getSize(parentWidthMeasureSpec) - horizontalPadding),
                MeasureSpec.UNSPECIFIED);

        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom(), lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    public AutoTruncateTextView getTouchInterceptTextView() {
        return (AutoTruncateTextView) this.getChildAt(0);
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public boolean isScrollable() {
        return scrollable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        onTouchEvent(e);
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // Fire the detector
        mDetector.onTouchEvent(e);

        // Detect if we have a scroll that just finished
        if (e.getAction() == MotionEvent.ACTION_UP && mIsScrolling) {
            scrollFinished();
        }

        return super.onTouchEvent(e);
    }

    public void setOnEndScrollListener(OnEndScrollListener onEndScrollListener) {
        this.onEndScrollListener = onEndScrollListener;
    }

    private void scrollFinished() {
        mIsScrolling  = false;
        cancel = false;
        postDelayed(new ScrollStateHandler(), ON_END_SCROLL_DELAY);
        lastScrollUpdate = System.currentTimeMillis();
    }

    interface OnEndScrollListener {

        void onEndScroll();
    }

    private class ScrollStateHandler implements Runnable {
        @Override
        public void run() {
            if (!cancel) {
                // Hasn't been touched for some time
                long currentTime = System.currentTimeMillis();
                if ((currentTime - lastScrollUpdate) > ON_END_SCROLL_DELAY) {
                    lastScrollUpdate = -1;
                    if (onEndScrollListener != null) {
                        onEndScrollListener.onEndScroll();
                    }
                } else {
                    postDelayed(this, ON_END_SCROLL_DELAY);
                }
            }
        }
    }

    public Rect getScrollViewRect() {
        getGlobalVisibleRect(scrollViewRect);
        return scrollViewRect;
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            ViewGroup viewGroup = getViewGroup();

            if (viewGroup != null) {
                viewGroup.performClick();
            }

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            ViewGroup viewGroup = getViewGroup();

            if (viewGroup != null) {
                viewGroup.performLongClick();
            }
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mIsScrolling = true;

            getTouchInterceptTextView().untruncateText();

            return false;
        }

        private ViewGroup getViewGroup() {
            TouchInterceptFrameLayout touchInterceptFrameLayout = getTouchInterceptTextView().getTouchInterceptFrameLayout();

            if (touchInterceptFrameLayout != null) {
                return (ViewGroup) touchInterceptFrameLayout.getParent();
            }

            return null;
        }
    }
}
