package com.example.sandhyayalla.googletrips;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.sandhyayalla.googletrips.provider.PlaceContract;
//import com.delaroystudios.locationgeo.provider.PlaceContract;
//import com.example.sandhyayalla.googletrips;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AddNewTripActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mClient;
    Button btn_adddest;
    private static final int REQUEST_LOCATION = 2;
    String lattitude;
    String Longitude;
    GoogleMap mMap;
    Marker marker;
    String apiurl="";
    ArrayList<Placeinfo> placlist=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_trip);
        btn_adddest=(Button)findViewById(R.id.btn_adddest);
        //api logic
        mClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this,this)
                .build();
      //PlaceDetectionClient mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        btn_adddest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(ActivityCompat.checkSelfPermission(AddNewTripActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(AddNewTripActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_LOCATION);



                   // Toast.makeText(AddNewTripActivity.this, "Need to Enable location permissions first", Toast.LENGTH_SHORT).show();
                }
                else
                {

                   /* try {
                        PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                        Intent i = null;
                        i = builder.build(AddNewTripActivity.this);
                        startActivityForResult(i,1);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                    catch (Exception e)
                    {
                        Log.d("demo",e.toString());
                    }
                    */
                   Intent intent=new Intent(AddNewTripActivity.this,MapsActivity.class);
                   startActivity(intent);


                }
            }
        });



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("demo","API Client connection Successful");
        getPlacedata();

    }

    @Override
    public void onConnectionSuspended(int i) {

        Toast.makeText(this, "API Client Connection failed", Toast.LENGTH_SHORT).show();
        mClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public void getPlacedata()
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String req=String.valueOf(requestCode);
        Log.d("demo","result"+ req);
        Log.d("demo",resultCode+"");
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK)

        {
            Place place=PlacePicker.getPlace(this,data);
            if(place==null)
            {
                Log.d("demo","No Place Selected");
                //return;

            }
            else{
                String placeID=place.getId();
                LatLng latLng=place.getLatLng();
                double lat = latLng.latitude;
                double lng = latLng.longitude;
                Log.d("demo","latlong"+latLng.toString());
                lattitude=String.valueOf(lat);
                Longitude=String.valueOf(lng);

                String placename= (String) place.getAddress();
                Log.d("demo","place"+placename);
                //String placename=place.getLatLng();
                geturl(lattitude,Longitude,"restaurant");


                Log.d("demo","Placeid"+placeID);
                new AsyncnearbyPlaces().execute();
               // ContentValues contentValues=new ContentValues();
               // contentValues.put(PlaceContract.PlaceEntry.COLUMN_PLACE_ID,placeID);
                //getContentResolver().insert(PlaceContract.PlaceEntry.CONTENT_URI,contentValues);

            }
        }
    }
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                try {
                    PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                    Intent i = null;
                    i = builder.build(AddNewTripActivity.this);
                    startActivityForResult(i,1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    Log.d("demo",e.toString());
                }

            } else {
                Log.d("demo", "Permission was denied or request was cancelled");
            }
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
                            Log.d("demo","length"+threads.length());
                        for (int i = 0; i < threads.length(); i++) {
                            JSONObject sourcesJson = threads.getJSONObject(i);
                           Log.d("demo","sourcejson"+sourcesJson.toString());
                            Placeinfo placeinfo = new Placeinfo();
                            placeinfo.latitude = sourcesJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
                            Log.d("demo",placeinfo.latitude.toString());
                            placeinfo.longitude = sourcesJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

                            placeinfo.placename = sourcesJson.getString("name");


                            placeinfo.vicinity=sourcesJson.getString("vicinity");
                            placeinfo.placename = sourcesJson.getString("reference");
                            Log.d("demo","nearplace"+placeinfo.toString());


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
        for(int i=0;i<placedata.size()-50;i++)
        {
            Placeinfo placeinfo=placedata.get(i);
            Double lat=Double.parseDouble(placeinfo.latitude);
            Double longitude=Double.parseDouble(placeinfo.longitude);
            MarkerOptions markerOptions=new MarkerOptions();
            LatLng latLng=new LatLng(lat,longitude);
            markerOptions.position(latLng);
            markerOptions.title("Placename"+placeinfo.vicinity.toString());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(markerOptions);
        }
    }
}
