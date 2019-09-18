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
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Ignore
public class ResourceConsumptionBenchmarkTest {

    private static final String GRAPH_FILE_PATH = "/map/road_map.graph";

    private static final LocalTime TIME_7 = LocalTime.of(7, 0);
    private static final LocalTime TIME_10 = LocalTime.of(10, 0);
    private static final LocalTime TIME_12 = LocalTime.of(12, 0);
    private static final LocalTime TIME_14 = LocalTime.of(14, 0);
    private static final LocalTime TIME_17 = LocalTime.of(17, 0);
    private static final LocalTime TIME_21 = LocalTime.of(21, 0);

    private Graph<BusStation, DefaultEdge> roadMap;

    private RoutesGenerator routesGenerator = new RoutesGenerator();

    @Before
    public void init() throws IOException {
        roadMap = GraphIoUtil.importFromResources(GRAPH_FILE_PATH);
    }

    @Test
    public void testResourceConsumptionCalculation() {

    }

    private Map<BusDemand, Integer> generateDemands(LocalTime time) {
        var demands = new HashMap<BusDemand, Integer>();

        // From 7:00 to 10:00
        if (time.compareTo(TIME_7) > 0 && time.compareTo(TIME_10) < 0) {
            System.out.println("7");
        }

        // From 10:00 to 12:00
        else if (time.compareTo(TIME_10) > 0 && time.compareTo(TIME_12) < 0) {
            System.out.println("10");
        }

        // From 12:00 to 14:00
        else if (time.compareTo(TIME_12) > 0 && time.compareTo(TIME_14) < 0) {
            System.out.println("12");
        }

        // From 14:00 to 17:00
        else if (time.compareTo(TIME_14) > 0 && time.compareTo(TIME_17) < 0) {
            System.out.println("14");
        }

        // From 17:00 to 21:00
        else if (time.compareTo(TIME_17) > 0 && time.compareTo(TIME_21) < 0) {
            System.out.println("17");
        }

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
