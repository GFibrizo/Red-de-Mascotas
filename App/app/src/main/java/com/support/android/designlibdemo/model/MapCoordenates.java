package com.support.android.designlibdemo.model;

/**
 * Created by agu on 12/10/15.
 */
public class MapCoordenates {

    private static MapCoordenates ourInstance = new MapCoordenates();
    private String latitude = null;
    private String longitude = null;

    public static MapCoordenates getInstance() {
        return ourInstance;
    }

    private MapCoordenates() {
    }

    public boolean isNotSet() {
        return ((latitude == null) || (longitude == null));
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

    public void clear() {
        latitude = null;
        longitude = null;
    }
}
