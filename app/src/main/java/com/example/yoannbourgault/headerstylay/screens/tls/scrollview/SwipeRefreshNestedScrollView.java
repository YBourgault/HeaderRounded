package com.example.yoannbourgault.headerstylay.screens.tls;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.yoannbourgault.headerstylay.R;
import com.example.yoannbourgault.headerstylay.app.base.SwipeToRefreshNestedScrollView;


public class SwipeRefreshNestedScrollView extends NestedScrollView implements
        GestureDetector.OnGestureListener {

    private static final String TAG = SwipeToRefreshNestedScrollView.class.getSimpleName();

    private GestureDetectorCompat mGestureDetector;

    private boolean isOverScrollingTop = false;

    private float mActionDownY = 0;

    private int mDistanceScrimScrollY;

    public SwipeRefreshNestedScrollView(Context context) {
        super(context);
        initialize();
    }

    public SwipeRefreshNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public SwipeRefreshNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        //Log.d(TAG, String.format("onOverScrolled(clamped=%s, scrollY=%d)", clampedY, scrollY));
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        isOverScrollingTop = clampedY && scrollY == 0;
    }

    /**
     *
     * @param e1 {@code e1.getAction()} = {@link MotionEvent#ACTION_DOWN}
     * @param distanceY distance between old Y position and current one (if < 0 : last scroll motion goes down)
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        /*Log.d(TAG, String.format("onScroll(e1.getY()=%f, distanceY=%f)\n"
                        + "mDistanceScrimScrollY=%d\n"
                        + "isOverScrollingTop=%s",
                e1.getY(), distanceY, mDistanceScrimScrollY, isOverScrollingTop));*/

        if (mActionDownY != e1.getY()) {
            mActionDownY = e1.getY();
            mDistanceScrimScrollY = 0;
        }
        mDistanceScrimScrollY += distanceY;
        return true;
    }

    private void initialize() {
        inflate(getContext(), R.layout.swipe_refresh_nested_scroll_view, this);
        //Registering gesture detection
        mGestureDetector = new GestureDetectorCompat(getContext(), this);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return SwipeRefreshNestedScrollView.super.onTouchEvent(event);
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Mandatory but unused overridden methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
