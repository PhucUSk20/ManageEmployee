package com.hcmus.manage_employee.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.hcmus.manage_employee.R;

public class WebviewActivity extends AppCompatActivity {

    private WebView webView;
    private static final String PREF_IS_SIGNED_IN = "is_signed_in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = findViewById(R.id.webView);
        configureWebView();

        String url = getIntent().getStringExtra("url");

        if (requiresGoogleSignIn(url) && !isSignedIn()) {
            signInWithGoogle();
            return;
        }

        loadUrl(url);
    }

    private boolean requiresGoogleSignIn(String url) {
        return url.contains("https://accounts.google.com/");
    }

    private boolean isSignedIn() {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return preferences.getBoolean(PREF_IS_SIGNED_IN, false);
    }

    private void configureWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
    }

    private void loadUrl(String url) {
        webView.loadUrl(url);
    }

    private void signInWithGoogle() {

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }
}
