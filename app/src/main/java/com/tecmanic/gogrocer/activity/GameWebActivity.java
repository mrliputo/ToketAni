package com.tecmanic.gogrocer.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import com.tecmanic.gogrocer.R;

public class GameWebActivity extends AppCompatActivity {

    WebSettings webSettings;
    private WebView webView;
    private ContentLoadingProgressBar progressId;
    private View bgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_web);
        webView = findViewById(R.id.web_view);
        bgBack = findViewById(R.id.bg_back);
        progressId = findViewById(R.id.progress_id);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        bgBack.setVisibility(View.VISIBLE);
        progressId.show();
        webView.loadUrl("https://www.gamezop.com/?id=e2b2jv9S2");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                bgBack.setVisibility(View.GONE);
                progressId.hide();
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                bgBack.setVisibility(View.GONE);
                progressId.hide();
                super.onReceivedError(view, request, error);
            }

        });

    }

    @Override
    public void onBackPressed() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        } else {
            new AlertDialog.Builder(GameWebActivity.this)
                    .setTitle("Game Alert!")
                    .setMessage("Are you sure want to exit from game.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, which) -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            finishAndRemoveTask();
                        } else {
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();

        }

    }
}