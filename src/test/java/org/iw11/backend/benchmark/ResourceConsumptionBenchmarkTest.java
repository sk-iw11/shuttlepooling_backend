package org.iw11.backend.benchmark;

import org.iw11.backend.Stations;
import org.iw11.backend.model.BusStation;
import org.iw11.backend.util.GraphIoUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

@Ignore
public class ResourceConsumptionBenchmarkTest {

    private static final String GRAPH_FILE_PATH = "/map/road_map.graph";

    private Graph<BusStation, DefaultWeightedEdge> roadMap;

    @Before
    public void init() throws IOException {
        roadMap = GraphIoUtil.importRoadMapFromResources(GRAPH_FILE_PATH);
        // TODO: Import weights from file
        roadMap.setEdgeWeight(Stations.SKOLTECH, Stations.TECHNOPARK, 3.0);
        roadMap.setEdgeWeight(Stations.TECHNOPARK, Stations.SKOLTECH, 3.0);
        roadMap.setEdgeWeight(Stations.TECHNOPARK, Stations.NOBEL_STREET, 2.0);
        roadMap.setEdgeWeight(Stations.NOBEL_STREET, Stations.TECHNOPARK, 2.0);
        roadMap.setEdgeWeight(Stations.TECHNOPARK, Stations.USADBA, 1.0);
        roadMap.setEdgeWeight(Stations.USADBA, Stations.TECHNOPARK, 1.0);
        roadMap.setEdgeWeight(Stations.TECHNOPARK, Stations.PARKING, 1.2);
        roadMap.setEdgeWeight(Stations.PARKING, Stations.TECHNOPARK, 1.2);
    }

    @Test
    public void testWorkdayConsumption() {
        new WorkdaySimulationScenario(roadMap).run();
    }

    @Test
    public void testWeekendConsumption() {
        new WeekendSimulationScenario(roadMap).run();
    }
}
