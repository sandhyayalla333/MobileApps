package com.example.sandhyayalla.chatroom;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageThreads extends AppCompatActivity {

    TextView tv_displayname;
    EditText et_addtthread;
    ImageView iv_Addthread;
    ImageView iv_logout;
    String token=null;
    String threadurl="http://ec2-18-234-222-229.compute-1.amazonaws.com/api/thread";
    String addthreadurl="http://ec2-18-234-222-229.compute-1.amazonaws.com/api/thread/add";
    String deletethreadurl="http://ec2-18-234-222-229.compute-1.amazonaws.com/api/thread/delete/";
    String userid=null;
    ListView listthreads;
    ArrayList<Threaditem> resultthreads=new ArrayList<Threaditem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_threads);
        setTitle("Message Threads");
        final SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        listthreads=(ListView)findViewById(R.id.listviewThreads);
        et_addtthread=(EditText)findViewById(R.id.etaddThread) ;

        iv_logout=(ImageView)findViewById(R.id.ivLogout);
       // iv_Add=(ImageView)findViewById(R.id.ivAdd) ;
        iv_Addthread=(ImageView)findViewById(R.id.ivaddthread);
        iv_Addthread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!et_addtthread.getText().toString().isEmpty()) {
                    String threadtitle = et_addtthread.getText().toString();
                    Log.d("demo", "clicked " + threadtitle);
                    Addnewthread(threadtitle);
                    et_addtthread.setText("");
                }
                else
                {
                    Toast.makeText(MessageThreads.this, "Enter new thread", Toast.LENGTH_SHORT).show();
                }
            }
        });
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                preferences.edit().remove("token").commit();
                String token=preferences.getString("token","");
                Log.d("demo","tokenafterlogout :"+token);
                Intent loginintent=new Intent(MessageThreads.this,MainActivity.class);

                startActivity(loginintent);
                finish();
            }
        });
        if(getIntent()!=null && getIntent().getExtras()!=null)
        {
            Intent intent = getIntent();
            UserAuthentication user = (UserAuthentication) intent.getExtras().getSerializable("authenticationdetails");
            token = preferences.getString("token","");
           // preferences.edit().putString("userid",user.userId.toString() ).commit();

            Log.d("demo","token"+token);
            userid=user.userId;
            tv_displayname=(TextView) findViewById(R.id.tvDisplayname);
            if(user.userFname!=null)
            tv_displayname.setText(user.userFname.toString()+ " "+user.userLname.toString());
            if(isConnected())
            {
                new LoadThreads().execute(threadurl);

            }

            //callMessagethreads();
           // displaySources();




        }

    }
    //asynctask

    public class LoadThreads extends AsyncTask<String,Integer,ArrayList<Threaditem>>
    {
        @Override
        protected void onPostExecute(ArrayList<Threaditem> threaditems) {
            //super.onPostExecute(threaditems);
            resultthreads=threaditems;
            ThreadAdapter adapter = new ThreadAdapter(MessageThreads.this, R.layout.activity_threaditem, resultthreads);
            listthreads.setAdapter(adapter);
        }

        @Override
        protected ArrayList<Threaditem> doInBackground(String... strings) {
            ArrayList<Threaditem> result=new ArrayList<>();
            OkHttpClient threadclient=new OkHttpClient();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .header("Authorization", "BEARER "+token)
                    .build();
            try {
                Response response=threadclient.newCall(request).execute();
                if(response.isSuccessful())
                {
                    String json = response.body().string();
                    JSONObject root = null;
                    try {
                        root = new JSONObject(json);

                        JSONArray threads = root.getJSONArray("threads");
                        for (int i = 0; i < threads.length(); i++) {
                            JSONObject sourcesJson = threads.getJSONObject(i);
                            Threaditem threaditem = new Threaditem();

                            threaditem.userid = sourcesJson.getString("user_id");

                            threaditem.title = sourcesJson.getString("title");
                            threaditem.threadid=sourcesJson.getString("id");



                            result.add(threaditem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                   // Toast.makeText(MessageThreads.this, "Error from AApi"+response.message().toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;

        }
    }
    //end asynctask for add

    //async task for delete
    public class DeleteThread extends AsyncTask<String,Integer,Threaditem>
    {
        @Override
        protected void onPostExecute(Threaditem threaditems) {
            super.onPostExecute(threaditems);

        }

        @Override
        protected Threaditem doInBackground(String... strings) {
            Threaditem result=new Threaditem();
            OkHttpClient threadclient=new OkHttpClient();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .header("Authorization", "BEARER "+token)
                    .build();
            try {
                Response response=threadclient.newCall(request).execute();
                if(response.isSuccessful())
                {
                    String json = response.body().string();
                    JSONObject root = null;
                    try {
                        root = new JSONObject(json);
                        JSONObject obj=root.getJSONObject("thread");
                        result.threadid=obj.getString("id");
                        result.title=obj.getString("title");
                        result.userid=obj.getString("user_id");



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    // Toast.makeText(MessageThreads.this, "Error from AApi"+response.message().toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;

        }
    }





    //adapter
    public class ThreadAdapter extends ArrayAdapter<Threaditem>
    {
        public ThreadAdapter(@NonNull Context context, int resource, @NonNull List<Threaditem> objects) {
            super(context, resource, objects);
        }
        //Threaditem threaditem=new Threaditem();

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Threaditem threaditem=getItem(position);
            if(convertView==null)
            {
                convertView= LayoutInflater.from(getContext()).inflate(R.layout.activity_threaditem,parent,false);
            }
            TextView tv_id=(TextView)convertView.findViewById(R.id.tvTitle);
            if(threaditem.title.toString()!=null)
            tv_id.setText(threaditem.title.toString());
           ImageView iv_delete=(ImageView)convertView.findViewById(R.id.ivdelete);
            iv_delete.setVisibility(View.INVISIBLE);
           if(threaditem.userid.toString().equals(userid))
           {
              // Log.d("demo","thread and user"+threaditem.userid.toString()+" "+userid);
               iv_delete.setVisibility(View.VISIBLE);
           }
           iv_delete.setTag(position);
           iv_delete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    Log.d("demo","deleteclicked");
                       int i=(int) view.getTag();
                        Threaditem item=getItem(i);
                      new DeleteThread().execute(deletethreadurl+item.threadid.toString());
                      Log.d("demo","threadid"+item.threadid.toString());
                      new LoadThreads().execute(threadurl);


               }
           });



            //return super.getView(position, convertView, parent);
            return convertView;
        }

    }
    //display threads in list view


    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }else
            return true;
    }
    // Addnewthread
    private void  Addnewthread(String threadtitle)
    {
        if(token!=null)
        {
            OkHttpClient addclient=new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("title",threadtitle).build();
            Request request = new Request.Builder()
                    .url(addthreadurl)
                    .header("Authorization", "BEARER "+token)
                    .post(formBody)
                    .build();

            addclient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    if(response.isSuccessful())
                    {
                       // Log.d("demo","added");
                        new LoadThreads().execute(threadurl);
                    }
                }
            });

        }

    }
    //end add new thread

}
