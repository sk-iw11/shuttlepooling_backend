package org.iw11.backend.map;

import org.iw11.backend.model.BusStation;
import org.iw11.backend.util.GraphIoUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RoadMapService {

    private static final String GRAPH_FILE_PATH = "/map/road_map.graph";

    private Graph<BusStation, DefaultWeightedEdge> roadMap;

    public RoadMapService() throws IOException {
        roadMap = GraphIoUtil.importRoadMapFromResources(GRAPH_FILE_PATH);
    }

    public Graph<BusStation, DefaultWeightedEdge> getRoadMap() {
        return roadMap;
    }

}
