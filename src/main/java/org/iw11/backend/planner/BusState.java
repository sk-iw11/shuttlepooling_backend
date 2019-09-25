package org.iw11.backend.planner;

import org.iw11.backend.model.BusRoute;
import org.iw11.backend.model.GeoCoordinates;

import java.util.Optional;

public class BusState {

    private BusRoute currentRoute = null;

    private GeoCoordinates currentLocation = null;

    private final Object lock = new Object();

    public BusState() { }

    public Optional<BusRoute> getCurrentRoute() {
        synchronized (lock) {
            return Optional.ofNullable(currentRoute);
        }
    }

    public void setCurrentRoute(BusRoute route) {
        synchronized (lock) {
            currentRoute = route;
        }
    }

    public Optional<GeoCoordinates> getCurrentLocation() {
        synchronized (lock) {
            return Optional.ofNullable(currentLocation);
        }
    }

    public void setCurrentLocation(GeoCoordinates location) {
        synchronized (lock) {
            currentLocation = location;
        }
    }

    public boolean isAvailable() {
        synchronized (lock) {
            return currentRoute == null;
        }
    }
}
