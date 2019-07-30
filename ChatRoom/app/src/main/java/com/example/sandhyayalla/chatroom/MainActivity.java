package com.example.sandhyayalla.chatroom;
//Group R-17
//Assignment 6
//Naga Sandhyadevi yalla,Pawan,Sumanth
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    TextView editTextUserName;
    TextView editTextPassword;
    Handler handler;
    Button buttonLogin;
    String loginUrl = "http://ec2-18-234-222-229.compute-1.amazonaws.com/api/login";
    private final OkHttpClient client = new OkHttpClient();
    public Login login = new Login();
    UserAuthentication authenticationDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //login
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);

        buttonLogin = findViewById(R.id.buttonLogin);
        if (isConnected()) {

            handler=new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    if(message.what==150)
                    {
                        Toast.makeText(MainActivity.this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validateSetCredentials();
                if (editTextUserName.getText().toString().isEmpty() && editTextUserName.getText() != null) {
                    editTextUserName.setError("Enter Username");
                } else if (editTextPassword.getText().toString().isEmpty() && editTextPassword.getText() != null) {
                    editTextPassword.setError("Enter Password");
                }
                else
                {


                RequestBody formBody = new FormBody.Builder()
                        .add("email", editTextUserName.getText().toString())
                        .add("password", editTextPassword.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .url(loginUrl)
                        .post(formBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        if (!response.isSuccessful()) {
                            //throw new IOException("Unexpected code " + response);
                            Message message = new Message();
                            message.what = 150;
                            handler.sendMessage(message);

                        } else
                        {
                            authenticationDetails = AuthenticationParser.parseAuthenticationDetails(response.body().string());
                        Log.d("demo", "details" + authenticationDetails.toString());
                        //Toast.makeText(MainActivity.this,authenticationDetails.toString(),Toast.LENGTH_LONG).show();

                        //call the thread api to get the threads
                        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                        preferences.edit().remove("token").commit();//remove if any pref tokens are there
                        preferences.edit().putString("token", authenticationDetails.token.toString()).commit();

                        Intent messageintent = new Intent(MainActivity.this, MessageThreads.class);
                        messageintent.putExtra("authenticationdetails", authenticationDetails);
                        startActivity(messageintent);
                    }

                            // callMessageThread(authenticationDetails.toString().toString());

                    }
                });
            }//for else
            }
        });

        //end login

        //signup
        Button btn_Signup = (Button) findViewById(R.id.buttonSignUp);
        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    else
        {
            Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

   /* public void validateSetCredentials() {
        if (editTextUserName.getText() != null && !editTextUserName.getText().toString().isEmpty()) {
            editTextUserName.setText(editTextUserName.getText().toString());
            login.setUsername(editTextUserName.getText().toString());
        } else {
            editTextUserName.requestFocus();
            editTextUserName.setError("Enter Username");
        }

        if (editTextPassword.getText() != null && !editTextPassword.getText().toString().isEmpty()) {
            editTextPassword.setText(editTextPassword.getText().toString());
            login.setPassword(editTextPassword.getText().toString());
        } else {
            editTextPassword.requestFocus();
            editTextPassword.setError("Enter Password");
        }
    }*/

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }else
            return true;
    }
}
