package com.example.sandhyayalla.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetDownloadimageAsyc extends AsyncTask<String,Integer,Bitmap> {
    MainActivity mainActivity;
    public GetDownloadimageAsyc(MainActivity mainActivity)
    {
        this.mainActivity=mainActivity;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap=null;
        HttpURLConnection connection=null;
        BufferedReader reader;
        for (int i=0;i<100;i++)
        {
            for(int j=0;j<1000000;j++)
            {

            }
            publishProgress(i);
        }

        try {
            URL url = new URL(strings[0]);
            connection=(HttpURLConnection)url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(connection!=null)
                connection.disconnect();
        }
        return  bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        mainActivity.progressDialog.dismiss();
        ImageView iv_display=(ImageView)mainActivity.findViewById(R.id.ivDisplay);
        iv_display.setImageBitmap(bitmap);

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        mainActivity.progressDialog.show();
        mainActivity.progressDialog.setMessage("Loading photo..");
       mainActivity.progressDialog.setProgressStyle(values[0]);
    }
}
