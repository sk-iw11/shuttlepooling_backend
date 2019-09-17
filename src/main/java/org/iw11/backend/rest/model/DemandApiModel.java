package org.iw11.backend.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DemandApiModel {

    @JsonProperty("departure")
    private String departure;

    @JsonProperty("destination")
    private String destination;

    public DemandApiModel() { }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }
}
