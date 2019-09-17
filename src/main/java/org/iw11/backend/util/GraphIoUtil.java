package org.iw11.backend.util;

import org.iw11.backend.model.BusStation;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.DOTImporter;
import org.jgrapht.io.ImportException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class GraphIoUtil {

    private GraphIoUtil() { }

    public static Graph<BusStation, DefaultEdge> importFromResources(String filePath) throws IOException {
        var graph = new DefaultDirectedGraph<BusStation, DefaultEdge>(DefaultEdge.class);
        var importer = new DOTImporter<>((label, attrs) -> new BusStation(label),
                ((from, to, label, attrs) -> new DefaultEdge()));
        var reader = new BufferedReader(new InputStreamReader(GraphIoUtil.class.getResourceAsStream(filePath)));
        try {
            importer.importGraph(graph, reader);
        } catch (ImportException e) {
            throw new IOException(e);
        }
        return graph;
    }
}
