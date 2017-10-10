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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.yoannbourgault.headerstylay.R;

public abstract class RoundedHeaderActivity extends AppCompatActivity implements
        NestedScrollView.OnScrollChangeListener {

    private static final String TAG = RoundedHeaderActivity.class.getSimpleName();

    private NestedScrollView mScrollView;
    private RelativeLayout mContent;
    private FrameLayout mIconContainer;
    private ImageView mHeaderIcon;
    private View mScrim;

    private int mMinIconSize = 0;
    private int mMaxIconSize = 0;
    private int mMinScrimSize = 0;
    private int mMaxScrimSize = 0;
    private int mScrollYHalf = 0;
    private int mHalfScrimSize;
    private int mMaxIconX;

    private int mMaxIconPosX;
    private int mMaxIconPosY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        changeStatusBarColor(getStatusBarColorResId());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScrollView = getScrollView();
        if (mScrollView != null) {
            mIconContainer = getIconContainer();
            mScrim = getHeaderScrim();

            mMaxScrimSize = getResources().getDimensionPixelSize(R.dimen.header_scrim);
            mMinScrimSize = getResources().getDimensionPixelSize(R.dimen.header_scrim_min);
            mHalfScrimSize = ((mMaxScrimSize - mMinScrimSize) / 2) + mMinScrimSize;

            mMaxIconSize = getResources().getDimensionPixelSize(R.dimen.image_width);
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                mMinIconSize = TypedValue.complexToDimensionPixelSize(tv.data,
                        getResources().getDisplayMetrics());
            }
            mMaxIconPosY = (int) mIconContainer.getY();
            mMaxIconPosX = (int) mIconContainer.getX();
            mScrollView.setOnScrollChangeListener(this);
        } else {
            Log.e(TAG, "ScrollView not found !!");
        }
    }

    protected abstract int getLayoutResId();

    protected abstract int getStatusBarColorResId();

    protected abstract ImageView getHeaderIcon();

    protected abstract NestedScrollView getScrollView();

    protected abstract RelativeLayout getHeaderContainer();

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

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
            int oldScrollY) {
        if (mIconContainer != null && mScrim != null) {
            int size;

            //Scrim
            ViewGroup.LayoutParams contentParams = mScrim.getLayoutParams();
            size = mMaxScrimSize - scrollY;
            if (size < mMinScrimSize) {
                size = mMinScrimSize;
            }
            contentParams.height = size;
            mScrim.setLayoutParams(contentParams);

            int currentScrimHeight = mScrim.getHeight();
            int scrimRange = mMaxScrimSize - mMinScrimSize;
            int scrimDiff = mMaxScrimSize - currentScrimHeight;
            int scrimHeightPercent = (int) (((scrimRange - scrimDiff) * 100F) / scrimRange);

            Log.e(TAG, String.format("currentScrimHeight = %d\n" +
                            "scrimRange = %d\n" +
                            "scrimDiff = %d\n" +
                            "Scrim at %d %%",
                    currentScrimHeight, scrimRange, scrimDiff, scrimHeightPercent));

            //Icon
            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) mIconContainer.getLayoutParams();
            mIconContainer.setLayoutParams(params);

            int x;
            if (scrimHeightPercent >= 50) {
                //icon center_horizontal, move on Y axe
                x = (int) ((mScrim.getWidth() / 2F) - (mIconContainer.getWidth() / 2F));
            } else {
                //icon moving on X and Y axes
                x = (int) (((scrimHeightPercent * 2F) / 100F) * mMaxIconPosX);
            }

            Log.e(TAG, String.format("Max Pos Y = %d\n" +
                            "Ratio = %f\n" +
                            "Calculated Y = %d", mMaxIconPosY, scrimHeightPercent / 100F,
                    (scrimHeightPercent / 100) * mMaxIconPosY));

            setIconX(x);
            setIconY((int) ((scrimHeightPercent / 100F) * mMaxIconPosY));
        } else {
            Log.e(TAG, "Header icon NULL !!");
        }
    }

    private void setIconX(final int x) {
        mIconContainer.setX(x);
    }

    private void setIconY(final int y) {
        mIconContainer.setY(y);
    }
}
