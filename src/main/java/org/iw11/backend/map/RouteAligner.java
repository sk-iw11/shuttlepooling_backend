package org.iw11.backend.map;

import org.iw11.backend.model.BusRoute;
import org.iw11.backend.model.BusStation;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RouteAligner {

    public BusRoute alignRoute(BusRoute route, Graph<BusStation, DefaultWeightedEdge> stationLocationsMap,
                               boolean merge) {
        var modifiedStations = new ArrayList<BusStation>();
        var stations = stationLocationsMap.vertexSet();

        var it = route.getStations().iterator();
        var currentStation = it.next();
        while (it.hasNext()) {
            var nextStation = it.next();
            BusStation finalCurrentStation = currentStation;
            var currentSimilar = stations.stream().filter(station -> station.getName().startsWith(finalCurrentStation.getName()))
                    .collect(Collectors.toList());
            var nextSimilar = stations.stream().filter(station -> station.getName().startsWith(nextStation.getName()))
                    .collect(Collectors.toList());

            var currentIt = currentSimilar.iterator();
            while (currentIt.hasNext()) {
                var stationA = currentIt.next();
                var nextIt = nextSimilar.iterator();
                while (nextIt.hasNext()) {
                    var stationB = nextIt.next();
                    if (stationLocationsMap.getEdge(stationA, stationB) == null)
                        continue;

                    if (merge) {
                        var mergedA = mergeStation(stationA);
                        if (!modifiedStations.contains(mergedA))
                            modifiedStations.add(mergedA);

                        var mergedB = mergeStation(stationB);
                        if (!modifiedStations.contains(mergedB))
                            modifiedStations.add(mergedB);
                    } else {
                        if (!modifiedStations.contains(stationA))
                            modifiedStations.add(stationA);
                        if (!modifiedStations.contains(stationB))
                            modifiedStations.add(stationB);
                    }

                    currentStation = nextStation;
                }
            }
        }

        return new BusRoute(modifiedStations);
    }

    private BusStation mergeStation(BusStation station) {
        var stationName = station.getName();
        if (stationName.contains("_")) {
            stationName = stationName.substring(0, stationName.lastIndexOf('_'));
        }
        return new BusStation(stationName, station.getLocation());
    }
}
