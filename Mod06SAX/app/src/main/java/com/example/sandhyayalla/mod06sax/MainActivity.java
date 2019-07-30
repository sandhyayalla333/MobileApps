package com.example.sandhyayalla.mod06sax;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.BufferedReader;
import  java.io.InputStreamReader;
import java.util.ArrayList;
import android.os.AsyncTask;
import android.view.View;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnxml).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetSimpleAsync().execute("http://api.theappsdr.com/xml/");
            }
        });
    }
    private class GetSimpleAsync extends AsyncTask<String, Void, ArrayList<Person>> {
        @Override
        protected ArrayList<Person> doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            ArrayList<Person> result = null;
            StringBuilder output=new StringBuilder();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                   /* reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line="";
                    while ((line=reader.readLine())!=null)
                    {
                        output.append(line);
                    }

                    Log.d("demo","output stream "+output.toString());*/
                   // PersonParser parser=new PersonParser();
                    //result=PersonParser.PersonSaxParser.ParsePersons(connection.getInputStream());
                   result=PersonParser.PersonPullParser.ParsePersons(connection.getInputStream());
                    //result=PersonParser.PersonPullParser.ParsePersons(output.toString());


                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } /*catch (SAXException e) {
                e.printStackTrace();
            }*/ finally {
                if (connection != null) {
                    connection.disconnect();
                }

            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Person> result) {
            if (result!=null) {
                Log.d("demo", result.toString());
                Log.d("demo","Size of result : "+result.size());
            } else {
                Log.d("demo", "null result");
            }
        }
    }


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

}
