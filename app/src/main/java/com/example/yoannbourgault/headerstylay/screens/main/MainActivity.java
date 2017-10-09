package com.example.yoannbourgault.headerstylay.screens.main;

import android.os.Bundle;

import com.example.yoannbourgault.headerstylay.R;
import com.example.yoannbourgault.headerstylay.app.base.RoundedHeaderActivity;

public class MainActivity extends RoundedHeaderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected int getStatusBarColorResId() {
        return R.color.colorRedDark;
    }
}
