package com.helloworld.goodpoint.pojo;

import com.google.android.gms.maps.model.LatLng;

public class ObjectLocation {

    LatLng latLng;
    int userId;

    public ObjectLocation(double longitude, double latitude, int userId) {
        latLng = new LatLng(latitude, longitude);
        this.userId = userId;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public int getUserId() {
        return userId;
    }
}
