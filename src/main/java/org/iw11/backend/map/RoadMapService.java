package org.iw11.backend.map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.iw11.backend.model.BusStation;
import org.iw11.backend.util.GraphIoUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RoadMapService {

    private static final String ROADMAP_FILE_PATH = "/map/road_map.graph";

    private static final String LOCATIONS_FILE_PATH = "/map/bus_stations_map.graph";

    private Graph<BusStation, DefaultWeightedEdge> roadMap;

    private Graph<BusStation, DefaultWeightedEdge> stationLocationsMap;

    private List<BusStation> stations;

    public RoadMapService() throws IOException {
        roadMap = GraphIoUtil.importRoadMapFromResources(ROADMAP_FILE_PATH);
        stationLocationsMap = GraphIoUtil.importRoadMapFromResources(LOCATIONS_FILE_PATH);
        stations = List.copyOf(roadMap.vertexSet());
    }

    public Optional<BusStation> getStation(String station) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(station));
        return stations.stream().filter(entry -> entry.getName().equals(station)).findFirst();
    }

    public Graph<BusStation, DefaultWeightedEdge> getRoadMap() {
        return roadMap;
    }

    public Graph<BusStation, DefaultWeightedEdge> getBusLocationsMap() {
        return stationLocationsMap;
    }

}
