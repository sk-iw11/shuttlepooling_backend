package org.iw11.backend.planner;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        config.getBuses().forEach(bus -> buses.put(bus, BusState.FREE));
    }

    private Config parseConfig(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        var reader = new BufferedReader(new InputStreamReader(GraphIoUtil.class.getResourceAsStream(filePath)));
        Config config = objectMapper.readValue(reader, Config.class);
        return config;
    }

    private static enum BusState {
        BUSY,
        FREE
    }

    private static class Config {
        private String[] buses;

        public Config() { }

        public List<String> getBuses() {
            return buses == null ? new ArrayList<>() : Arrays.asList(buses);
        }
    }
}
