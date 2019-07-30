package com.example.sandhyayalla.practicemodule6;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;
import java.io.BufferedReader;
import  java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnjson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected())
                {
                    Toast.makeText(MainActivity.this, "NetworkPresent", Toast.LENGTH_SHORT).show();
                    new GetDataAsync().execute("http://api.theappsdr.com/json/");

                }
                else
                {
                    Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

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
    private class GetDataAsync extends AsyncTask<String, Void, ArrayList<Person>> {
        @Override
        protected ArrayList<Person> doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            ArrayList<Person> result = new ArrayList<Person>();
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
                    JSONArray persons=jsonObject.getJSONArray("persons");
                    for(int i=0;i<persons.length();i++)
                    {
                        JSONObject personobj=persons.getJSONObject(i);
                        Person person=new Person();
                        person.name=personobj.getString("name");
                        person.id=personobj.getLong("id");
                        person.age=personobj.getInt("age");
                        Address address=new Address();
                        JSONObject addressobj=personobj.getJSONObject("address");
                        address.line1=addressobj.getString("line1");
                        address.state=addressobj.getString("state");
                        address.city=addressobj.getString("city");
                        address.zip=addressobj.getString("zip");
                        person.address=address;
                        result.add(person);
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
        protected void onPostExecute(ArrayList<Person> result) {
            if (result.size()>0) {
                Log.d("demo",result.size()+" Count");
                Log.d("demo", result.toString());
            } else {
                Log.d("demo", "null result");
            }
        }
    }



}
