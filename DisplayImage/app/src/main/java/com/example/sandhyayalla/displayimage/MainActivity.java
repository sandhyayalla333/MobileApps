//Assignment#: In Class Assignment 04
//MainActivity
//Ashika Shivakote Annegowda, Sandhyadevi Yalla

package com.example.sandhyayalla.displayimage;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity {
    Button btn_thread;
    Handler handler;
    ImageView myImageView;
    ProgressBar progressBar;

    String url = "https://cdn.pixabay.com/photo/2014/12/16/22/25/youth-570881_960_720.jpg";

ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Display Image");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.darkGrey));

        btn_thread=(Button)findViewById(R.id.btnThread);
        progressBar=(ProgressBar)findViewById(R.id.pbimageload);
        myImageView=(ImageView)findViewById(R.id.ivImage);

        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.what==loadimage.progress_status)
                {
                    ProgressBar pb_show=(ProgressBar)findViewById(R.id.pbimageload);
                    pb_show.setProgress(msg.getData().getInt(loadimage.progress_key));
                }
                else if(msg.what==loadimage.image_done)
                {
                    Bitmap bmp;

                    byte[] byteArray = msg.getData().getByteArray(loadimage.bitmap_key);
                    bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    myImageView =(ImageView) findViewById(R.id.ivImage);
                    myImageView.setImageBitmap(bmp);
                    //setting to zero for progressbar
                    ProgressBar pb_show=(ProgressBar)findViewById(R.id.pbimageload);
                    pb_show.setProgress(0);



                }

                return false;
            }
        });

        //btn click for thread
        executorService= Executors.newFixedThreadPool(2);
        btn_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                executorService.execute(new loadimage());

            }
        });

        //btn for async
        findViewById(R.id.btnAsync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DisplayImageAsync().execute(url);
            }
        });

    }

    //async task
    class DisplayImageAsync extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Bitmap image) {
            myImageView.setImageBitmap(image);
            //setting to zero for progressbar
            progressBar.setProgress(0);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap myBitmap = null;
            URL url = null;
            try {
                url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);

                for(int i = 0;i<100;i++){
                    for (int j=0; j<10000000;j++){

                    }
                    publishProgress(i);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return myBitmap;
        }
    }
    //async task end
    class loadimage implements  Runnable
    {
        final static String bitmap_key="bitmapkey";
        final static int image_done=100;
        final static int progress_status=200;
        final static String progress_key="Progress";
        @Override
        public void run() {

            try {
                URL url = new URL("https://cdn.pixabay.com/photo/2017/12/31/06/16/boats-3051610_960_720.jpg");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                Bundle bundle=new Bundle();
                //new code

                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                byte[] byteArray = bStream.toByteArray();
                bundle.putByteArray(bitmap_key,byteArray);


                for(int i=0;i<100;i++)
                {
                    for(int j=0;j<10000000;j++)
                    {

                    }

                    Message updatemessage=new Message();
                    Bundle bundle1=new Bundle();
                    updatemessage.what=progress_status;
                    bundle1.putInt(progress_key,(Integer)i);
                    updatemessage.setData(bundle1);
                    handler.sendMessage(updatemessage);
                }


                Message imagemessage=new Message();
                imagemessage.what=image_done;
                imagemessage.setData(bundle);
                handler.sendMessage(imagemessage);



               // return myBitmap;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

}
