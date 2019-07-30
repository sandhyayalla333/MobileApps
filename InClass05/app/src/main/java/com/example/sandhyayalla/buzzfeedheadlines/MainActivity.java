package com.example.sandhyayalla.buzzfeedheadlines;
//Sandhyadevi Yalla -801097837
//Mainactivity

import android.content.Context;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
        TextView tv_title;
        TextView tv_publishedat;
        TextView tv_Description;
        ImageView iv_image;
        ImageView iv_next;
        ImageView iv_prev;
        Button btnclose;
        int index=0;
        int articlecount=0;
        String errortext="";
        ArrayList<Article> articles=null;
        AlertDialog.Builder builder;

        AlertDialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("BuzzFeed HeadLines");




        tv_title=(TextView)findViewById(R.id.tvheadline);
        tv_publishedat=(TextView)findViewById(R.id.tvpublisheddate);
        tv_Description=(TextView)findViewById(R.id.tvdescriptiontext);
        iv_image=(ImageView)findViewById(R.id.ivimage);
        iv_next=(ImageView)findViewById(R.id.ivnext);
        iv_prev=(ImageView)findViewById(R.id.ivprev);
        btnclose=(Button)findViewById(R.id.btnCLose);
        errortext=getResources().getString(R.string.errortext);
        if(isConnected())
        {
            builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();

            builder.setTitle("Loading News").setView(inflater.inflate(R.layout.dialog_bar, null));

            dialog = builder.create();
            dialog.show();


            new GetDataAsync().execute("https://newsapi.org/v2/top-headlines?sources=buzzfeed&apiKey=6e915fa39ec04e34850e0e6f85ff977b");
        }
        else
        {
            Toast.makeText(this, errortext, Toast.LENGTH_SHORT).show();
        }
        //btn quit func

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

   //Prev click functionality
        iv_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstitem=getResources().getString(R.string.firstitem);
                if(index==0 && articlecount!=0)
                {
                    Toast.makeText(MainActivity.this, firstitem, Toast.LENGTH_SHORT).show();
                    //index=articlecount-1;
                }
                else
                {
                    index=index-1;
                    if(index>=0 && index <articlecount)
                    {

                        Article article=new Article();
                        // List<Article> results=articles;
                        article=articles.get(index);
                        articlecount=articles.size();
                        tv_title.setText(article.title.toString());
                        tv_Description.setText(article.description.toString());
                        String[] out=article.publishedAt.split("T");
                        tv_publishedat.setText(out[0]);
                        Picasso.get().load(article.urlToImage).into(iv_image);
                    }
                }



            }
        });

    //Next click functionality
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastitem=getResources().getString(R.string.lastitem);

                    if(index>=articlecount && articlecount!=0)
                    {

                        Toast.makeText(MainActivity.this, lastitem, Toast.LENGTH_SHORT).show();
                    }

                    else {
                        index = index + 1;
                        if (index >= 0 && index < articlecount) {

                            Article article = new Article();
                            // List<Article> results=articles;
                            article = articles.get(index);
                            articlecount = articles.size();
                            tv_title.setText(article.title.toString());
                            tv_Description.setText(article.description.toString());

                            String[] out = article.publishedAt.split("T");
                            tv_publishedat.setText(out[0]);
                            Picasso.get().load(article.urlToImage).into(iv_image);
                        }
                    }
            }
        });
    }
    //isconnected
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }
    //async task

    private class GetDataAsync extends AsyncTask<String, Void, ArrayList<Article>> {

        @Override
        protected ArrayList<Article> doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;


            ArrayList<Article> result = new ArrayList<Article>();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    String json=stringBuilder.toString();
                    JSONObject jsonObject= new JSONObject(json);
                    JSONArray articles=jsonObject.getJSONArray("articles");
                    for(int i=0;i<articles.length();i++)
                    {
                        JSONObject articleobj=articles.getJSONObject(i);
                        Article article=new Article();
                        article.title=articleobj.getString("title");
                        article.description=articleobj.getString("description");
                        article.urlToImage=articleobj.getString("urlToImage");
                        article.publishedAt=articleobj.getString("publishedAt");
                       // person.name=personobj.getString("name");
                        //description,urlToImage,publishedAt

                        result.add(article);
                    }




                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;

        }

        @Override
        protected void onPostExecute(ArrayList<Article> result) {
            if (result.size()>0) {
                Log.d("demo",result.size()+" Count");
                Log.d("demo", result.toString());
            } else {
                Log.d("demo", "null result");
            }
            dialog.dismiss();

            Displayarticles(result);

        }
    }
    //Display article

    public void Displayarticles(ArrayList<Article> data)
    {
        Article article=new Article();
            //dialog.dismiss();
            articles=data;
        articlecount=articles.size();
        if(articlecount!=0) {
            article = articles.get(0);
            index = 0;

            tv_title.setText(article.title.toString());
            tv_Description.setText(article.description.toString());
            String[] out = article.publishedAt.split("T");
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            String dateresult = "";


            //tv_publishedat.setText(dateresult.toString());
            tv_publishedat.setText(out[0].toString());
            if (article.urlToImage != null) {
                Picasso.get().load(article.urlToImage).into(iv_image);
            }
        }


    }


}
