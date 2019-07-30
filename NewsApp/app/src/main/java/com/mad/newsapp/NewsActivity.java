package com.mad.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

//import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    Source source = new Source();
    String url;
    ArrayList<News> articleResult = new ArrayList<News>();
    AlertDialog.Builder builder;
    AlertDialog dialog;
    ListView listViewArticles;
    final static String ARTICLE_KEY = "ARTICLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.darkGray));

        listViewArticles = findViewById(R.id.listViewArticles);

        if(getIntent() != null && getIntent().getExtras() != null){
            source = (Source) getIntent().getSerializableExtra(MainActivity.SOURCE_KEY);

            setTitle(source.name);
            url = "https://newsapi.org/v2/top-headlines?sources="+source.id+"&apiKey=9137e42870314465942b515187cd1a3b";

            listViewArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("demo", "Clicked item "+i);
                    Intent intent = new Intent(NewsActivity.this, WebViewActivity.class);
                    intent.putExtra(ARTICLE_KEY, articleResult.get(i));
                    startActivity(intent);
                }
            });

            if(isConnected()){
                builder = new AlertDialog.Builder(NewsActivity.this);
                LayoutInflater inflater = this.getLayoutInflater();
                builder.setTitle("Loading Stories").setView(inflater.inflate(R.layout.dialog_bar, null));
                dialog = builder.create();
                dialog.show();

                new DisplayArticlesAsync().execute(url);
            }else{
                Toast.makeText(NewsActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //async task
    class DisplayArticlesAsync extends AsyncTask<String, Integer, ArrayList<News>> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(ArrayList<News> result) {
            if(result.size()>0){
                dialog.dismiss();
                displayResults(result);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected ArrayList<News> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<News> result = new ArrayList<News>();
            StringBuilder stringBuilder=new StringBuilder();
            BufferedReader reader=null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    String json=stringBuilder.toString();

                    JSONObject root = new JSONObject(json);
                    JSONArray articles = root.getJSONArray("articles");
                    for (int i=0;i<articles.length();i++) {
                        JSONObject articlesJson = articles.getJSONObject(i);
                        News article = new News();
                        article.author = articlesJson.getString("author");
                        article.title = articlesJson.getString("title");
                        article.url = articlesJson.getString("url");
                        article.urlToImage = articlesJson.getString("urlToImage");
                        article.publishedAt = articlesJson.getString("publishedAt");

                        //adding extra delay to show loading
                        for(int m=0; m<10000; m++){
                            for (int n=0; n<7000;n++){
                                //do nothing
                            }
                        }

                        result.add(article);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return result;
        }

    }

    public void displayResults(ArrayList<News> articles){
        articleResult = articles;
        Log.d("demo", articleResult.toString());
        //display result
        if(articleResult.size()!=0){
            NewsAdapter adapter = new NewsAdapter(this, R.layout.news_item, articleResult);
            listViewArticles.setAdapter(adapter);
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
