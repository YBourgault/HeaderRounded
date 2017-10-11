package com.example.yoannbourgault.headerstylay.screens.detector;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.yoannbourgault.headerstylay.R;
import com.example.yoannbourgault.headerstylay.screens.header.FixedToolbar;

public class DetectorActivity extends AppCompatActivity implements
        FixedToolbar.OnClickBackArrowListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detector);

        changeStatusBarColor(R.color.colorRedDark);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        FixedToolbar toolbar = (FixedToolbar) findViewById(R.id.fixed_toolbar);
        toolbar.setPageTitle("Porte d'entrée");
        toolbar.setIcon(R.drawable.ic_home_black_24dp);
        toolbar.setBubbleTitle("Détecteur Inférieur");
        toolbar.setBackArrowClickListener(this);
    }

    protected void changeStatusBarColor(int colorResId) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(this, colorResId));
        }
    }

    @Override
    public void onClickBackArrow() {
        finish();
    }
}
