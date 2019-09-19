package org.iw11.backend.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.iw11.backend.model.BusRoute;
import org.iw11.backend.model.BusStation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssignedBusModel {

    @JsonProperty("name")
    private String name;

    @JsonProperty("route")
    private List<String> route;

    public AssignedBusModel() { }

    public AssignedBusModel(String name, List<String> route) {
        this.name = name;
        this.route = new ArrayList<>(route);
    }

    public static AssignedBusModel create(String name, BusRoute route) {
        return new AssignedBusModel(name, route.getStations().stream().map(BusStation::getName).collect(Collectors.toList()));
    }

    public String getName() {
        return name;
    }

    public List<String> getRoute() {
        return new ArrayList<>(route);
    }
}
