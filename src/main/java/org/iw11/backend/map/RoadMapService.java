package org.iw11.backend.map;

import org.iw11.backend.model.BusStation;
import org.iw11.backend.util.GraphIoUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RoadMapService {

    private static final String GRAPH_FILE_PATH = "/map/road_map.graph";

    private Graph<BusStation, DefaultEdge> roadMap;

    public RoadMapService() throws IOException {
        roadMap = GraphIoUtil.importFromResources(GRAPH_FILE_PATH);
    }

    public Graph<BusStation, DefaultEdge> getRoadMap() {
        return roadMap;
    }

}
