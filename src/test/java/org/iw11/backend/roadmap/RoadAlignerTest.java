package org.iw11.backend.roadmap;

import org.iw11.backend.Stations;
import org.iw11.backend.map.RouteAligner;
import org.iw11.backend.model.BusRoute;
import org.iw11.backend.model.BusStation;
import org.iw11.backend.util.GraphIoUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

@Ignore
public class RoadAlignerTest {

    private static final String LOCATIONS_FILE_PATH = "/map/bus_stations_map.graph";

    private Graph<BusStation, DefaultWeightedEdge> stationLocationsMap;

    private RouteAligner routeAligner = new RouteAligner();

    @Before
    public void init() throws IOException {
        stationLocationsMap = GraphIoUtil.importRoadMapFromResources(LOCATIONS_FILE_PATH);
    }

    @Test
    public void testRouteAlignment() {
        var stations = new ArrayList<BusStation>();
        stations.add(Stations.PARKING);
        stations.add(Stations.TECHNOPARK);
        stations.add(Stations.NOBEL_STREET);
        var route = new BusRoute(stations);
        System.out.println("Initial route: " + route.toString());

        var alignedRoute = routeAligner.alignRoute(route, stationLocationsMap, true);
        System.out.println("Aligned route: " + alignedRoute.toString());
    }

}
