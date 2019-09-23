package org.iw11.backend.planner;

import org.iw11.backend.model.BusRoute;
import org.iw11.backend.model.GeoCoordinates;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class BusState {

    private AtomicReference<BusRoute> currentRoute = new AtomicReference<>();

    private AtomicReference<GeoCoordinates> currentLocation = new AtomicReference<>();

    public BusState() {
        currentRoute.set(null);
        currentLocation.set(null);
    }

    public Optional<BusRoute> getCurrentRoute() {
        return Optional.ofNullable(currentRoute.get());
    }

    public void setCurrentRoute(BusRoute route) {
        currentRoute.set(route);
    }

    public Optional<GeoCoordinates> getCurrentLocation() {
        return Optional.ofNullable(currentLocation.get());
    }

    public void setCurrentLocation(GeoCoordinates location) {
        currentLocation.set(location);
    }

    public boolean isAvailable() {
        return currentRoute.get() == null;
    }
}
