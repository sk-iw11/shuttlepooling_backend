package org.iw11.backend.map;

import org.iw11.backend.model.BusStation;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.*;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class RoadMapService {

    private static final String GRAPH_FILE_PATH = "/map/road_map.graph";

    private Graph<BusStation, DefaultEdge> roadMap = new DefaultDirectedGraph<>(DefaultEdge.class);

    public RoadMapService() throws IOException {
        var importer = new DOTImporter<>((label, attrs) -> new BusStation(label),
                ((from, to, label, attrs) -> new DefaultEdge()));
        var reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(GRAPH_FILE_PATH)));
        try {
            importer.importGraph(roadMap, reader);
        } catch (ImportException e) {
            throw new IOException(e);
        }
    }

    public Graph<BusStation, DefaultEdge> getRoadMap() {
        return roadMap;
    }

}
