package org.iw11.backend.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BusLocationModel {

    @JsonProperty("latitude")
    private double latitude;

    @JsonProperty("longitude")
    private double longitude;

    public BusLocationModel() { }

    public BusLocationModel(String name, double latitude, double longitude) {
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
