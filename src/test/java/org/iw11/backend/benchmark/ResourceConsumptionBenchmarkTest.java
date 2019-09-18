package org.iw11.backend.benchmark;

import org.iw11.backend.model.BusDemand;
import org.iw11.backend.model.BusStation;
import org.iw11.backend.planner.RoutesGenerator;
import org.iw11.backend.util.GraphIoUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Ignore
public class ResourceConsumptionBenchmarkTest {

    private static final String GRAPH_FILE_PATH = "/map/road_map.graph";

    private Graph<BusStation, DefaultEdge> roadMap;

    private RoutesGenerator routesGenerator = new RoutesGenerator();

    @Before
    public void init() throws IOException {
        roadMap = GraphIoUtil.importFromResources(GRAPH_FILE_PATH);
    }

    @Test
    public void testResourceConsumptionCalculation() {
        System.out.println("Ok");
    }

    private Map<BusDemand, Integer> generateDemands() {
        var demands = new HashMap<BusDemand, Integer>();

        return demands;
    }

    private int generateDemandsNumber(DemandsRate priority) {
        switch (priority) {
            case LOW:
                return ThreadLocalRandom.current().nextInt(1, 4);
            case MEDIUM:
                return ThreadLocalRandom.current().nextInt(5, 8);
            case HIGH:
                return ThreadLocalRandom.current().nextInt(8, 15);
            default:
                return 0;
        }
    }

    private static enum DemandsRate {
        NONE,
        LOW,
        MEDIUM,
        HIGH
    }
}
