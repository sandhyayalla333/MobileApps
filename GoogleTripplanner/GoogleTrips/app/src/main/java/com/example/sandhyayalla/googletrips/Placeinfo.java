package com.example.sandhyayalla.googletrips;

import java.io.Serializable;

public class Placeinfo  implements Serializable{
    public String placename,latitude,longitude,vicinity,reference;

    public Placeinfo(String placename, String latitude, String longitude, String vicinity) {
        this.placename = placename;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vicinity = vicinity;

    }



    public Placeinfo() {
    }

    @Override
    public String toString() {
        return "Placeinfo{" +
                "placename='" + placename + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", vicinity='" + vicinity + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }
}
