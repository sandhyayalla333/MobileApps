package com.example.sandhyayalla.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.util.Log;

public class GetImageurlsAsync extends AsyncTask<String,Integer,ArrayList<String>> {
    MainActivity mainActivity;
    public GetImageurlsAsync(MainActivity mainActivity)
    {
        this.mainActivity=mainActivity;

    }
   // ProgressBar pb;
    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        Bitmap bitmap=null;
        HttpURLConnection connection=null;
        ArrayList<String> list=new ArrayList<String>();
        BufferedReader reader;
        for (int i=0;i<100;i++)
        {
            for(int j=0;j<1000000;j++)
            {

            }
            publishProgress(i);
        }
        URL url= null;
        try {
            url = new URL(strings[0]);
            connection=(HttpURLConnection)url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // bitmap=BitmapFactory.decodeStream(connection.getInputStream());
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
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
        Log.d("demo",list.size()+"");

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        //pb=(ProgressBar) mainActivity.findViewById(R.id.pbDictionary);
       // pb.setVisibility(ProgressBar.INVISIBLE);

        mainActivity.progressDialog.dismiss();



        mainActivity.Handleurls(arrayList);


    }

    @Override
    protected void onProgressUpdate(Integer... values) {

     //pb=(ProgressBar) mainActivity.findViewById(R.id.pbDictionary);
       //pb.setVisibility(ProgressBar.VISIBLE);
        mainActivity.progressDialog.show();
        mainActivity.progressDialog.setMessage("Loading Dictionary");
        mainActivity.progressDialog.setProgress(values[0]);



    }
}
