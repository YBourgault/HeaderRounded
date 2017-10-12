package com.example.yoannbourgault.headerstylay.app.base;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class SwipeToRefreshNestedScrollView extends NestedScrollView implements
        GestureDetector.OnGestureListener {

    @SuppressWarnings("unused")
    private static final String TAG = SwipeToRefreshNestedScrollView.class.getSimpleName();

    private boolean mOverScrollSwipeToRefresh = false;

    private GestureDetectorCompat mGestureDetector;

    private float mActionDownY;
    private float mDistanceScrimScrollY;

    private OnSwipeToRefreshListener mSwipeToRefreshListener;

    public SwipeToRefreshNestedScrollView(Context context) {
        super(context);
        initialize();
    }

    public SwipeToRefreshNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public SwipeToRefreshNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        mGestureDetector = new GestureDetectorCompat(getContext(), this);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP && mSwipeToRefreshListener != null) {
                    Log.e(TAG, String.format("mDistanceScrimScrollY=%f", mDistanceScrimScrollY));
                    mSwipeToRefreshListener.onActionUp();
                    mDistanceScrimScrollY = 0;
                }
                mGestureDetector.onTouchEvent(motionEvent);
                return SwipeToRefreshNestedScrollView.super.onTouchEvent(motionEvent);
            }
        });
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        mOverScrollSwipeToRefresh = clampedY && scrollY == 0;
    }

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
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i(TAG, String.format("onScroll(distanceY=%f)", distanceY));
        if (mOverScrollSwipeToRefresh && distanceY < 0) {
            if (mActionDownY != e1.getY()) {
                //Record new scroll (vertical down)
                mActionDownY = e1.getY();
                mDistanceScrimScrollY = 0;
            }
            mDistanceScrimScrollY += ((-1) * distanceY);

            if (mSwipeToRefreshListener != null) {
                mSwipeToRefreshListener.onSwipingToRefresh((int) mDistanceScrimScrollY);
            }
            return true;
        } else {
            mActionDownY = e1.getY();
            mDistanceScrimScrollY = 0;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public void setOnSwipeToRefreshListener(OnSwipeToRefreshListener listener) {
        mSwipeToRefreshListener = listener;
    }

    private static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    interface OnSwipeToRefreshListener {
        void onSwipingToRefresh(int scrollY);
        void onActionUp();
    }
}
