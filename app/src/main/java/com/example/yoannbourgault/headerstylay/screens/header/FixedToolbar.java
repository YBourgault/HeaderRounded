package com.example.yoannbourgault.headerstylay.screens.header;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yoannbourgault.headerstylay.R;

public class FixedToolbar extends Toolbar {

    private final Context mContext;
    private ImageView mIcon;
    private TextView mBubbleTitle;
    private TextView mPageTitle;
    private OnClickBackArrowListener mBackArrowListener;

    public FixedToolbar(Context context) {
        super(context);
        mContext = context;
        initialize();
    }

    public FixedToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initialize();
    }

    public FixedToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initialize();
    }

    private void initialize() {
        View v = inflate(mContext, R.layout.fixed_toolbar, this);
        mPageTitle = v.findViewById(R.id.toolbar_title);
        mIcon = v.findViewById(R.id.icon);
        mBubbleTitle = v.findViewById(R.id.title);
        (v.findViewById(R.id.back_arrow)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBackArrowListener != null) {
                    mBackArrowListener.onClickBackArrow();
                }
            }
        });
    }

    public void setIcon(@DrawableRes int iconId) {
        mIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), iconId));
    }

    public void setPageTitle(String title) {
        mPageTitle.setText(title);
    }

    public void setBubbleTitle(String title) {
        mBubbleTitle.setText(title);
    }

    public void setBackArrowClickListener(OnClickBackArrowListener listener) {
        mBackArrowListener = listener;
    }

    public interface OnClickBackArrowListener {
        void onClickBackArrow();
    }

}
