package com.example.yoannbourgault.headerstylay.screens.tls;

import com.example.yoannbourgault.headerstylay.screens.tls.scrollview.SwipeRefreshNestedScrollView;

interface TLSContract {

    interface View {

        void changeStatusBarColor(int colorResId);

        void setToolbarTitle(String title);

        SwipeRefreshNestedScrollView getSwipeRefreshScrollView();

        void changeHeaderHeight(int height);

        void changeScrollViewSpacing(int height);

        int getDefaultHeaderHeight();

        int getScrollViewSpacing();

    }

    interface Presenter {

        void initialize();

    }
}
