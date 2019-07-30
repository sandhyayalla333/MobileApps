//MapsActivity.java
//Ashika Shivakote Annegowda, Naga Sandhyadevi Yalla

package com.mad.inclass10;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationPoints locationPoints;
    private int padding = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setTitle("Paths Activity");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        //load the json data from trip.json
        String tripJson = loadJSONFromAsset();

        locationPoints = new Gson().fromJson(tripJson,LocationPoints.class);

        this.mMap.getUiSettings().setZoomControlsEnabled(true);
        this.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //add marker to start and end points
        this.mMap.addMarker(new MarkerOptions().position(locationPoints.getPoints().get(0)).title("Start Location"));
        this.mMap.addMarker(new MarkerOptions().position(locationPoints.getPoints().get(locationPoints.getPoints().size()-1)).title("End location"));

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(locationPoints.getPoints());
        polylineOptions
                .width(5)
                .color(Color.BLUE);

        this.mMap.addPolyline(polylineOptions);

        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
        for (int i = 0; i< locationPoints.getPoints().size(); i++){
            latLngBuilder.include(locationPoints.getPoints().get(i));
        }

        LatLngBounds latLngBounds = latLngBuilder.build();
        final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding);

        this.mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(cameraUpdate);
            }
        });
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = MapsActivity.this.getAssets().open("trip.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
