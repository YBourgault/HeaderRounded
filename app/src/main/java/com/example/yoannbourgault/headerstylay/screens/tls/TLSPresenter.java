package com.example.yoannbourgault.headerstylay.screens.tls;

import com.example.yoannbourgault.headerstylay.R;
import com.example.yoannbourgault.headerstylay.screens.tls.scrollview.OnSwipeToRefresh;

class TLSPresenter implements TLSContract.Presenter, OnSwipeToRefresh {

    private static final String TAG = TLSPresenter.class.getSimpleName();

    private static final int COLOR_DEFAULT = R.color.colorDeviceDisabled;

    private final TLSContract.View mView;

    TLSPresenter(TLSContract.View view) {
        mView = view;
        initialize();
    }

    @Override
    public void initialize() {
        mView.changeStatusBarColor(COLOR_DEFAULT);
        mView.setToolbarTitle("Hello, world!");
        mView.getSwipeRefreshScrollView().setSwipeRefreshListener(this);
    }

    @Override
    public void onOverScrollTop(boolean overScrollTop, int distanceScrollTop) {
        mView.changeHeaderHeight(mView.getDefaultHeaderHeight() + distanceScrollTop);
        mView.changeScrollViewSpacing(mView.getScrollViewSpacing() + distanceScrollTop);
    }

    @Override
    public void onStopScrolling() {
        mView.changeHeaderHeight(mView.getDefaultHeaderHeight());
    }
}
