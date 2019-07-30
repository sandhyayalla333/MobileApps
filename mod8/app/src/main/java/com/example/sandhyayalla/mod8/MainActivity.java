package com.example.sandhyayalla.mod8;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_list);
        listViewSources=(ListView)findViewById(R.id.listViewSources);

       //mod 8
        if(isConnected())
        {
            new DisplaySourcesAsync().execute(url);
            /*OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //Log.d("demo","Response from async"+response.body().string());
                    ArrayList<Source> result=new ArrayList<Source>();

                    try {


                        String json=response.body().string();
                        Log.d("demo", "json "+json);
                        JSONObject root = new JSONObject(json);
                        JSONArray sources = root.getJSONArray("sources");
                        for (int i=0;i<sources.length();i++) {
                            JSONObject sourcesJson = sources.getJSONObject(i);
                            Source source = new Source();
                            source.id = sourcesJson.getString("id");
                            source.name = sourcesJson.getString("name");



                            sourceResult.add(source);
                        }
                        Log.d("demo","sourceresult" + sourceResult);





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                  //  sourceResult=result;
                   // Log.d("demo","sourceresult"+sourceResult.toString());


                }
            });*/


        }

        ArrayList<Source> finalresult=new ArrayList<>();
        finalresult=sourceResult;
        Log.d("demo","final "+finalresult);




        listViewSources.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              // Intent intent=new Intent(MainActivity.this,NewsActivity.class);
               // startActivity(intent);

            }
        });

    }
    class DisplaySourcesAsync extends AsyncTask<String, Integer, ArrayList<Source>> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(ArrayList<Source> result) {
            if(result.size()>0){
               // dialog.dismiss();
                displaySources(result);
                callrecyclerview(result);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected ArrayList<Source> doInBackground(String... params) {
            StringBuilder stringBuilder=new StringBuilder();
            String json=null;
            BufferedReader reader=null;
            HttpURLConnection connection = null;
            ArrayList<Source> result = new ArrayList<Source>();
            OkHttpClient okHttpClient=new OkHttpClient();
            Request request=new Request.Builder().url(url).build();
            try {
                Response response=okHttpClient.newCall(request).execute();
                 json=response.body().string();
                 Log.d("demo","http output "+ json);

            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject root = null;
            try {
                root = new JSONObject(json);
                JSONArray sources = root.getJSONArray("sources");
                for (int i=0;i<sources.length();i++) {
                    JSONObject sourcesJson = sources.getJSONObject(i);
                    Source source = new Source();
                    source.id = sourcesJson.getString("id");
                    source.name = sourcesJson.getString("name");



                    result.add(source);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }





            return result;
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

    public void displaySources(ArrayList<Source> sources)
    {
        sourceResult=sources;
        //ArrayAdapter<Source> adapter=new ArrayAdapter<Source>(this,android.R.layout.simple_list_item_1,android.R.id.text1,sourceResult);
        SourceAdapter adapter1=new SourceAdapter(this,R.layout.source_item,sourceResult);
        listViewSources.setAdapter(adapter1);
        //listViewSources.setAdapter(adapter1);
    }
    public void callrecyclerview(ArrayList<Source> sources)
    {
        Intent intent=new Intent(MainActivity.this,NewsActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("sources",sources);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }


}
