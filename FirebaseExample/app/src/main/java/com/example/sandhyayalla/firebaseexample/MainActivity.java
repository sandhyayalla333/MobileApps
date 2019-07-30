package com.example.sandhyayalla.firebaseexample;
//Main Activity
//Inclass09 - naga Sandhyadevi yalla, Ashika
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//import com.google.firebase.auth.AuthResult;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
//import

public class MainActivity extends AppCompatActivity {
   // FirebaseDatabase database;
    DatabaseReference myRef;
    //FirebaseAuth
    //FirebaseAuth auth;
    String currentemail="";
    Button btnlogin;
    EditText et_email;
    EditText et_pwd;
    ArrayList<User> userlist=new ArrayList<User>();
    int userexist=0;
    FirebaseAuth firebaseAuth;
    User usertosend=new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(android.R.layout);
        setContentView(R.layout.activity_main);
        myRef = FirebaseDatabase.getInstance().getReference().child("users");
        btnlogin=(Button)findViewById(R.id.btnlogin);
        et_email=(EditText)findViewById(R.id.et_email);
        et_pwd=(EditText)findViewById(R.id.et_pwd);
        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null)
        {
            currentemail=firebaseAuth.getCurrentUser().getEmail();
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //User user1=dataSnapshot.getValue(User.class);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);

                        //Log.d("demo","datasnap"+user.email.toString()+user.password.toString());
                        // Log.d("demo","given"+et_email.getText().toString()+et_pwd.getText().toString());
                        if(user.email.toString().equals(currentemail))
                        {
                            //Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();

                            //usertosend=user;
                            Log.d("demo","user to send "+user.toString());
                            finish();
                            Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class);

                            intent.putExtra("user",user);
                            startActivity(intent);



                        }

                        //events.add(event);

                    }
                    if(userexist==0)
                    {
                        //Toast.makeText(MainActivity.this, "Login is not Successful", Toast.LENGTH_SHORT).show();
                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            finish();
            Intent intent=new Intent(MainActivity.this,ChatRoomActivity.class);
            // intent.putExtra("user",user);
            startActivity(intent);
        }

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_email.getText().toString().isEmpty())
                {
                    //Toast.makeText(MainActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    et_email.setError("Enter Email");
                }
                else if(et_pwd.getText().toString().isEmpty())
                {
                    //Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    et_pwd.setError("Enter Password");
                }
                else
                {
                    final User loginuser=new User(et_email.getText().toString(),et_pwd.getText().toString());
                    //registeruser();
                    firebaseAuth.signInWithEmailAndPassword(et_email.getText().toString(),et_pwd.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                       // Log.d("demo", "signInWithEmail:success");
                                        Toast.makeText(MainActivity.this, "Login is Successful", Toast.LENGTH_SHORT).show();
                                        //call database to get data

                                        //old code
                                        myRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //User user1=dataSnapshot.getValue(User.class);
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    User user = snapshot.getValue(User.class);
                                                    User user1=new User(user.email,user.password);
                                                    userlist.add(user1);
                                                    //Log.d("demo","datasnap"+user.email.toString()+user.password.toString());
                                                    // Log.d("demo","given"+et_email.getText().toString()+et_pwd.getText().toString());
                                                    if(user.email.toString().equals(et_email.getText().toString()) && user.password.toString().equals(et_pwd.getText().toString()) )
                                                    {
                                                        //Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                                        et_pwd.setText("");
                                                        et_email.setText("");
                                                        userexist=1;
                                                        //usertosend=user;
                                                        Log.d("demo","user to send "+user.toString());
                                                        finish();
                                                        Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class);

                                                        intent.putExtra("user",user);
                                                        startActivity(intent);



                                                    }

                                                    //events.add(event);

                                                }
                                                if(userexist==0)
                                                {
                                                    //Toast.makeText(MainActivity.this, "Login is not Successful", Toast.LENGTH_SHORT).show();
                                                }




                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        //end call

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("demo", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Login is not Successful.",
                                                Toast.LENGTH_SHORT).show();
                                       // updateUI(null);
                                    }

                                    // ...
                                }
                            });



                }
            }
        });



        Button btnsignup=(Button)findViewById(R.id.btnSignUp);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_pwd.setText("");
                et_email.setText("");
                Intent signupintent=new Intent(MainActivity.this,SignUpactivity.class);
                startActivity(signupintent);

            }
        });

        //auth.createUserWithEmailAndPassword("sandhyayalla333@gmail.com","12345");


    }

}
