package org.iw11.backend.planner;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.iw11.backend.model.BusDemand;
import org.iw11.backend.model.BusRoute;
import org.iw11.backend.model.GeoCoordinates;
import org.iw11.backend.util.GraphIoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BusTracker {

    private static final Logger LOG = LoggerFactory.getLogger(BusTracker.class);

    private static final String CONFIG_PATH = "/config/buses.json";

    private Map<String, BusState> buses = new ConcurrentHashMap<>();

    public BusTracker() throws IOException {
        var config = parseConfig(CONFIG_PATH);
        config.getBuses().forEach(bus -> buses.put(bus, new BusState()));
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
        var routeIterator = routes.iterator();
        buses.entrySet().stream()
                .filter(bus -> bus.getValue().isAvailable())
                .limit(routes.size())
                .forEach(bus -> bus.getValue().setCurrentRoute(routeIterator.next()));
    }

    private Config parseConfig(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        var reader = new BufferedReader(new InputStreamReader(GraphIoUtil.class.getResourceAsStream(filePath)));
        Config config = objectMapper.readValue(reader, Config.class);
        return config;
    }

    private static class Config {
        private String[] buses;

        public Config() { }

        public List<String> getBuses() {
            return buses == null ? new ArrayList<>() : Arrays.asList(buses);
        }
    }
}
