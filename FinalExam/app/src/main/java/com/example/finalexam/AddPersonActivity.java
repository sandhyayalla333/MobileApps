package com.example.finalexam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalexam.utils.Person;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPersonActivity extends AppCompatActivity {
    final String TAG = "demo";
    EditText editTextBudget, editTextName;
    DatabaseReference myRef;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        setTitle(R.string.add_person);
        editTextBudget = findViewById(R.id.editTextBudget);
        editTextName = findViewById(R.id.editTextName);
        myRef = FirebaseDatabase.getInstance().getReference().child("persons");
        firebaseAuth= FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_person_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit_person_menu_item:
                String name = editTextName.getText().toString();
                String budgetString = editTextBudget.getText().toString();

                if(name == null || name.equals("")){
                    Toast.makeText(this, "Enter Name !!", Toast.LENGTH_SHORT).show();
                } else if(budgetString == null || budgetString.equals("")){
                    Toast.makeText(this, "Enter Budget !!", Toast.LENGTH_SHORT).show();
                } else{
                    try{

                        int budget = Integer.valueOf(budgetString);
                        if(budget > 0 ){
                            String key = myRef.push().getKey();
                            Person person=new Person(name,budget,0,0,key);

                            myRef.child(key).setValue(person).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AddPersonActivity.this, "person added !", Toast.LENGTH_SHORT).show();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddPersonActivity.this, "Not able to save", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent i=new Intent(AddPersonActivity.this,ChristmasListActivity.class);
                            startActivity(i);


                            finish();
                        } else{
                            Toast.makeText(this, "Enter Valid Budget !!", Toast.LENGTH_SHORT).show();
                            editTextBudget.setText("");
                        }
                    } catch (NumberFormatException ex){
                        Toast.makeText(this, "Enter Valid Budget !!", Toast.LENGTH_SHORT).show();
                    }

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
