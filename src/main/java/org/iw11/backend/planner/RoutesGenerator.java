package org.iw11.backend.planner;

import org.iw11.backend.model.BusDemand;
import org.iw11.backend.model.BusRoute;
import org.iw11.backend.model.BusStation;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.*;

public class RoutesGenerator {

    public Map<BusRoute, Integer> generateRoutes(Graph<BusStation, DefaultEdge> roadGraph,
                                                                   Map<BusDemand, Integer> demandsMap) {

        /* Step 1: Generate demands graph */
        var demandsGraph = createDemandsGraph(demandsMap);

        /* Step 2: Find all paths that satisfy demands */
        var paths = new HashMap<GraphPath<BusStation, DefaultEdge>, Double>();

        var pathEstimator = new DijkstraShortestPath<>(roadGraph);

        for (DefaultEdge demand : new HashSet<>(demandsGraph.edgeSet())) {
            // Obtain path that satisfy current demand
            var path = pathEstimator.getPath(demandsGraph.getEdgeSource(demand), demandsGraph.getEdgeTarget(demand));

            // Calculate satisfaction score for that path
            double pathWeight = 0;
            for (DefaultEdge pathEdge : path.getEdgeList()) {
                var demandEdge = demandsGraph.getEdge(
                        path.getGraph().getEdgeSource(pathEdge), path.getGraph().getEdgeTarget(pathEdge));
                if (demandEdge == null)
                    continue;
                pathWeight += demandsGraph.getEdgeWeight(demandEdge);
            }

            paths.put(path, pathWeight);
        }

        /* Step 3: Remove all sub-paths from obtained paths list */
        for (GraphPath<BusStation, DefaultEdge> pathI : new HashSet<>(paths.keySet())) {
            for (GraphPath<BusStation, DefaultEdge> pathJ : new HashSet<>(paths.keySet())) {
                if (pathI == pathJ)
                    continue;
                var pathMax = pathI.getEdgeList().size() > pathJ.getEdgeList().size() ? pathI : pathJ;
                var pathMin = pathI.getEdgeList().size() < pathJ.getEdgeList().size() ? pathI : pathJ;
                if (pathMax.getEdgeList().containsAll(pathMin.getEdgeList()))
                    paths.remove(pathMin);
            }
        }

        var routes = new HashMap<BusRoute, Integer>();
        paths.entrySet().forEach(entry -> routes.put(pathToRoute(entry.getKey()), entry.getValue().intValue()));

        return routes;
    }

    private Graph<BusStation, DefaultEdge> createDemandsGraph(Map<BusDemand, Integer> demandsMap) {
        var graph = new SimpleDirectedWeightedGraph<BusStation, DefaultEdge>(DefaultEdge.class);

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

    private BusRoute pathToRoute(GraphPath<BusStation, DefaultEdge> path) {
        List<BusStation> vertices = new ArrayList<>();
        for (var edge : path.getEdgeList()) {
            if (!vertices.contains(path.getGraph().getEdgeSource(edge)))
                vertices.add(path.getGraph().getEdgeSource(edge));
            if (!vertices.contains(path.getGraph().getEdgeTarget(edge)))
                vertices.add(path.getGraph().getEdgeTarget(edge));
        }
        return new BusRoute(vertices);
    }
}
