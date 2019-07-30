package com.example.sandhyayalla.photogallery;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;

//import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetKeywordsAsync extends AsyncTask<String,Integer,CharSequence[]> {
    MainActivity mainActivity;
    public GetKeywordsAsync (MainActivity mainActivity)
    {
        this.mainActivity=mainActivity;
    }
    @Override
    protected CharSequence[] doInBackground(String... strings)  {

        HttpURLConnection connection=null;
        String output=null;
        BufferedReader bufferedReader=null;
        StringBuilder stringBuilder=new StringBuilder();
        CharSequence[] items=null;
        for (int i=0;i<115;i++)
        {
            for(int j=0;j<1000000;j++)
            {

            }
            publishProgress(i);
        }
        try
        {
            URL url=new URL(strings[0]);
            connection=(HttpURLConnection)url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                //output= IOUtils.toString(connection.getInputStream(),"UTF-8");
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(connection!=null)
            {
                connection.disconnect();

            }
            if(bufferedReader!=null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        output=stringBuilder.toString();
        items=output.split(";");
        Log.d("demo",output.toString());

        return items;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //super.onProgressUpdate(values);
        mainActivity.progressDialog.show();
        mainActivity.progressDialog.setMessage("Loading Dictionary");
        mainActivity.progressDialog.setProgress(values[0]);

    }

    @Override
    protected void onPostExecute(CharSequence[] s) {

       mainActivity.progressDialog.dismiss();
        mainActivity.Handlestrings(s);

    }
}
