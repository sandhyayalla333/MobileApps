package com.example.sandhyayalla.googletrips;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Trip> triplist=new ArrayList<>();
    ArrayList<Placeinfo> placeinfos=new ArrayList<>();
    DatabaseReference myRef;;
    String tripkey;
    TextView tripname,tv_displaytrip;
    ImageView iv_closemap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_maps);
        setTitle("Display Trip");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tripname=(TextView)findViewById(R.id.tv_cityname);
        tv_displaytrip=(TextView)findViewById(R.id.tv_triptitle);

        iv_closemap=(ImageView)findViewById(R.id.iv_closedisplay);
        myRef = FirebaseDatabase.getInstance().getReference().child("tasks");

        if(getIntent()!=null )
        {
            Intent i=new Intent();
           tripkey=getIntent().getStringExtra("tripkey");
           Log.d("demo","key to mark"+tripkey);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Trip trip = snapshot.getValue(Trip.class);
                       // Trip trip1 = new Trip(trip.key, trip.username, trip.tripname, trip.destinationname, trip.datetime,);
                        Log.d("demo","keysto"+ trip.key + "="+tripkey);

                        if(trip.key.equals(tripkey))
                        {
                            triplist.add(trip);

                            tv_displaytrip.setText("Trip Name" +trip.tripname);
                            tripname.setText(trip.destinationname);
                            Double lat1 = Double.parseDouble(trip.destlat);
                            Double longitude1 = Double.parseDouble(trip.destlong);

                            LatLng latLngdest=new LatLng(lat1,longitude1);

                            moveCamera(latLngdest, 14.0f, trip.destinationname);
                            if(trip.placeinfos!=null) {
                                for (Placeinfo placeinfo : trip.placeinfos) {
                                    placeinfos.add(placeinfo);
                                }
                                if (placeinfos.size() != 0) {

                                    for (int i = 0; i < placeinfos.size(); i++) {
                                        Placeinfo placeinfo = placeinfos.get(i);
                                        Double lat = Double.parseDouble(placeinfo.latitude);
                                        Double longitude = Double.parseDouble(placeinfo.longitude);
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        LatLng latLng = new LatLng(lat, longitude);
                                        moveCamera(latLng, 14.0f, placeinfo.vicinity);

                                    }
                                }
                                Log.d("demo", "displayplaces" + placeinfos.toString());
                            } //end
                        }

                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        iv_closemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DisplayMapsActivity.this,TripActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }
    private void moveCamera(LatLng latLng,float zoom,String title)
    {

       Log.d("demo","movecamera is here");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng).title(title);
        mMap.addMarker(markerOptions);

    }
}
