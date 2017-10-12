package com.example.yoannbourgault.headerstylay.screens.tls;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.yoannbourgault.headerstylay.R;
import com.example.yoannbourgault.headerstylay.app.base.RoundedHeaderActivity;
import com.example.yoannbourgault.headerstylay.app.base.SwipeToRefreshNestedScrollView;
import com.example.yoannbourgault.headerstylay.screens.header.CollapsingToolbar;

public class TLSActivity extends RoundedHeaderActivity implements
        CollapsingToolbar.OnCloseIconClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CollapsingToolbar toolbar = (CollapsingToolbar) findViewById(R.id.collapsing_toolbar);
        toolbar.setOnCloseIconListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_tls;
    }

    @Override
    protected int getStatusBarColorResId() {
        return R.color.colorRedDark;
    }

    @Override
    protected SwipeToRefreshNestedScrollView getScrollView() {
        return (SwipeToRefreshNestedScrollView) findViewById(R.id.nested_scroll_view);
    }

    @Override
    protected RelativeLayout getHeader() {
        return (RelativeLayout) findViewById(R.id.header_content);
    }

    @Override
    protected View getHeaderScrim() {
        return findViewById(R.id.header_scrim);
    }

    @Override
    protected FrameLayout getIconContainer() {
        return (FrameLayout) findViewById(R.id.header_icon_container);
    }

    @Override
    protected void refreshContent() {
        // TODO: 12/10/2017 Refresh data & UI
    }

    @Override
    public void onCloseIconClicked() {
        finish();
    }
}
