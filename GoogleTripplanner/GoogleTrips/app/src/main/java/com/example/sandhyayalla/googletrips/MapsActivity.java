package com.example.sandhyayalla.googletrips;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    ArrayList<Placeinfo> placlist=new ArrayList<>();
    String apiurl="";
    Button btn_restaurant,btn_close,btn_save;
    Place destination=null;
    String lattitude;
    String Longitude;
    EditText et_tripname;
    ArrayList<Placeinfo> selectedlist=new ArrayList<>();
    AlertDialog.Builder builder;
    AlertDialog alert;
    FirebaseAuth firebaseAuth;
    String username="";
    DatabaseReference myRef;
    String destinationname;
    String key;
    Trip triptosave=null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle("Make a Trip");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btn_restaurant=(Button)findViewById(R.id.btn_Restaurant);
        btn_close=(Button)findViewById(R.id.btn_close);
        btn_save=(Button)findViewById(R.id.btn_savetrip);
        et_tripname=(EditText)findViewById(R.id.et_tripname);
        firebaseAuth=FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference().child("tasks");
        if(firebaseAuth.getCurrentUser()!=null)
        {
            username=firebaseAuth.getCurrentUser().getEmail().toString();
            Log.d("demo","loged in user"+username);
        }



        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("USA")
                .build();

        autocompleteFragment.setFilter(typeFilter);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.d("demo", "Place: " + place.getName());
                destination=place;
                destinationname=place.getName().toString();

              moveCamera(place.getLatLng(),6.0f,place.getAddress().toString());

            }

            @Override
            public void onError(Status status) {
                Log.d("demo","error here "+status.toString());
            }


        });

        //alert dialog
        builder= new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure to exit without saving?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                Intent intent1=new Intent(MapsActivity.this,TripActivity.class);
                startActivity(intent1);
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();
                Toast.makeText(MapsActivity.this, "Click on savebutton to save trip details", Toast.LENGTH_SHORT).show();
            }
        });

        alert = builder.create();

        //close the map
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.show();


            }
        });


        //save the trip details
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(et_tripname.getText().toString().isEmpty())
                    {
                        et_tripname.setError("Enter Trip name before saving ");
                    }
                    else if(destination==null)
                    {
                        Toast.makeText(MapsActivity.this, "Please select destination", Toast.LENGTH_SHORT).show();
                    }
                    else
                        {
                            //String placeID1 = destination.getId();
                            LatLng latLng1 = destination.getLatLng();
                            double lat = latLng1.latitude;
                            double lng = latLng1.longitude;

                            lattitude = String.valueOf(lat);
                            Longitude = String.valueOf(lng);
                            Calendar c = Calendar.getInstance();
                            key=myRef.push().getKey();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDate = df.format(c.getTime());
                            if(selectedlist.size()!=0) {
                                triptosave = new Trip(key, username, et_tripname.getText().toString(), destinationname, formattedDate, lattitude,Longitude, selectedlist);
                            }
                            else
                            {
                                triptosave = new Trip(key, username, et_tripname.getText().toString(), destinationname, formattedDate,lattitude,Longitude);
                            }
                            myRef.child(key).setValue(triptosave).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Log.d("demo","successfully saved data ");
                                    Toast.makeText(MapsActivity.this, " Added to todolist", Toast.LENGTH_SHORT).show();
                                  /* for(int i=0;i<selectedlist.size();i++) {
                                        Placeinfo place=selectedlist.get(i);
                                        String placekey = myRef.child("placeinfo").push().getKey();
                                       myRef.child(key).child(placekey).setValue(place);
                                        Log.d("demo","placeinfosaved"+place.toString());
                                    }*/


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("demo","failed "+e.toString());
                                }
                            });



                        Intent i = new Intent(MapsActivity.this, TripActivity.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(MapsActivity.this, "Trip details are saved ", Toast.LENGTH_SHORT).show();
                    }

            }
        });

        btn_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (destination == null) {

                    Toast.makeText(MapsActivity.this, "Please enter destination", Toast.LENGTH_SHORT).show();
                }
                else
                {
                String placeID = destination.getId();
                LatLng latLng = destination.getLatLng();
                double lat = latLng.latitude;
                double lng = latLng.longitude;
                Log.d("demo", "latlong" + latLng.toString());
                lattitude = String.valueOf(lat);
                Longitude = String.valueOf(lng);

                String placename = (String) destination.getAddress();
                Log.d("demo", "place" + placename);
                //String placename=place.getLatLng();
                geturl(lattitude, Longitude, "restaurant");


                Log.d("demo", "Placeid" + placeID);
                new AsyncnearbyPlaces().execute();
            }
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

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {

                Log.d("demo","marker here "+marker.getPosition().toString());
                Log.d("demo","marker name"+marker.getTitle());
                String markervicinity=marker.getTitle();
                LatLng latLng= marker.getPosition();
                marker.getTitle();
                double lat=latLng.latitude;
                double longt=latLng.longitude;
                String lat1=String.valueOf(lat);
                String longt1=String.valueOf(longt);
                Log.d("demo","markerlatlong"+latLng.toString()+"markerclicked");
                Log.d("demo","placelistsize"+placlist.size());
                Log.d("demo",placlist.toString());
                Placeinfo placeinfo1=new Placeinfo(marker.getId().toString(),lat1,longt1,markervicinity);
                if(selectedlist.size()<15) {
                    if(selectedlist.contains(placeinfo1))
                    {
                        Toast.makeText(MapsActivity.this, "Already added ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        selectedlist.add(placeinfo1);
                        Toast.makeText(MapsActivity.this, "Added to the tripdetails", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MapsActivity.this, "You can select upto 15 places only.please save details ", Toast.LENGTH_SHORT).show();



                }



                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void moveCamera(LatLng latLng,float zoom,String title)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng).title(title);
        mMap.addMarker(markerOptions);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {


       return true;
    }

    public class  AsyncnearbyPlaces extends AsyncTask<Object,String,String>
    {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Shownearbyplaces(placlist);
        }

        @Override
        protected String doInBackground(Object... objects) {
            HttpURLConnection connection = null;
            //  ArrayList<News> result = new ArrayList<News>();
            StringBuilder stringBuilder=new StringBuilder();
            BufferedReader reader=null;

            try {
                URL url = new URL(apiurl);
                Log.d("demo","url is "+apiurl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    String json=stringBuilder.toString();
                    Log.d("demo","data here "+json);
                    try {
                        JSONObject root = new JSONObject(json);

                        JSONArray threads = root.getJSONArray("results");

                        for (int i = 0; i < threads.length(); i++) {
                            JSONObject sourcesJson = threads.getJSONObject(i);

                            Placeinfo placeinfo = new Placeinfo();
                            placeinfo.latitude = sourcesJson.getJSONObject("geometry").getJSONObject("location").getString("lat");

                            placeinfo.longitude = sourcesJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

                            placeinfo.placename = sourcesJson.getString("name");


                            placeinfo.vicinity=sourcesJson.getString("vicinity");
                           // placeinfo.reference = sourcesJson.getString("reference");



                            placlist.add(placeinfo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }





                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return null;



        }
    }
    private void Shownearbyplaces(ArrayList<Placeinfo> placedata)
    {
        Log.d("demo","got here"+placedata.size());

        if(placedata.size()!=0) {
            for (int i = 0; i < placedata.size(); i++) {
                Placeinfo placeinfo = placedata.get(i);
                Double lat = Double.parseDouble(placeinfo.latitude);
                Double longitude = Double.parseDouble(placeinfo.longitude);
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng = new LatLng(lat, longitude);
                moveCamera(latLng, 14.0f, placeinfo.vicinity);

            }
        }
        else
        {
            Toast.makeText(MapsActivity.this, "No Near by Restaurants available", Toast.LENGTH_SHORT).show();
        }
    }
    private String geturl(String latitude,String longitude,String type)
    {
        String url=null;

        String api="https://maps.googleapis.com/maps/api/place/findplacefromtext/output?parameters";
        url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=1609&type="+type+"&key=AIzaSyBJNgfjpeFIAv9AFK46ssqdAyz7xbjVQyg";
        apiurl=url;
        //Log.d("demo")
        return url;
    }

    protected Float getDistanceInMeters(LatLng a, LatLng b) {
        Location l1 = new Location("");
        Location l2 = new Location("");
        l1.setLatitude(a.latitude);
        l1.setLongitude(a.longitude);
        l2.setLatitude(b.latitude);
        l2.setLongitude(b.longitude);
        return l1.distanceTo(l2);
    }


}
