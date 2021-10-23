package com.helloworld.goodpoint.pojo;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObjectLocation {
    @SerializedName("longitude")
    @Expose
    double longitude;
    @SerializedName("latitude")
    @Expose
    double latitude;
    @SerializedName("user_id")
    @Expose
    int userId;


    public ObjectLocation(double longitude, double latitude, int userId) {
        this.userId = userId;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public int getUserId() {
        return userId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
