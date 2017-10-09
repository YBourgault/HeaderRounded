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
    private FrameLayout mHeaderIconContainer;
    private ImageView mHeaderIcon;
    private View mScrim;

    private int mMinIconSize = 0;
    private int mMaxIconSize = 0;
    private int mMinScrimSize = 0;
    private int mMaxScrimSize = 0;
    private int mScrollYHalf = 0;
    private int mScrimHalfSize;
    private int mMaxIconX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        changeStatusBarColor(getStatusBarColorResId());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        mScrollView = getScrollView();
        if (mScrollView != null) {
            mHeaderIconContainer = getHeaderIconContainer();
            mHeaderIcon = getHeaderIcon();
            mContent = getHeaderContainer();
            mScrim = getHeaderScrim();
            mMaxScrimSize = getResources().getDimensionPixelSize(R.dimen.header_scrim);
            mMinScrimSize = getResources().getDimensionPixelSize(R.dimen.header_scrim_min);
            mScrimHalfSize = ((mMaxScrimSize - mMinScrimSize) / 2) + mMinScrimSize;
            mMaxIconSize = getResources().getDimensionPixelSize(R.dimen.image_width);
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                mMinIconSize = TypedValue.complexToDimensionPixelSize(tv.data,
                        getResources().getDisplayMetrics());
            }
            mScrollView.setOnScrollChangeListener(this);
        } else {
            Log.e(TAG, "ScrollView NULL !!");
        }
    }

    protected abstract int getLayoutResId();

    protected abstract int getStatusBarColorResId();

    protected abstract ImageView getHeaderIcon();

    protected abstract NestedScrollView getScrollView();

    protected abstract RelativeLayout getHeaderContainer();

    protected abstract View getHeaderScrim();

    protected abstract FrameLayout getHeaderIconContainer();

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
        if (mHeaderIconContainer != null && mScrim != null) {
            int size;

            //Container
            ViewGroup.LayoutParams contentParams = mScrim.getLayoutParams();
            size = mMaxScrimSize - scrollY;
            if (size < mMinScrimSize) {
                size = mMinScrimSize;
            }
            contentParams.height = size;
            mScrim.setLayoutParams(contentParams);

            //Icon
            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) mHeaderIconContainer.getLayoutParams();
            size = mMaxIconSize - scrollY;
            if (size < mMinIconSize) {
                size = mMinIconSize;
            }
            params.width = size;
            params.height = size;

            mHeaderIconContainer.setLayoutParams(params);

            int x;
            if (mScrim.getHeight() >= mScrimHalfSize) {
                x = (mContent.getWidth() / 2) - (mHeaderIconContainer.getWidth() / 2);
                if (mScrim.getHeight() == mScrimHalfSize) {
                    mMaxIconX = (int) mScrim.getX();
                }
            } else {
                int percentScrolling = 100;
                x = (percentScrolling / 100) * mMaxIconX;
            }
            mHeaderIconContainer.setX(x);

        } else {
            Log.e(TAG, "Header icon NULL !!");
        }
    }
}
