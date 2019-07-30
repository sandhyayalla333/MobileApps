package com.example.sandhyayalla.googletrips;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TripActivity extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    ImageView iv_logout;
    ImageView iv_add;
    FirebaseAuth.AuthStateListener authStateListener;
    GoogleSignInClient googleSignInClient;
    ArrayList<Trip> triplist=new ArrayList<>();
    ArrayList<Placeinfo> placeinfos=new ArrayList<>();
    DatabaseReference myRef;
    MessageAdapter adapter;
    ListView triplistview;
    int positiontodelete=0;
    AlertDialog.Builder builder;
    TextView tv_loginuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        setTitle("Trips");
        iv_logout=(ImageView)findViewById(R.id.iv_logout);
        iv_add=(ImageView)findViewById(R.id.iv_Add);
        firebaseAuth=FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference().child("tasks");
        triplistview=(ListView)findViewById(R.id.triplistview);
        tv_loginuser=(TextView)findViewById(R.id.tv_loginuser);
        //add single firebase
        if(firebaseAuth.getCurrentUser()!=null)
        {
            String[] email=firebaseAuth.getCurrentUser().getEmail().split("@");
            String username=email[0];
            tv_loginuser.setText("Welcome "+username.toString());
        }


            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Trip trip = snapshot.getValue(Trip.class);
                        //Trip trip1 = new Trip(trip.key, trip.username, trip.tripname, trip.destinationname, trip.datetime);

                        triplist.add(trip);


                    }

                    Log.d("demo", "trip details " + placeinfos.toString() + " tripdetails" + triplist.toString());
                    adapter = new MessageAdapter(TripActivity.this, R.layout.activity_textitem, triplist);
                    triplistview.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });





        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               if(firebaseAuth.getCurrentUser()==null)
               {
                   Log.d("demo","user logo ut ");
                   Intent loginActivity=new Intent(TripActivity.this,MainActivity.class);
                   startActivity(loginActivity);
               }
            }
        };

        GoogleSignInOptions  gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                googleSignInClient.signOut();


            }
        });


        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addtripintent=new Intent(TripActivity.this,MapsActivity.class);

                startActivity(addtripintent);
                finish();

            }
        });
        triplistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent display=new Intent(TripActivity.this,DisplayMapsActivity.class);
               Trip trip= triplist.get(i);
                display.putExtra("tripkey",trip.key);
                startActivity(display);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    //Message Adapter
    public class MessageAdapter extends ArrayAdapter<Trip> {
        public MessageAdapter(@NonNull Context context, int resource, @NonNull List<Trip> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            final Trip trip = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_textitem, parent, false);
            }
            TextView tv_text = (TextView) convertView.findViewById(R.id.tv_trip);
            tv_text.setText(trip.tripname.toString());
            TextView tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            tv_username.setText(trip.username.toString());
           // final ImageView iv_deletetrip=(ImageView) convertView.findViewById(R.id.iv_tripdelete);
            TextView tv_destination = (TextView) convertView.findViewById(R.id.tv_destination);
            tv_destination.setText(trip.destinationname.toString());










            // checkBox1.setOnCheckedChangeListener(new OnCh);








            return convertView;
        }
    }

    //end message adapter
}
