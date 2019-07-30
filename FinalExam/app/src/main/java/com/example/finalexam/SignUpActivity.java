package com.example.finalexam;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    DatabaseReference myRef;
    String useremail;
    Button btn_login1;
    FirebaseAuth firebaseAuth;
   // DatabaseReference myRef;


    //EditText et_repwd;
   // User usertosend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle(R.string.signup);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        myRef = FirebaseDatabase.getInstance().getReference().child("gifts");
        firebaseAuth=FirebaseAuth.getInstance();

        findViewById(R.id.buttonSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if(email == null || email.equals("")){
                    Toast.makeText(SignUpActivity.this, "Enter Email !!", Toast.LENGTH_SHORT).show();
                } else if(password == null || password.equals("")){
                    Toast.makeText(SignUpActivity.this, "Enter Password !!", Toast.LENGTH_SHORT).show();
                } else{

                    useremail=editTextEmail.getText().toString();
                    //signupuser();
                    firebaseAuth.createUserWithEmailAndPassword(useremail, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("demo", "createUserWithEmail:success");
                                        Toast.makeText(SignUpActivity.this, "User has been created", Toast.LENGTH_SHORT).show();
                                        finish();
                                        Intent intent=new Intent(SignUpActivity.this,ChristmasListActivity.class);
                                       // intent.putExtra("user",usertosend);
                                        startActivity(intent);
                                        editTextEmail.setText("");

                                        editTextPassword.setText("");




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

                    //signup user
                    //goto the main activity
                    //finish this activity.
                }
            }
        });

        findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
