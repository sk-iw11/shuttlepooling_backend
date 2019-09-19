package org.iw11.backend.planner;

import org.iw11.backend.model.BusRoute;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class BusState {

    private AtomicReference<BusRoute> currentRoute = new AtomicReference<>();

    public BusState() {
        currentRoute.set(null);
    }

    public Optional<BusRoute> getCurrentRoute() {
        return Optional.of(currentRoute.get());
    }

    public void setCurrentRoute(BusRoute route) {
        currentRoute.set(route);
    }

    public boolean isAvailable() {
        return currentRoute.get() == null;
    }
}
