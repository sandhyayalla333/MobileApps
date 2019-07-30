package com.mad.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        setTitle("Web View");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.darkGray));

        if(getIntent() != null && getIntent().getExtras() != null){
            News article = (News) getIntent().getSerializableExtra(NewsActivity.ARTICLE_KEY);

            if(isConnected()){
                WebView webView = findViewById(R.id.webView);
                webView.loadUrl(article.url);
            }else{
                Toast.makeText(WebViewActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }else
            return true;
    }
}
