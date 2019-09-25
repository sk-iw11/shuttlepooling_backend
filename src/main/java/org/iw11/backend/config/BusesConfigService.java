package org.iw11.backend.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class BusesConfigService {

    private static final String CONFIG_PATH = "/config/buses.json";

    private Map<String, BusInfo> busesInfo;

    public BusesConfigService() throws IOException {
        busesInfo = parseConfig(CONFIG_PATH);
    }

    public List<String> getBuses() {
        return new ArrayList<>(busesInfo.keySet());
    }

    public Optional<String> getBusPassword(String bus) {
        return Optional.ofNullable(busesInfo.get(bus).getPassword());
    }

    private Map<String, BusInfo> parseConfig(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        var reader = new BufferedReader(new InputStreamReader(BusesConfigService.class.getResourceAsStream(filePath)));
        var config = objectMapper.readValue(reader, BusConfig.class);
        Map<String, BusInfo> map = new HashMap<>();
        config.getBuses().stream().forEach(entity -> map.put(entity.getName(), entity));
        return map;
    }

    public static class BusInfo {

        @JsonProperty("name")
        private String name;

        @JsonProperty("password")
        private String password;

        public BusInfo() { }

        public BusInfo(String name, String password) {
            this.name = name;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public String getPassword() {
            return password;
        }
    }

    public static class BusConfig {

        @JsonProperty("buses")
        private BusInfo[] buses;

        public BusConfig() { }

        public BusConfig(BusInfo[] buses) {
            this.buses = buses;
        }

        public List<BusInfo> getBuses() {
            return Arrays.asList(buses);
        }
    }
}
