package org.iw11.backend.model;

public class GeoCoordinates {

    private double latitude;
    private double longitude;

    public GeoCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
