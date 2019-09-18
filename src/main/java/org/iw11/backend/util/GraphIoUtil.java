package org.iw11.backend.util;

import org.iw11.backend.model.BusStation;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.io.DOTImporter;
import org.jgrapht.io.ImportException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class GraphIoUtil {

    private GraphIoUtil() { }

    public static Graph<BusStation, DefaultWeightedEdge> importRoadMapFromResources(String filePath) throws IOException {
        var graph = new DefaultDirectedWeightedGraph<BusStation, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        var importer = new DOTImporter<>((label, attrs) -> new BusStation(label),
                ((from, to, label, attrs) -> new DefaultWeightedEdge()));
        var reader = new BufferedReader(new InputStreamReader(GraphIoUtil.class.getResourceAsStream(filePath)));
        try {
            importer.importGraph(graph, reader);
        } catch (ImportException e) {
            throw new IOException(e);
        }
        return graph;
    }

}
