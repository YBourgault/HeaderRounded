package com.example.yoannbourgault.headerstylay.screens.header;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.example.yoannbourgault.headerstylay.R;

public class CollapsingToolbar extends Toolbar {

    @SuppressWarnings("unused")
    private final Context mContext;

    public CollapsingToolbar(Context context) {
        super(context);
        mContext = context;
        initialize();
    }

    public CollapsingToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initialize();
    }

    public CollapsingToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initialize();
    }

    private void initialize() {
        inflate(mContext, R.layout.collapsing_toolbar, this);
    }
}
