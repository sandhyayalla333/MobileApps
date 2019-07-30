//Homework 05
//Ashika Shivakote Annegowda, Nagasandhyadevi Yalla
//Main activity

package com.mad.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class MainActivity extends AppCompatActivity {

    String url = "https://newsapi.org/v2/sources?apiKey=9137e42870314465942b515187cd1a3b";
    ArrayList<Source> sourceResult = new ArrayList<Source>();
    AlertDialog.Builder builder;
    AlertDialog dialog;
    ListView listViewSources;
    final static String SOURCE_KEY = "SOURCE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Main Activity");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.darkGray));

        listViewSources = findViewById(R.id.listViewSources);

        listViewSources.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("demo", "Clicked item "+i);
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                intent.putExtra(SOURCE_KEY, sourceResult.get(i));
                startActivity(intent);
            }
        });

        if(isConnected()){
            builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = this.getLayoutInflater();
            builder.setTitle("Loading Sources").setView(inflater.inflate(R.layout.dialog_bar, null));
            dialog = builder.create();
            dialog.show();

            new DisplaySourcesAsync().execute(url);
        }else{
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }


    }

    //async task
    class DisplaySourcesAsync extends AsyncTask<String, Integer, ArrayList<Source>> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(ArrayList<Source> result) {
            if(result.size()>0){
                dialog.dismiss();
                displaySources(result);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected ArrayList<Source> doInBackground(String... params) {
            StringBuilder stringBuilder=new StringBuilder();
            BufferedReader reader=null;
            HttpURLConnection connection = null;
            ArrayList<Source> result = new ArrayList<Source>();

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
                    JSONArray sources = root.getJSONArray("sources");
                    for (int i=0;i<sources.length();i++) {
                        JSONObject sourcesJson = sources.getJSONObject(i);
                        Source source = new Source();
                        source.id = sourcesJson.getString("id");
                        source.name = sourcesJson.getString("name");

                        //adding extra delay to show loading
                        for(int m=0; m<1000; m++){
                            for (int n=0; n<6000;n++){
                                //do nothing
                            }
                        }

                        result.add(source);
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

    public void displaySources(ArrayList<Source> sources){
        sourceResult = sources;
        Log.d("demo", sourceResult.toString());
        //display result
        if(sourceResult.size()!=0){
            ArrayAdapter<Source> adapter = new ArrayAdapter<Source>(this, android.R.layout.simple_list_item_1, android.R.id.text1, sourceResult);
            listViewSources.setAdapter(adapter);
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
