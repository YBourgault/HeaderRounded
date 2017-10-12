package com.example.yoannbourgault.headerstylay.app.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.yoannbourgault.headerstylay.R;

public abstract class RoundedHeaderActivity extends AppCompatActivity implements
        NestedScrollView.OnScrollChangeListener,
        SwipeToRefreshNestedScrollView.OnSwipeToRefreshListener {

    private static final String TAG = RoundedHeaderActivity.class.getSimpleName();

    private RelativeLayout mHeader;
    private FrameLayout mIconContainer;
    private View mScrim;

    private int mMinIconSize = 0;
    private int mMaxIconSize = 0;

    private int mMinScrimSize = 0;
    private int mMaxScrimSize = 0;

    private float mMarginLeftStart = 0;
    private float mMarginTopStart = 0;

    private int mSwipeRefreshScrollY = 0;

    private boolean swipingToRefresh = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        changeStatusBarColor(getStatusBarColorResId());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        SwipeToRefreshNestedScrollView scrollView = getScrollView();
        if (scrollView != null) {
            mHeader = getHeader();
            //Init scrim
            mScrim = getHeaderScrim();
            mMaxScrimSize = getResources().getDimensionPixelSize(R.dimen.scrim_height_max);
            mMinScrimSize = getResources().getDimensionPixelSize(R.dimen.scrim_height_min);
            //Init icon
            mIconContainer = getIconContainer();
            mMaxIconSize = getResources().getDimensionPixelSize(R.dimen.image_size_max);
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                mMinIconSize = TypedValue.complexToDimensionPixelSize(tv.data,
                        getResources().getDisplayMetrics());
            }

            ViewTreeObserver vto = mScrim.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mScrim.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mMarginLeftStart = mScrim.getX() + (mScrim.getWidth() / 2) - (mMaxIconSize / 2);
                    mMarginTopStart = mScrim.getY() + mScrim.getHeight() - (mMaxIconSize / 2);
                    RelativeLayout.LayoutParams params =
                            (RelativeLayout.LayoutParams) mIconContainer.getLayoutParams();
                    params.setMargins((int) mMarginLeftStart, (int) mMarginTopStart, 0, 0);
                    mIconContainer.setLayoutParams(params);
                }
            });
            scrollView.setOnScrollChangeListener(this);
            scrollView.setOnSwipeToRefreshListener(this);
        } else {
            Log.e(TAG, "ScrollView not found !!");
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        Log.i(TAG, String.format("onScrollChange(scrollX=%d, scrollY=%d, oldScrollX=%d, oldScrollY=%d)", scrollX, scrollY, oldScrollX, oldScrollY));
        if (mIconContainer != null && mScrim != null) {
            int scrimHeightPercent = transformScrim(scrollY);
            transformIcon(scrimHeightPercent);
        } else {
            Log.e(TAG, "Header icon NULL !!");
        }
    }

    protected abstract int getLayoutResId();

    protected abstract int getStatusBarColorResId();

    protected abstract SwipeToRefreshNestedScrollView getScrollView();

    protected abstract RelativeLayout getHeader();

    protected abstract View getHeaderScrim();

    protected abstract FrameLayout getIconContainer();

    protected void changeStatusBarColor(int colorResId) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(this, colorResId));
        }
    }

    private int transformScrim(int scrollY) {
        //Log.i(TAG, String.format("transformScrim(scrollY=%d)", scrollY));
        int size;
        ViewGroup.LayoutParams contentParams = mScrim.getLayoutParams();
        size = mMaxScrimSize - scrollY;
        if (size < mMinScrimSize) {
            size = mMinScrimSize;
        }
        contentParams.height = size;
        mScrim.setLayoutParams(contentParams);
        int scrimRange = mMaxScrimSize - mMinScrimSize;
        int scrimDiff = mMaxScrimSize - size;
        return (int) (((scrimRange - scrimDiff) * 100F) / scrimRange);
    }

    private void transformIcon(final int scrimHeightPercent) {
        //Log.i(TAG, String.format("transformIcon(scrimHeightPercent=%d)", scrimHeightPercent));
        //Resize
        int rangeIconSize = mMaxIconSize - mMinIconSize;
        float iconSize = (((scrimHeightPercent / 100F) * rangeIconSize) + mMinIconSize);
        float scale = (iconSize / mMaxIconSize);
        mIconContainer.setScaleX(scale);
        mIconContainer.setScaleY(scale);

        //Position
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) mIconContainer.getLayoutParams();
        int topMargin;
        if (scrimHeightPercent == 100) {
            topMargin = (int) (mScrim.getY() + mScrim.getHeight() - (mMaxIconSize / 2));
        } else {
            topMargin = (int) (((scrimHeightPercent / 100F) * mMarginTopStart) -
                    ((mMaxIconSize - mMinIconSize) / 2F));
        }

        int leftMargin;
        if (scrimHeightPercent >= 50) {
            leftMargin = (int) mMarginLeftStart;
        } else {
            float x = (((scrimHeightPercent * 2F) / 100F) * mMarginLeftStart);
            leftMargin = (int) (x - ((mMaxIconSize - mMinIconSize) / 2F));
        }
        if (params.topMargin == topMargin && params.leftMargin == leftMargin) {
            return;
        }
        params.topMargin = topMargin;
        params.leftMargin = leftMargin;
        mIconContainer.setLayoutParams(params);
    }

    @Override
    public void onSwipingToRefresh(int scrollY) {
        //Log.i(TAG, String.format("onSwipingToRefresh(scrollY=%d)", scrollY));
        mSwipeRefreshScrollY = scrollY;
        if (mScrim != null) {
            //Header height
            ViewGroup.LayoutParams headerParams = mHeader.getLayoutParams();
            headerParams.height = getResources().getDimensionPixelOffset(R.dimen.header_height)
                    + scrollY;
            mHeader.setLayoutParams(headerParams);
            //Scrim height
            RelativeLayout.LayoutParams scrimParams =
                    (RelativeLayout.LayoutParams) mScrim.getLayoutParams();
            scrimParams.height = getResources().getDimensionPixelOffset(R.dimen.scrim_height_max)
                    + scrollY;
            mScrim.setLayoutParams(scrimParams);
            //Icon top margin
            RelativeLayout.LayoutParams iconParams =
                    (RelativeLayout.LayoutParams) mIconContainer.getLayoutParams();
            iconParams.topMargin = (int) (mScrim.getY() + mScrim.getHeight() - (mMaxIconSize / 2));
            mIconContainer.setLayoutParams(iconParams);
        }
    }

    @Override
    public void onActionUp() {
        //Log.i(TAG, String.format("onActionUp() [mSwipeRefreshScrollY=%d]", mSwipeRefreshScrollY));
        if (!swipingToRefresh) {
            Log.e(TAG, String.format("mSwipeRefreshScrollY <= 0 (%d)", mSwipeRefreshScrollY));
            return;
        }
        if (mScrim != null) {
            Log.e(TAG, String.format("mSwipeRefreshScrollY > 0 (%d)", mSwipeRefreshScrollY));
            //Header height
            ViewGroup.LayoutParams headerParams = mHeader.getLayoutParams();
            headerParams.height = getResources().getDimensionPixelOffset(R.dimen.header_height);
            mHeader.setLayoutParams(headerParams);
            //Scrim height
            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) mScrim.getLayoutParams();
            params.height = getResources().getDimensionPixelOffset(
                    R.dimen.scrim_height_max);
            mScrim.setLayoutParams(params);
            //Icon top margin
            RelativeLayout.LayoutParams iconParams =
                    (RelativeLayout.LayoutParams) mIconContainer.getLayoutParams();
            iconParams.topMargin = (int) (mScrim.getY() + mMaxScrimSize - (mMaxIconSize / 2));
            mIconContainer.setLayoutParams(iconParams);

            //Refresh data?
            if (mSwipeRefreshScrollY > dpFromPx(96)) {
                refreshContent();
            }
            mSwipeRefreshScrollY = 0;
        }
    }

    private float dpFromPx(final float px) {
        return px / getResources().getDisplayMetrics().density;
    }


    protected abstract void refreshContent();

}
