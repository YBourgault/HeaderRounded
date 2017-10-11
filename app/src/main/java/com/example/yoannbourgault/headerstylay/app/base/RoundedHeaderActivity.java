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
        NestedScrollView.OnScrollChangeListener {

    private static final String TAG = RoundedHeaderActivity.class.getSimpleName();

    private FrameLayout mIconContainer;
    private View mScrim;

    private int mMinIconSize = 0;
    private int mMaxIconSize = 0;

    private int mMinScrimSize = 0;
    private int mMaxScrimSize = 0;

    private float mMarginLeftStart = 0;
    private float mMarginTopStart = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        changeStatusBarColor(getStatusBarColorResId());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        NestedScrollView scrollView = getScrollView();
        if (scrollView != null) {
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
        } else {
            Log.e(TAG, "ScrollView not found !!");
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
            int oldScrollY) {
        if (mIconContainer != null && mScrim != null) {
            int scrimHeightPercent = transformScrim(scrollY);
            Log.e(TAG, String.format("Scrim percent = %d", scrimHeightPercent));
            transformIcon(scrimHeightPercent);
        } else {
            Log.e(TAG, "Header icon NULL !!");
        }
    }

    protected abstract int getLayoutResId();

    protected abstract int getStatusBarColorResId();

    protected abstract NestedScrollView getScrollView();

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

}
