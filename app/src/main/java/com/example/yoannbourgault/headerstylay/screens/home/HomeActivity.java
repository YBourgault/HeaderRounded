package com.example.yoannbourgault.headerstylay.screens.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.yoannbourgault.headerstylay.R;
import com.example.yoannbourgault.headerstylay.screens.detector.DetectorActivity;
import com.example.yoannbourgault.headerstylay.screens.tls.TLSActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    public void onClickBtnTls(View v) {
        startActivity(new Intent(this, TLSActivity.class));
    }

    public void onClickBtnDetector(View v) {
        startActivity(new Intent(this, DetectorActivity.class));
    }
}
