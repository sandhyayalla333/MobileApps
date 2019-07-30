package com.example.sandhyayalla.photogallery;
//Main Activity
//Naga Sandhyadevi Yalla,Ashika
//Group28
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView tv_keyword;

    ImageView iv_prev;
    ImageView iv_next;
    AlertDialog.Builder builder;
    ProgressBar progressBarDict;
    int index=0;
    int urlcount=0;
    String[] imageurls=null;
    CharSequence[] items=null;
    ProgressDialog progressDialog;
    String errortext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_keyword = findViewById(R.id.tvKeyword);
        iv_prev = (ImageView) findViewById(R.id.ivprev);
        iv_next = (ImageView) findViewById(R.id.ivnext);
        //iv_next.setClickable(false);
        //iv_prev.setClickable(false);
        iv_prev.setEnabled(false);
        iv_next.setEnabled(false);

        progressBarDict = (ProgressBar) findViewById(R.id.pbDictionary);
        progressBarDict.setVisibility(ProgressBar.INVISIBLE);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDialog.setMessage("Loading photo");
        errortext = getResources().getString(R.string.interneterror);

        if (isConnected()) {
            new GetKeywordsAsync(MainActivity.this).execute("http://dev.theappsdr.com/apis/photos/keywords.php");
        }
        else {
            Toast.makeText(this, errortext, Toast.LENGTH_SHORT).show();
        }

        // Go onclick to get the alert
        findViewById(R.id.btnGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(builder!=null) {
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "No keywords", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // next image view onclick
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    Log.d("demo", "Connected" + isConnected() + "");
                    index = index + 1;
                    if (index >= urlcount) {
                        index = 0;

                    }
                    if (index >= 0 && index < urlcount) {
                        new GetDownloadimageAsyc(MainActivity.this).execute(imageurls[index]);

                    }
                } else {
                    Toast.makeText(MainActivity.this, errortext, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //prev image view on click
        iv_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {

                    if (index == 0) {
                        index = urlcount - 1;
                    } else {
                        index = index - 1;
                    }
                    if (index >= 0 && index < urlcount) {
                        new GetDownloadimageAsyc(MainActivity.this).execute(imageurls[index]);
                    }
                } else {

                    Toast.makeText(MainActivity.this, errortext, Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    //for checking internet connection
    private boolean isConnected()
    {
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null || !networkInfo.isConnected())
        {
            return false;
        }

            return  true;


    }
    //display keywords in alert
    public void Handlestrings(CharSequence[] data)
    {
        //ArrayList<CharSequence> items=new ArrayList<CharSequence>();
        //final CharSequence[] items=data.split(";");
            items=data;
         builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose keyword");
        builder.setCancelable(false);
        builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(isConnected()) {
                        String keyword=items[which].toString();
                        tv_keyword.setText(keyword);
                        String dicturl="http://dev.theappsdr.com/apis/photos/index.php?keyword="+keyword;
                        //calling another api to get urls for selected keyword

                            new GetImageurlsAsync(MainActivity.this).execute(dicturl);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, errortext, Toast.LENGTH_SHORT).show();
                        }
                    }
                });






    }
    public void Handleurls(ArrayList<String> arrayList)
    {
         imageurls=arrayList.toArray(new String[arrayList.size()]);
         if(imageurls.length!=0) {
             if(isConnected()) {
                 new GetDownloadimageAsyc(MainActivity.this).execute(imageurls[0]);
                 urlcount = arrayList.size();
                 if(imageurls.length==1)
                 {
                     iv_next.setEnabled(false);
                     iv_prev.setEnabled(false);
                 }
                 else {
                     iv_next.setEnabled(true);
                     iv_prev.setEnabled(true);
                 }
             }
             else
             {
                 Toast.makeText(MainActivity.this, errortext, Toast.LENGTH_SHORT).show();
             }


         }

         else
         {

             ImageView imageView=(ImageView)findViewById(R.id.ivDisplay);

            imageView.setImageResource(R.drawable.ic_launcher_background);
             iv_prev.setEnabled(false);
             iv_next.setEnabled(false);
             String imageerror=getResources().getString(R.string.imageerror);
             Toast.makeText(MainActivity.this, imageerror, Toast.LENGTH_SHORT).show();
         }
        //iv_prev.setClickable(true);
        //iv_next.setClickable(true);

    }
}
