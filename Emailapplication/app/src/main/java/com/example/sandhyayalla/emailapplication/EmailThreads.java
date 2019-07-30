package com.example.sandhyayalla.emailapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EmailThreads extends AppCompatActivity {

    TextView tv_displayname;
    //EditText et_addtthread;
    ImageView iv_newemail;
    ImageView iv_logout;
    String token = null;
    String threadurl = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox";

    private  RecyclerView.LayoutManager mlayoutmanager;
    private RecyclerView.Adapter madapter;
    private  RecyclerView mrecyclerView;

    ArrayList<EmailItem> resultemails = new ArrayList<EmailItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_threads);
        setTitle("Inbox");
        final SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

        tv_displayname=(TextView)findViewById(R.id.tvUsername);
        iv_newemail=(ImageView) findViewById(R.id.ivnewemail);
        iv_logout=(ImageView)findViewById(R.id.ivLogout);
        //recylerview
        mrecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mrecyclerView.setHasFixedSize(true);
        //use linear layout manager
        mlayoutmanager = new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(mlayoutmanager);
        //recyclerview

        //intent
        if (getIntent() != null && getIntent().getExtras() != null) {
            Intent intent = getIntent();
            UserAuthentication user = (UserAuthentication) intent.getExtras().getSerializable("authenticationdetails");
            token = preferences.getString("token", "");
            // preferences.edit().putString("userid",user.userId.toString() ).commit();

            Log.d("demo", "token" + token);
         //   userid = user.userId;
            //tv_displayname = (TextView) findViewById(R.id.);
            if (user.userFname != null)
                tv_displayname.setText(user.userFname.toString() + " " + user.userLname.toString());
            if (isConnected()) {
                new LoadEmails().execute(threadurl);

            }

            //callMessagethreads();
            // displaySources();


        }

        //intent

        //Logout
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                preferences.edit().remove("token").commit();
                String token = preferences.getString("token", "");
                Log.d("demo", "tokenafterlogout :" + token);
                Intent loginintent = new Intent(EmailThreads.this, MainActivity.class);

                startActivity(loginintent);
                finish();
            }
        });

        //end logout

        //new email creation
        iv_newemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newmailintent=new Intent(EmailThreads.this,CreateNewMail.class);
                //logic
                startActivity(newmailintent);

            }
        });


    }

    //load emails

    public class LoadEmails extends AsyncTask<String, Integer, ArrayList<EmailItem>>
    {

        @Override
        protected ArrayList<EmailItem> doInBackground(String... strings) {
            ArrayList<EmailItem> result = new ArrayList<>();
            OkHttpClient threadclient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .header("Authorization", "BEARER " + token)
                    .build();
            try {
                Response response = threadclient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    JSONObject root = null;
                    try {
                        root = new JSONObject(json);

                        JSONArray threads = root.getJSONArray("messages");
                        for (int i = 0; i < threads.length(); i++) {
                            JSONObject sourcesJson = threads.getJSONObject(i);
                            EmailItem emailitem = new EmailItem();
                            // "sender_fname": "Alice",
                            //            "sender_lname": "Smith",
                            //            "id": "4",
                            //            "sender_id": "2",
                            //            "receiver_id": "2",
                            //            "message": "Hi",
                            //            "subject": "Hello",
                            //            "created_at": "2018-10-20 02:43:07",
                            //            "updated_at": "2018-10-20 02:43:07"
                            emailitem.firstname = sourcesJson.getString("sender_fname");
                            emailitem.lastname = sourcesJson.getString("sender_lname");
                            emailitem.id=sourcesJson.getString("id");
                            emailitem.sender_id=sourcesJson.getString("sender_id");
                            emailitem.receiver_id=sourcesJson.getString("receiver_id");
                            emailitem.message=sourcesJson.getString("message");
                            emailitem.subject=sourcesJson.getString("subject");
                            emailitem.Created_at=sourcesJson.getString("created_at");
                            emailitem.updated_at= sourcesJson.getString("updated_at");





                            result.add(emailitem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Toast.makeText(MessageThreads.this, "Error from AApi"+response.message().toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;

           // return null;
        }

        @Override
        protected void onPostExecute(ArrayList<EmailItem> emails) {
           // super.onPostExecute(emails);

            resultemails = emails;
                displayemails(resultemails);

            Log.d("demo","emailthreads"+resultemails.toString());
        }
    }
    //end load emails

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        } else
            return true;
    }

    public void displayemails(ArrayList<EmailItem> data)
    {
        madapter = new EmailitemsAdapter(resultemails);
        mrecyclerView.setAdapter(madapter);
    }

    //emailadapter




    public class EmailitemsAdapter extends RecyclerView.Adapter<EmailitemsAdapter.ViewHolder> {
        ArrayList<EmailItem> mData;

        public EmailitemsAdapter(ArrayList<EmailItem> mData) {
            this.mData = mData;
        }

        @NonNull
        @Override
        public EmailitemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            //return null;
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_source_item,parent,false);
            EmailitemsAdapter.ViewHolder viewHolder=new EmailitemsAdapter.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(EmailitemsAdapter.ViewHolder holder, int position) {

            EmailItem emailitem=mData.get(position);

            Log.d("demo","emailadapter email");
            holder.tvsubject.setText(emailitem.subject.toString());
            holder.tvcreatedat.setText(emailitem.Created_at);
            holder.ivdelete.setTag(position);
            //iv_delete.setTag(position);
            //access to source
            holder.emailitem=emailitem;
        }



        @Override
        public int getItemCount() {
            return mData.size();
        }

        public  class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView tvsubject;
            TextView tvcreatedat;
            EmailItem emailitem;
            ImageView ivdelete;
            public ViewHolder(@NonNull final View itemView) {
                super(itemView);
                this.emailitem=emailitem;
                tvsubject=(TextView)itemView.findViewById(R.id.tvSubject);
                tvcreatedat=(TextView)itemView.findViewById(R.id.tv_createdtime);
                ivdelete=(ImageView)itemView.findViewById(R.id.ivdeletemail);

                // tvcreatedat=(TextView)itemView.findViewById(R.id.tv_createdtime);

           ivdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("demo","buttonclicked");
                    //int i = (Integer) itemView.getTag();

                    new DeleteThread().execute("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/delete/"+emailitem.id);
                    new LoadEmails().execute(threadurl);
                }
            });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // new DeleteThread().execute("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/inbox/delete/"+"");
                        Displayintent( emailitem);
                    }
                });

            }
        }
    }


    //emailadaper


    //delete email

    public class DeleteThread extends AsyncTask<String, Integer,EmailItem> {
        @Override
        protected void onPostExecute(EmailItem emailItems) {
            super.onPostExecute(emailItems);
        }

        @Override
        protected EmailItem doInBackground(String... strings) {
            EmailItem result = new EmailItem();
            OkHttpClient threadclient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .header("Authorization", "BEARER " + token)
                    .build();
            try {
                Response response = threadclient.newCall(request).execute();
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    JSONObject root = null;
                    try {
                        root = new JSONObject(json);
                        JSONObject obj = root.getJSONObject("thread");
                        result.id = obj.getString("id");
                        result.message = obj.getString("message");
                       // result.userid = obj.getString("user_id");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                     //Toast.makeText(MessageThreads.this, "Error from AApi"+response.message().toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
    }
   public void Displayintent(EmailItem emailitem)
   {

       Intent displayintent=new Intent(EmailThreads.this,Displaymail.class);
       displayintent.putExtra("display",emailitem);
       Log.d("demo",emailitem.toString());
       startActivity(displayintent);

   }
}
