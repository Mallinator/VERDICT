package com.example.administrator.verdict;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Web extends AppCompatActivity {

    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        web =findViewById(R.id.web);

        String s = getIntent().getExtras().getString("title");

        web.setWebViewClient(new WebViewClient());

        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.loadUrl(s);
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack())
        {
            web.goBack();
        }
        else {

            super.onBackPressed();
        }

    }
}
