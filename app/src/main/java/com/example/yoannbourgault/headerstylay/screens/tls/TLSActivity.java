package com.example.yoannbourgault.headerstylay.screens.tls;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.yoannbourgault.headerstylay.R;
import com.example.yoannbourgault.headerstylay.screens.tls.scrollview.SwipeRefreshNestedScrollView;
import com.example.yoannbourgault.headerstylay.screens.tls.toolbar.CollapseHeaderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TLSActivity extends AppCompatActivity implements TLSContract.View {

    @BindView(R.id.collapse_toolbar)
    CollapseHeaderView mToolbar;

    @BindView(R.id.scrollView)
    SwipeRefreshNestedScrollView mScrollView;

    @BindView(R.id.collapse_header_title)
    TextView mTitle;

    @SuppressWarnings("FieldCanBeLocal")
    private TLSContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapse_header);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
        mPresenter = new TLSPresenter(this);
        mPresenter.initialize();
    }

    @Override
    public void changeStatusBarColor(int colorResId) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(this, colorResId));
        }
    }

    @Override
    public void setToolbarTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public SwipeRefreshNestedScrollView getSwipeRefreshScrollView() {
        return mScrollView;
    }

    @Override
    public void changeHeaderHeight(int height) {
        mToolbar.changeHeight(height);
    }

    @Override
    public void changeScrollViewSpacing(int height) {
        mScrollView.changeSpacingTop(height);
    }

    @Override
    public int getDefaultHeaderHeight() {
        return getResources().getDimensionPixelOffset(R.dimen.collapse_header_default_height);
    }

    @Override
    public int getScrollViewSpacing() {
        return getResources().getDimensionPixelOffset(R.dimen.collapse_header_default_height);
    }

    @OnClick(R.id.collapse_header_close_icon)
    public void onClickCloseIcon() {
        finish();
    }

}
