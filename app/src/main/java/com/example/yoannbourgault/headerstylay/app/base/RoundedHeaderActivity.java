package com.example.yoannbourgault.headerstylay.app.base;

import android.content.Context;
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

    private float mMaxIconPosX = 0;
    private float mMaxIconPosY = 0;

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
            float halfIconDiff = ((mMaxIconSize - mMinIconSize) / 2);
            mMaxIconPosY = mIconContainer.getY() + halfIconDiff ;
            mMaxIconPosX = mIconContainer.getX() + halfIconDiff;
            scrollView.setOnScrollChangeListener(this);
        } else {
            Log.e(TAG, "ScrollView not found !!");
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
            int oldScrollY) {
        if (mIconContainer != null && mScrim != null) {
            transformScrim(scrollY);
            int scrimHeightPercent = getScrimScrollingPercent();
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

    private void transformScrim(int scrollY) {
        int size;
        ViewGroup.LayoutParams contentParams = mScrim.getLayoutParams();
        size = mMaxScrimSize - scrollY;
        if (size < mMinScrimSize) {
            size = mMinScrimSize;
        }
        contentParams.height = size;
        mScrim.setLayoutParams(contentParams);
    }

    private void transformIcon(int scrimHeightPercent) {
        //Resize
        int rangeIconSize = mMaxIconSize - mMinIconSize;
        float iconSize = (((scrimHeightPercent / 100F) * rangeIconSize) + mMinIconSize);
        float scale = (iconSize / mMaxIconSize);
        mIconContainer.setScaleX(scale);
        mIconContainer.setScaleY(scale);

        //Position
        float translationX;
        if (scrimHeightPercent >= 50) {
            // >= 50% : icon center_horizontal, move only on Y axe
            translationX = 0;
            if (mIconContainer.getX() > mMaxIconPosX) {
                mMaxIconPosX = mIconContainer.getX();
            }
        } else {
            // < 50% : icon moving on X and Y axes (according to scrim scrolling percentage)
            float x = (((scrimHeightPercent * 2F) / 100F) * mMaxIconPosX);
            translationX = (mMaxIconPosX - x) * (-1F);
        }
        mIconContainer.setTranslationX(translationX);
        float translationY = ((mMaxIconPosY - ((scrimHeightPercent / 100F) * mMaxIconPosY))) * (-1);
        mIconContainer.setTranslationY(translationY);
    }

    private int getScrimScrollingPercent() {
        int currentScrimHeight = mScrim.getHeight();
        int scrimRange = mMaxScrimSize - mMinScrimSize;
        int scrimDiff = mMaxScrimSize - currentScrimHeight;
        return (int) (((scrimRange - scrimDiff) * 100F) / scrimRange);
    }

    private static int dpFromPx(final Context context, final float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }
}
