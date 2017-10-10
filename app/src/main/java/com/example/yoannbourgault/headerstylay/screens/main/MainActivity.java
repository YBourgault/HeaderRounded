package com.example.yoannbourgault.headerstylay.screens.main;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.yoannbourgault.headerstylay.R;
import com.example.yoannbourgault.headerstylay.app.base.RoundedHeaderActivity;

public class MainActivity extends RoundedHeaderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getStatusBarColorResId() {
        return R.color.colorRedDark;
    }

    @Override
    protected ImageView getHeaderIcon() {
        return (ImageView) findViewById(R.id.header_icon);
    }

    @Override
    protected NestedScrollView getScrollView() {
        return (NestedScrollView) findViewById(R.id.nested_scroll_view);
    }

    @Override
    protected RelativeLayout getHeaderContainer() {
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
}
