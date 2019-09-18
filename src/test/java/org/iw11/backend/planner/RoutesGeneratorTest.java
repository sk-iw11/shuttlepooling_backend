package org.iw11.backend.planner;

import org.iw11.backend.Stations;
import org.iw11.backend.model.BusDemand;
import org.iw11.backend.model.BusRoute;
import org.iw11.backend.model.BusStation;
import org.iw11.backend.util.GraphIoUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Ignore
public class RoutesGeneratorTest {

    private static final String GRAPH_FILE_PATH = "/map/road_map.graph";

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
        demands.put(new BusDemand(Stations.SKOLTECH, Stations.TECHNOPARK), 10);
        demands.put(new BusDemand(Stations.TECHNOPARK, Stations.SKOLTECH), 15);
        demands.put(new BusDemand(Stations.PARKING, Stations.TECHNOPARK), 7);
        demands.put(new BusDemand(Stations.PARKING, Stations.SKOLTECH), 10);
        demands.put(new BusDemand(Stations.NOBEL_STREET, Stations.TECHNOPARK), 10);

        var routes = routesGenerator.generateRoutes(roadMap, demands);
        printRoutes(routes);
        System.out.println("\n");
    }

    @Test
    public void eveningScenarioTest() throws Exception {
        System.out.println("Simulating evening scenario:");
        var demands = new HashMap<BusDemand, Integer>();

        demands.put(new BusDemand(Stations.SKOLTECH, Stations.TECHNOPARK), 10);
        demands.put(new BusDemand(Stations.SKOLTECH, Stations.USADBA), 5);
        demands.put(new BusDemand(Stations.SKOLTECH, Stations.PARKING), 7);

        demands.put(new BusDemand(Stations.TECHNOPARK, Stations.PARKING), 15);
        demands.put(new BusDemand(Stations.TECHNOPARK, Stations.USADBA), 15);

        demands.put(new BusDemand(Stations.NOBEL_STREET, Stations.TECHNOPARK), 11);
        demands.put(new BusDemand(Stations.NOBEL_STREET, Stations.USADBA), 8);
        demands.put(new BusDemand(Stations.NOBEL_STREET, Stations.PARKING), 12);

        var routes = routesGenerator.generateRoutes(roadMap, demands);
        printRoutes(routes);
        System.out.println("\n");
    }

    private void printRoutes(Map<BusRoute, Integer> routes) {
        routes.entrySet().forEach(entry -> System.out.println(entry.getKey().toString() + " " + entry.getValue().toString()));
    }
}
