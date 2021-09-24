package com.example.alarmclock;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class WebShow extends AppCompatActivity {



    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webshow);





        webView=(WebView)findViewById(R.id.webview);
        webView.setWebViewClient((new WebViewClient()));
        webView.loadUrl("https://www.android.com");
    }
}