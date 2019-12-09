package com.icls.offlinekyc.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.icls.offlinekyc.R;

public class UploadDock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_dock);
        WebView mywebview = (WebView) findViewById(R.id.webView);
        mywebview.getSettings().setLoadsImagesAutomatically(true);
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mywebview.loadUrl("https://developers.digitallocker.gov.in/public/oauth2/1/authorize?response_type=code&client_id=E2F3BD85&state=xyz&redirect_uri=www.myeid.co.in");
        WebSettings webSettings = mywebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }
}
