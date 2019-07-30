package com.example.sandhyayalla.firebaseexample;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignUpactivity extends AppCompatActivity {
    DatabaseReference myRef;
    String useremail;
    Button btn_login1;
    FirebaseAuth firebaseAuth;
    EditText et_name;
    EditText et_lname;
    EditText et_email;
    EditText et_pwd;
    EditText et_repwd;
    User usertosend;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_upactivity);
        btn_login1=(Button)findViewById(R.id.btn_login1);
        myRef = FirebaseDatabase.getInstance().getReference().child("users");

            btn_login1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
         et_name=(EditText)findViewById(R.id.et_fname);
         et_lname=(EditText)findViewById(R.id.et_lastname);
         et_email=(EditText)findViewById(R.id.et_email);
        et_pwd=(EditText)findViewById(R.id.et_pwd1);
         et_repwd=(EditText)findViewById(R.id.et_repwd);
        Button btnsignup=(Button)findViewById(R.id.btnsign);
        firebaseAuth=FirebaseAuth.getInstance();
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_name.getText().toString().isEmpty()) {
                    et_name.setError("Enter first Name");
                }
                 else   if (et_lname.getText().toString().isEmpty()) {
                        et_lname.setError("Enter lastname Name");
                } else if (et_email.getText().toString().isEmpty()) {
                    et_email.setError("Enter Email");
                } else if (et_pwd.getText().toString().isEmpty()) {
                    et_pwd.setError("Enter password");
                }
                else if(et_pwd.getText().toString().length()<6)
                {
                    et_pwd.setError("Password length should be more than 6 char");
                }
                else if (et_repwd.getText().toString().isEmpty())
                {
                    et_repwd.setError("Repeat Password");
                }
                else if (!et_pwd.getText().toString().equals(et_repwd.getText().toString()))
                {
                   et_pwd.setError("Password and repeat password should match ");
                }

                else {

                     useremail=et_email.getText().toString();
                     //signupuser();
                    firebaseAuth.createUserWithEmailAndPassword(useremail, et_pwd.getText().toString())
                            .addOnCompleteListener(SignUpactivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("demo", "createUserWithEmail:success");
                                        Toast.makeText(SignUpactivity.this, "User has been created", Toast.LENGTH_SHORT).show();
                                        //FirebaseUser user = firebaseAuth.getCurrentUser();
                                        //updateUI(user);

                                        User user=new User(et_name.getText().toString(),et_lname.getText().toString(),et_email.getText().toString(),et_pwd.getText().toString());
                                         usertosend=user;

                                        myRef.push().setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Log.d("demo","successfully saved data ");
                                                //Toast.makeText(SignUpactivity.this, "User has been Created", Toast.LENGTH_SHORT).show();
                                                Log.d("demo","user to send in intent "+usertosend);
                                                finish();
                                                Intent intent=new Intent(SignUpactivity.this,ChatRoomActivity.class);
                                                intent.putExtra("user",usertosend);
                                                startActivity(intent);
                                                et_email.setText("");
                                                et_lname.setText("");
                                                et_name.setText("");
                                                et_pwd.setText("");
                                                et_repwd.setText("");
                                            }
                                        });
                                       // userlist.clear();




                                        ///



                                        //end if task successful
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("demo", "createUserWithEmail:failure", task.getException());
                                       // Toast.makeText(EmailPasswordActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            });

                //Intent signupintent=new Intent(MainActivity.this,SignUpactivity.class);
              /*  myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //User user1=dataSnapshot.getValue(User.class);
                        ArrayList<String> userlist=new ArrayList<String>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            userlist.add(user.email);

                        }
                        Log.d("demo","usermails: "+userlist.toString());
                       if(userlist.contains(useremail))
                       {
                           //Toast.makeText(SignUpactivity.this, "Email already Exists", Toast.LENGTH_SHORT).show();
                           et_email.setText("");
                           et_email.setError("Email already Exists");
                       }
                       else
                       {
                           User user=new User(et_name.getText().toString(),et_lname.getText().toString(),et_email.getText().toString(),et_pwd.getText().toString());

                  myRef.push().setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                   // Log.d("demo","successfully saved data ");
                    Toast.makeText(SignUpactivity.this, "User has been Created", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpactivity.this, "unable to create Account "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
                  userlist.clear();

                           Intent intent=new Intent(SignUpactivity.this,ChatRoomActivity.class);
                           intent.putExtra("user",user);
                           startActivity(intent);
                           et_email.setText("");
                           et_lname.setText("");
                           et_name.setText("");
                           et_pwd.setText("");
                           et_repwd.setText("");

                       }
                        //if(userlist.contains())


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/
            }
            }
        });
    }
    private void signupuser()
    {
        Log.d("demo","singup");
        firebaseAuth.createUserWithEmailAndPassword(et_email.getText().toString(),et_pwd.getText().toString()).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("demo","signupuser");
                if(task.isSuccessful()) {
                    Toast.makeText(SignUpactivity.this, "User has been created ", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(SignUpactivity.this, ChatRoomActivity.class);
                    //intent.putExtra("user",user);
                    startActivity(intent);
                    et_email.setText("");
                    et_lname.setText("");
                    et_name.setText("");
                    et_pwd.setText("");
                    et_repwd.setText("");
                }
                else
                {
                    Log.d("demo","task exception"+task.getException());
                }
            }
        });
    }
}
