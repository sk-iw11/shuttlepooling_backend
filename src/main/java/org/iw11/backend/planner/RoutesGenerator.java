package org.iw11.backend.planner;

import org.iw11.backend.model.BusDemand;
import org.iw11.backend.model.BusRoute;
import org.iw11.backend.model.BusStation;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.*;

public class RoutesGenerator {

    public Map<BusRoute, Integer> generateRoutes(Graph<BusStation, DefaultWeightedEdge> roadGraph,
                                                                   Map<BusDemand, Integer> demandsMap) {

        /* Step 1: Find all paths that satisfy demands */
        var paths = new HashMap<GraphPath<BusStation, DefaultWeightedEdge>, Integer>();

        var pathEstimator = new DijkstraShortestPath<>(roadGraph);

        for (var demand : demandsMap.entrySet()) {
            // Obtain path that satisfy current demand
            var path = pathEstimator.getPath(demand.getKey().getDepartue(), demand.getKey().getDestination());
            paths.put(path, 0);
        }

        /* Step 2: Calculate satisfaction score for each path */
        for (var pathEntry : new HashSet<>(paths.entrySet())) {
            int pathWeight = pathEntry.getValue();
            var vertices = getVertices(pathEntry.getKey());
            for (var demand : demandsMap.entrySet()) {
                int departureIndex = vertices.indexOf(demand.getKey().getDepartue());
                int destinationIndex = vertices.indexOf(demand.getKey().getDestination());
                if (departureIndex == -1 || destinationIndex == -1)
                    continue;
                if (departureIndex < destinationIndex)
                    pathWeight += demand.getValue();
            }
            paths.put(pathEntry.getKey(), pathWeight);
        }

        /* Step 3: Remove all sub-paths from obtained paths list */
        for (GraphPath<BusStation, DefaultWeightedEdge> pathI : new HashSet<>(paths.keySet())) {
            for (GraphPath<BusStation, DefaultWeightedEdge> pathJ : new HashSet<>(paths.keySet())) {
                if (pathI == pathJ)
                    continue;
                GraphPath<BusStation, DefaultWeightedEdge> pathMin, pathMax;
                var sizeI = pathI.getEdgeList().size();
                var sizeJ = pathJ.getEdgeList().size();
                if (sizeI == sizeJ) {
                    pathMin = pathI;
                    pathMax = pathJ;
                } else {
                    pathMax = sizeI > sizeJ ? pathI : pathJ;
                    pathMin = sizeI < sizeJ ? pathI : pathJ;
                }
                if (pathMax.getEdgeList().containsAll(pathMin.getEdgeList()))
                    paths.remove(pathMin);
            }
        }

        var routes = new HashMap<BusRoute, Integer>();
        paths.entrySet().forEach(entry -> routes.put(pathToRoute(entry.getKey()), entry.getValue()));

        return routes;
    }

    private Graph<BusStation, DefaultWeightedEdge> createDemandsGraph(Map<BusDemand, Integer> demandsMap) {
        var graph = new SimpleDirectedWeightedGraph<BusStation, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        for (var entry : demandsMap.entrySet()) {
            if (!graph.containsVertex(entry.getKey().getDepartue()))
                graph.addVertex(entry.getKey().getDepartue());
            if (!graph.containsVertex(entry.getKey().getDestination()))
                graph.addVertex(entry.getKey().getDestination());
            graph.setEdgeWeight(graph.addEdge(entry.getKey().getDepartue(), entry.getKey().getDestination()),
                    entry.getValue().doubleValue());
        }

        return graph;
    }

    private List<BusStation> getVertices(GraphPath<BusStation, DefaultWeightedEdge> path) {
        var vertices = new ArrayList<BusStation>();
        for (var edge : path.getEdgeList()) {
            if (!vertices.contains(path.getGraph().getEdgeSource(edge)))
                vertices.add(path.getGraph().getEdgeSource(edge));
            if (!vertices.contains(path.getGraph().getEdgeTarget(edge)))
                vertices.add(path.getGraph().getEdgeTarget(edge));
        }
        return vertices;
    }

    private BusRoute pathToRoute(GraphPath<BusStation, DefaultWeightedEdge> path) {
        return new BusRoute(getVertices(path));
    }
}
