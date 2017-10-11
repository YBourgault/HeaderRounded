package com.example.yoannbourgault.headerstylay.screens.header;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.example.yoannbourgault.headerstylay.R;

public class CollapsingToolbar extends Toolbar {

    private final Context mContext;
    private OnCloseIconClickedListener mCloseIconListener;

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
        View v = inflate(mContext, R.layout.collapsing_toolbar, this);
        v.findViewById(R.id.close_icon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCloseIconListener != null) {
                    mCloseIconListener.onCloseIconClicked();
                }
            }
        });

    }

    public void setOnCloseIconListener(OnCloseIconClickedListener listener) {
        mCloseIconListener = listener;
    }

    public interface OnCloseIconClickedListener{
        void onCloseIconClicked();
    }
}
