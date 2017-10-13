package com.example.yoannbourgault.headerstylay.screens.tls.toolbar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.yoannbourgault.headerstylay.R;


public class CollapseHeaderView extends Toolbar implements CollapseHeaderContract.View {

    private static final int DEFAULT_HEIGHT = R.dimen.collapse_header_default_height;

    @SuppressWarnings("FieldCanBeLocal")
    private CollapseHeaderContract.Presenter mPresenter;

    private ImageView mIcon;
    private FrameLayout mContainer;

    private int mDefaultContainerHeight = 0;

    public CollapseHeaderView(Context context) {
        super(context);
        initialize();
    }

    public CollapseHeaderView(Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CollapseHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        View v = inflate(getContext(), R.layout.collapse_header, this);
        setContentInsetsAbsolute(0, 0);
        mContainer = v.findViewById(R.id.collapse_header_container);
        mIcon = v.findViewById(R.id.collapse_header_icon);
        mDefaultContainerHeight = getResources().getDimensionPixelOffset(DEFAULT_HEIGHT);
        mPresenter = new CollapseHeaderPresenter(this);
    }

    public void changeHeight(int height) {
        if (height < mDefaultContainerHeight) {
            height = mDefaultContainerHeight;
        }
        ViewGroup.LayoutParams params = mContainer.getLayoutParams();
        params.height = height;
        mContainer.setLayoutParams(params);
        // TODO: 13/10/2017 Move icon too
    }

}
