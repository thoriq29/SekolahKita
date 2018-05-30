package com.example.thoriq.sekolahkita.Model;

/**
 * Created by Thoriq on 25/02/2018.
 */

public class LatLangs {
    String lat,lng;

    public LatLangs(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
