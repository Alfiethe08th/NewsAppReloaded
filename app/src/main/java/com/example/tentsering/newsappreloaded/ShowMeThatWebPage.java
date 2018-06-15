package com.example.tentsering.newsappreloaded;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ShowMeThatWebPage extends AppCompatActivity {
    public WebView showWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_me_that_web_page);

        showWeb = (WebView) findViewById(R.id.showWeb);
        Intent intent = getIntent();
        String thatUrl = intent.getStringExtra(RecyclerviewAdapter.EXTRA_MESSAGE);
        showWeb.loadUrl(thatUrl);
        showWeb.setWebViewClient(new MyOwnLittleBrowser());
        showWeb.getSettings().setJavaScriptEnabled(true);

    }

    private class MyOwnLittleBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }
}
