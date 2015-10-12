package com.support.android.designlibdemo.model;

/**
 * Created by agu on 12/10/15.
 */
public class MapCoordenates {

    private static MapCoordenates ourInstance = new MapCoordenates();
    private String latitude;
    private String longitude;

    public static MapCoordenates getInstance() {
        return ourInstance;
    }

    private MapCoordenates() {
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
