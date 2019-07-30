package com.example.sandhyayalla.chatroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SignUpActivity extends AppCompatActivity {

    TextView et_firstname,et_lastname,et_email,et_pwd,et_repwd;
    Button btnSignUp,btncancel;
    UserAuthentication authenticationDetails;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_signup);
        setTitle("Sign up");

        if(getIntent()!=null) {
            et_firstname = (EditText) findViewById(R.id.tvfirstname);
            et_lastname = (EditText) findViewById(R.id.tvlastname);
            et_email = (EditText) findViewById(R.id.tvemail);
            et_pwd = (EditText) findViewById(R.id.etpwd);
            et_repwd = (EditText) findViewById(R.id.etrepwd);
            btncancel = (Button) findViewById(R.id.btncancel);
            btnSignUp = (Button) findViewById(R.id.btnsignup);
            //cancel button
            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (et_firstname.getText().toString() == null || et_firstname.getText().toString().isEmpty()) {
                        et_firstname.setError("Enter firstname");
                    } else if (et_lastname.getText().toString() == null || et_lastname.getText().toString().isEmpty() ) {
                        et_lastname.setError("Enter last name ");
                    } else if (et_email.getText().toString() == null || et_email.getText().toString().isEmpty()) {
                        et_email.setError("Enter email id  ");
                    } else if (et_pwd.getText().toString() == null || et_pwd.getText().toString().isEmpty()) {
                        et_lastname.setError("Enter Password ");
                    }
                    else if(et_pwd.getText().length()<6)
                    {
                        et_pwd.setError("Password has to be 6 characters or more");
                    }
                    else if (et_repwd.getText().toString() == null || et_repwd.getText().toString().isEmpty()) {
                        et_repwd.setError("Retype the password ");
                    } else if (!et_pwd.getText().toString().equals(et_repwd.getText().toString())) {
                        Toast.makeText(SignUpActivity.this, "Given password and the repeated password must match", Toast.LENGTH_SHORT).show();
                    } else {
                        signUp();
                    }
                }
            });

            handler=new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    if(message.what==400)
                    {
                       String emailerror=(String)message.obj;
                        et_email.setError("Choose Another email ! ");
                        Log.d("demo",message.getData().toString());
                    }
                    else if(message.what==200)
                    {
                        Toast.makeText(SignUpActivity.this, "User has been Created", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
        }

    }

    public void signUp()
    {
        OkHttpClient client=new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("email",et_email.getText().toString())
                .add("password",et_pwd.getText().toString())
                .add("fname",et_firstname.getText().toString())
                .add("lname",et_lastname.getText().toString())
                .build();
        Request request = new Request.Builder()
                .url("http://ec2-18-234-222-229.compute-1.amazonaws.com/api/signup")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body())
                {
                    //if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    if(response.isSuccessful()) {

                        authenticationDetails = AuthenticationParser.parseAuthenticationDetails(response.body().string());
                        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                        preferences.edit().remove("token").commit();//remove if any prev tokens
                        preferences.edit().putString("token",authenticationDetails.token.toString() ).commit();

                        Log.d("demo","output "+authenticationDetails.token.toString());
                        Message message=new Message();
                        message.what=200;
                        handler.sendMessage(message);
                        Intent messageintent=new Intent(SignUpActivity.this,MessageThreads.class);
                        messageintent.putExtra("authenticationdetails",authenticationDetails);
                        startActivity(messageintent);
                        finish();
                    }
                    else
                    {
                        //et_email.setError(response.message().toString());
                        Message message=new Message();
                        message.what=400;
                        //message.obj=response.message().toString();
                        //Log.d("message",response.message().toString());
                       // message.setData(response.message().toString());
                        handler.sendMessage(message);
                    }
                    //Toast.makeText(SignUpActivity.this, response.message().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
