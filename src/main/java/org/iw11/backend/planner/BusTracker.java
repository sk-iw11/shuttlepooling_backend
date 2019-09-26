package org.iw11.backend.planner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.iw11.backend.config.BusesConfigService;
import org.iw11.backend.model.BusDemand;
import org.iw11.backend.model.BusRoute;
import org.iw11.backend.model.GeoCoordinates;
import org.iw11.backend.util.CoordinatesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BusTracker {

    private static final Logger LOG = LoggerFactory.getLogger(BusTracker.class);

    private Map<String, BusState> buses = new ConcurrentHashMap<>();

    private final BusesConfigService busesConfigService;

    @Autowired
    public BusTracker(BusesConfigService busesConfigService) throws IOException {
        this.busesConfigService = busesConfigService;
        this.busesConfigService.getBuses().forEach(bus -> buses.put(bus, new BusState()));
    }

    public void updateBusLocation(String bus, GeoCoordinates location) {
        BusState state = buses.get(bus);
        if (state != null) {
            LOG.info("Bus " + bus + " " + location.toString());
            state.setCurrentLocation(location);
        }
    }

    public Optional<GeoCoordinates> getBusLocation(String bus) {
        return buses.get(bus).getCurrentLocation();
    }

    public Optional<BusRoute> getBusRoute(String bus) {
        return buses.get(bus).getCurrentRoute();
    }

    public void completeBusRoute(String bus) {
        buses.get(bus).setCurrentRoute(null);
    }

    public Optional<ImmutablePair<String, BusRoute>> getBusForDemand(BusDemand demand) {
        return buses.entrySet().stream()
                .filter(entry -> entry.getValue().getCurrentRoute().isPresent())
                .map(entry -> ImmutablePair.of(entry.getKey(), entry.getValue().getCurrentRoute().get()))
                .filter(bus -> bus.getRight().canSatisfyDemand(demand))
                .findAny();
    }

    public void assignRoutes(List<BusRoute> routes) {
        if (routes.isEmpty())
            return;

        routes.forEach(route -> {
            var cl = buses.entrySet().stream()
                    .filter(bus -> bus.getValue().isAvailable() && bus.getValue().getCurrentLocation().isPresent())
                    .min((busA, busB) ->
                        compareDistances(busA.getValue().getCurrentLocation(),
                                busB.getValue().getCurrentLocation(), route.getStations().get(0).getLocation()));
            if (cl.isPresent()) {
                cl.get().getValue().setCurrentRoute(route);
            }
        });
    }

    private int compareDistances(Optional<GeoCoordinates> pointA, Optional<GeoCoordinates> pointB, GeoCoordinates pointC) {
        if (pointA.isEmpty())
            return 1;
        if (pointB.isEmpty())
            return -1;
        return CoordinatesUtil.getDistance(pointA.get(), pointC) > CoordinatesUtil.getDistance(pointB.get(), pointC) ? 1 : -1;
    }
}
