package org.iw11.backend.util;

import org.iw11.backend.model.BusStation;
import org.iw11.backend.model.GeoCoordinates;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.io.Attribute;
import org.jgrapht.io.DOTImporter;
import org.jgrapht.io.ImportException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public final class GraphIoUtil {

    private GraphIoUtil() { }

    public static Graph<BusStation, DefaultWeightedEdge> importRoadMapFromResources(String filePath) throws IOException {
        var graph = new DefaultDirectedWeightedGraph<BusStation, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        var importer = new DOTImporter<>(GraphIoUtil::parseStation,
                ((from, to, label, attrs) -> new DefaultWeightedEdge()));
        var reader = new BufferedReader(new InputStreamReader(GraphIoUtil.class.getResourceAsStream(filePath)));
        try {
            importer.importGraph(graph, reader);
        } catch (ImportException e) {
            throw new IOException(e);
        }
        return graph;
    }

    private static BusStation parseStation(String label, Map<String, Attribute> attr) {
        var parts = attr.get("label").getValue().split(";");
        return new BusStation(label, new GeoCoordinates(Double.valueOf(parts[0]), Double.valueOf(parts[1])));
    }

}
