package org.iw11.backend.model;

import com.google.common.base.Preconditions;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BusRoute {

    private List<BusStation> stations;

    public BusRoute(List<BusStation> stations) {
        Preconditions.checkNotNull(stations);
        this.stations = Collections.unmodifiableList(stations);
    }

    public List<BusStation> getStations() {
        return stations;
    }

    public boolean canSatisfyDemand(BusDemand demand) {
        int departure = stations.indexOf(demand.getDepartue());
        int destination = stations.indexOf(demand.getDestination());
        if (departure == -1 || destination == -1)
            return false;
        return departure < destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusRoute busRoute = (BusRoute) o;
        return stations.equals(busRoute.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        stations.stream().forEach(station -> result.append(station.getName()).append(" "));
        return result.toString();
    }
}
