package com.example.sandhyayalla.googletrips;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class Trip implements Serializable{
   public String key,username,tripname,destinationname,datetime,destlat,destlong;
   //public LatLng latLng;
   public ArrayList<Placeinfo> placeinfos;


    public Trip(String key, String username, String tripname, String destinationname, String datetime, String destlat, String destlong, ArrayList<Placeinfo> placeinfos) {
        this.key = key;
        this.username = username;
        this.tripname = tripname;
        this.destinationname = destinationname;
        this.datetime = datetime;
        this.destlat = destlat;
        this.destlong = destlong;
        this.placeinfos = placeinfos;
    }

    public Trip(String key, String username, String tripname, String destinationname, String datetime, String destlat, String destlong) {
        this.key = key;
        this.username = username;
        this.tripname = tripname;
        this.destinationname = destinationname;
        this.datetime = datetime;
        this.destlat = destlat;
        this.destlong = destlong;
    }



    public  Trip()
    {}


    @Override
    public String toString() {
        return "Trip{" +
                "key='" + key + '\'' +
                ", username='" + username + '\'' +
                ", tripname='" + tripname + '\'' +
                ", destinationname='" + destinationname + '\'' +
                ", datetime='" + datetime + '\'' +
                ", placeinfos=" + placeinfos +
                '}';
    }
}
