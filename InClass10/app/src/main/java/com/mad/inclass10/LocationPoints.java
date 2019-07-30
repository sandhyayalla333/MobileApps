package com.mad.inclass10;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class LocationPoints {
    private String title;
    private List<LatLng> points;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "MapPoints{" +
                "title='" + title + '\'' +
                ", points=" + points +
                '}';
    }
}
