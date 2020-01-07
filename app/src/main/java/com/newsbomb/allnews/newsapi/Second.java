package com.newsbomb.allnews.newsapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.newsbomb.allnews.newsapi.Model.Articles;

public class Second extends AppCompatActivity {

    WebView webView;
    Toolbar mToolbar;


   LottieAnimationView mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Articles articles=new Articles();
        Intent intents=getIntent();
        String title=intents.getStringExtra("titles");
       // Toast.makeText(this, title.toString(), Toast.LENGTH_SHORT).show();

         mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(title.toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressBar=(LottieAnimationView) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);


        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        webView=(WebView) findViewById(R.id.webview);



     startWebView(url);


    }

    private void startWebView(String url) {

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mProgressBar.setVisibility(View.INVISIBLE);
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
               mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(Second.this, "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
        webView.loadUrl(url);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
           finish();
        }

        return super.onOptionsItemSelected(item);
    }



}
