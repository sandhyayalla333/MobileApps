package com.example.finalexam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalexam.utils.Gift;
import com.example.finalexam.utils.GiftsAdapter;
import com.example.finalexam.utils.Person;
import com.example.finalexam.utils.PersonsAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddGiftActivity extends AppCompatActivity {
    final String TAG = "demo";
    ListView listView;
    GiftsAdapter giftsAdapter;
    ArrayList<Gift> gifts = new ArrayList<>();
    DatabaseReference myRef;
    DatabaseReference personref;
    int balance=0;
    Person person=new Person();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gift);
        setTitle(R.string.add_gift);
        listView = findViewById(R.id.listview);
        myRef= FirebaseDatabase.getInstance().getReference().child("gifts");
        personref=FirebaseDatabase.getInstance().getReference().child("persons");
        if(getIntent()!=null && getIntent().getExtras()!=null)
        {
            //Intent i=new Intent();
           person=(Person)getIntent().getExtras().getSerializable("person");


            if(person!=null) {
               balance=person.totalBudget-person.totalBought;
            }

        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gifts.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Gift gift = snapshot.getValue(Gift.class);
                    if(gift.price<balance) {
                        gifts.add(gift);
                    }

                }

                giftsAdapter = new GiftsAdapter(AddGiftActivity.this, R.layout.gift_item, gifts);
                listView.setAdapter(giftsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Gift selectedgift=gifts.get(position);
                int count =person.giftCount+1;

                Person updateperson=new Person(person.name,person.totalBudget,selectedgift.price,count,person.id);
                myRef.child(person.id).setValue(person).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddGiftActivity.this, "Changes are applied to selected task", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
    }
}
