package com.tecmanic.toketani.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tecmanic.toketani.R;

/**
 * A simple {@link Fragment} subclass..
 */
public class BisnisFragment extends Fragment {

    public WebView mWebView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_bisnis, container, false);
        mWebView = (WebView) v.findViewById(R.id.webView);
        mWebView.loadUrl("file:///android_asset/index.html");

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        return v;
    }
}