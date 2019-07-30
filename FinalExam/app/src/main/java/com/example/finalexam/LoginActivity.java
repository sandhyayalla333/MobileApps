package com.example.finalexam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle(R.string.login);
        FirebaseApp.initializeApp(LoginActivity.this);

        editTextEmail = findViewById(R.id.editTextEmail);
        firebaseDatabase=FirebaseDatabase.getInstance();
        editTextPassword = findViewById(R.id.editTextPassword);
        myRef = firebaseDatabase.getReference().child("gifts");
        firebaseAuth= FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null)
        {
            finish();
            Intent intent=new Intent(LoginActivity.this,ChristmasListActivity.class);
            // intent.putExtra("user",user);
            startActivity(intent);

        }

        findViewById(R.id.buttonSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if(email == null || email.equals("")){
                    Toast.makeText(LoginActivity.this, "Enter Email !!", Toast.LENGTH_SHORT).show();
                } else if(password == null || password.equals("")){
                    Toast.makeText(LoginActivity.this, "Enter Password !!", Toast.LENGTH_SHORT).show();
                } else{
                    //login user
                    //goto the main activity
                    //finish this activity.

                    firebaseAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                       // Log.d("demo","user to send "+user.toString());
                                        finish();
                                        Intent intent = new Intent(LoginActivity.this, ChristmasListActivity.class);

                                        //intent.putExtra("user",user);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(LoginActivity.this, "Login is not successful", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // include code to handle the case if the user is already logged in,
        // which should take the user to main activity and finish this activity.
    }
}

