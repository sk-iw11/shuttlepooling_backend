package org.iw11.backend.planner;

import org.iw11.backend.map.RoadMapService;
import org.iw11.backend.model.BusDemand;
import org.iw11.backend.model.BusRoute;
import org.iw11.backend.model.BusStation;
import org.iw11.backend.util.GraphIoUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RoutesGeneratorTest {

    private static final String GRAPH_FILE_PATH = "/map/road_map.graph";

    private static final BusStation STATION_SKOLTECH = new BusStation("skoltech");
    private static final BusStation STATION_TECHNOPARK = new BusStation("technopark");
    private static final BusStation STATION_NOBEL_STREET = new BusStation("nobel_street");
    private static final BusStation STATION_USADBA = new BusStation("usadba");
    private static final BusStation STATION_PARKING = new BusStation("parking");

    private Graph<BusStation, DefaultEdge> roadMap;

    private RoutesGenerator routesGenerator = new RoutesGenerator();

    @Before
    public void init() throws IOException {
        roadMap = GraphIoUtil.importFromResources(GRAPH_FILE_PATH);
    }

    @Test
    public void lunchScenarioTest() throws Exception {
        System.out.println("Simulating lunch scenario:");
        var demands = new HashMap<BusDemand, Integer>();
        demands.put(new BusDemand(STATION_SKOLTECH, STATION_TECHNOPARK), 10);
        demands.put(new BusDemand(STATION_TECHNOPARK, STATION_SKOLTECH), 15);
        demands.put(new BusDemand(STATION_PARKING, STATION_TECHNOPARK), 7);
        demands.put(new BusDemand(STATION_PARKING, STATION_SKOLTECH), 10);
        demands.put(new BusDemand(STATION_NOBEL_STREET, STATION_TECHNOPARK), 10);

        var routes = routesGenerator.generateRoutes(roadMap, demands);
        printRoutes(routes);
        System.out.println("\n");
    }

    @Test
    public void eveningScenarioTest() throws Exception {
        System.out.println("Simulating evening scenario:");
        var demands = new HashMap<BusDemand, Integer>();

        demands.put(new BusDemand(STATION_SKOLTECH, STATION_TECHNOPARK), 10);
        demands.put(new BusDemand(STATION_SKOLTECH, STATION_USADBA), 5);
        demands.put(new BusDemand(STATION_SKOLTECH, STATION_PARKING), 7);

        demands.put(new BusDemand(STATION_TECHNOPARK, STATION_PARKING), 15);
        demands.put(new BusDemand(STATION_TECHNOPARK, STATION_USADBA), 15);

        demands.put(new BusDemand(STATION_NOBEL_STREET, STATION_TECHNOPARK), 11);
        demands.put(new BusDemand(STATION_NOBEL_STREET, STATION_USADBA), 8);
        demands.put(new BusDemand(STATION_NOBEL_STREET, STATION_PARKING), 12);

        var routes = routesGenerator.generateRoutes(roadMap, demands);
        printRoutes(routes);
        System.out.println("\n");
    }

    private void printRoutes(Map<BusRoute, Integer> routes) {
        routes.entrySet().forEach(entry -> System.out.println(entry.getKey().toString() + " " + entry.getValue().toString()));
    }
}
