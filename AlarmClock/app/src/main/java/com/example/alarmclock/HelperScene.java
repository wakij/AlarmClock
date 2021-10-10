package com.example.alarmclock;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

public class HelperScene extends Fragment {

    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        ViewGroup rootView= (ViewGroup) inflater.inflate(R.layout.webshow, container, false);

        webView = rootView.findViewById(R.id.webview);
        webView.setWebViewClient((new WebViewClient()));
        webView.loadUrl("https://www.android.com");
        return rootView;
    }
}
