package org.iw11.backend.benchmark;

import org.iw11.backend.Stations;
import org.iw11.backend.model.BusDemand;
import org.iw11.backend.model.BusRoute;
import org.iw11.backend.model.BusStation;
import org.iw11.backend.planner.RoutesGenerator;
import org.iw11.backend.util.GraphIoUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Ignore
public class ResourceConsumptionBenchmarkTest {

    private static final String GRAPH_FILE_PATH = "/map/road_map.graph";

    private static final LocalTime TIME_7 = LocalTime.of(7, 0);
    private static final LocalTime TIME_10 = LocalTime.of(10, 0);
    private static final LocalTime TIME_12 = LocalTime.of(12, 0);
    private static final LocalTime TIME_14 = LocalTime.of(14, 0);
    private static final LocalTime TIME_17 = LocalTime.of(17, 0);
    private static final LocalTime TIME_21 = LocalTime.of(21, 0);

    private static final double AVERAGE_MIN_PER_KM = 2.86; // min/km

    private Graph<BusStation, DefaultWeightedEdge> roadMap;

    private RoutesGenerator routesGenerator = new RoutesGenerator();

    @Before
    public void init() throws IOException {
        roadMap = GraphIoUtil.importRoadMapFromResources(GRAPH_FILE_PATH);
        // TODO: Import weights from file
        roadMap.setEdgeWeight(Stations.SKOLTECH, Stations.TECHNOPARK, 3.0);
        roadMap.setEdgeWeight(Stations.TECHNOPARK, Stations.SKOLTECH, 3.0);
        roadMap.setEdgeWeight(Stations.TECHNOPARK, Stations.NOBEL_STREET, 2.0);
        roadMap.setEdgeWeight(Stations.NOBEL_STREET, Stations.TECHNOPARK, 2.0);
        roadMap.setEdgeWeight(Stations.TECHNOPARK, Stations.USADBA, 1.0);
        roadMap.setEdgeWeight(Stations.USADBA, Stations.TECHNOPARK, 1.0);
        roadMap.setEdgeWeight(Stations.TECHNOPARK, Stations.PARKING, 1.2);
        roadMap.setEdgeWeight(Stations.PARKING, Stations.TECHNOPARK, 1.2);
    }

    @Test
    public void testResourceConsumptionCalculation() {
        double totalOvercomeDistance = 0.0;
        int totalRides = 0;
        int peopleCount = 0;

        var currentTime = LocalTime.of(7, 0);

        while (currentTime.compareTo(TIME_21) < 0) {
            System.out.println(currentTime);

            var demands = generateDemands(currentTime);
            var routes = routesGenerator.generateRoutes(roadMap, demands);
            var route = routes.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
                    .get().getKey();
            double length = calculateRouteLength(route);
            int satisfiedDemands = calculateSatisfiedDemands(route, demands);

            totalOvercomeDistance += length;
            totalRides++;
            peopleCount += satisfiedDemands;

            double routeTime = length * AVERAGE_MIN_PER_KM;
            currentTime = currentTime.plusMinutes((long)routeTime);
        }

        System.out.println("\n\n\n###### RESULTS ######");
        System.out.println("Total overcome distance: " + totalOvercomeDistance);
        System.out.println("Total completed rides: " + totalRides);
        System.out.println("Total people transfered: " + peopleCount);
    }

    private Map<BusDemand, Integer> generateDemands(LocalTime time) {
        var demands = new HashMap<BusDemand, Integer>();

        // From 7:00 to 10:00
        if (time.compareTo(TIME_7) >= 0 && time.compareTo(TIME_10) < 0) {
            demands.put(new BusDemand(Stations.USADBA, Stations.TECHNOPARK), generateDemandsNumber(DemandsRate.HIGH));
            demands.put(new BusDemand(Stations.PARKING, Stations.TECHNOPARK), generateDemandsNumber(DemandsRate.HIGH));
            demands.put(new BusDemand(Stations.USADBA, Stations.SKOLTECH), generateDemandsNumber(DemandsRate.MEDIUM));
            demands.put(new BusDemand(Stations.PARKING, Stations.SKOLTECH), generateDemandsNumber(DemandsRate.MEDIUM));
            demands.put(new BusDemand(Stations.USADBA, Stations.NOBEL_STREET), generateDemandsNumber(DemandsRate.MEDIUM));
            demands.put(new BusDemand(Stations.PARKING, Stations.NOBEL_STREET), generateDemandsNumber(DemandsRate.MEDIUM));
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.SKOLTECH), generateDemandsNumber(DemandsRate.HIGH));
        }

        // From 10:00 to 12:00
        else if (time.compareTo(TIME_10) >= 0 && time.compareTo(TIME_12) < 0) {
            demands.put(new BusDemand(Stations.NOBEL_STREET, Stations.TECHNOPARK), generateDemandsNumber(DemandsRate.LOW));
            demands.put(new BusDemand(Stations.NOBEL_STREET, Stations.SKOLTECH), generateDemandsNumber(DemandsRate.MEDIUM));
        }

        // From 12:00 to 14:00
        else if (time.compareTo(TIME_12) >= 0 && time.compareTo(TIME_14) < 0) {
            demands.put(new BusDemand(Stations.SKOLTECH, Stations.TECHNOPARK), generateDemandsNumber(DemandsRate.HIGH));
            demands.put(new BusDemand(Stations.NOBEL_STREET, Stations.TECHNOPARK), generateDemandsNumber(DemandsRate.HIGH));
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.SKOLTECH), generateDemandsNumber(DemandsRate.MEDIUM));
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.NOBEL_STREET), generateDemandsNumber(DemandsRate.HIGH));
        }

        // From 14:00 to 17:00
        else if (time.compareTo(TIME_14) >= 0 && time.compareTo(TIME_17) < 0) {
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.NOBEL_STREET), generateDemandsNumber(DemandsRate.LOW));
            demands.put(new BusDemand(Stations.SKOLTECH, Stations.NOBEL_STREET), generateDemandsNumber(DemandsRate.MEDIUM));
            demands.put(new BusDemand(Stations.SKOLTECH, Stations.PARKING), generateDemandsNumber(DemandsRate.LOW));
            demands.put(new BusDemand(Stations.SKOLTECH, Stations.USADBA), generateDemandsNumber(DemandsRate.LOW));
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.PARKING), generateDemandsNumber(DemandsRate.LOW));
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.USADBA), generateDemandsNumber(DemandsRate.LOW));
        }

        // From 17:00 to 21:00
        else if (time.compareTo(TIME_17) >= 0 && time.compareTo(TIME_21) < 0) {
            demands.put(new BusDemand(Stations.SKOLTECH, Stations.USADBA), generateDemandsNumber(DemandsRate.HIGH));
            demands.put(new BusDemand(Stations.SKOLTECH, Stations.PARKING), generateDemandsNumber(DemandsRate.MEDIUM));
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.USADBA), generateDemandsNumber(DemandsRate.HIGH));
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.PARKING), generateDemandsNumber(DemandsRate.HIGH));
            demands.put(new BusDemand(Stations.NOBEL_STREET, Stations.USADBA), generateDemandsNumber(DemandsRate.HIGH));
            demands.put(new BusDemand(Stations.NOBEL_STREET, Stations.PARKING), generateDemandsNumber(DemandsRate.HIGH));
        }

        return demands;
    }

    private int generateDemandsNumber(DemandsRate priority) {
        switch (priority) {
            case LOW:
                return ThreadLocalRandom.current().nextInt(1, 4);
            case MEDIUM:
                return ThreadLocalRandom.current().nextInt(5, 8);
            case HIGH:
                return ThreadLocalRandom.current().nextInt(8, 15);
            default:
                return 0;
        }
    }

    private double calculateRouteLength(BusRoute route) {
        double length = 0.0;
        int i = 0;
        var it = route.getStations().iterator();
        BusStation currentStation = it.next();
        BusStation nextStation = null;
        while (it.hasNext()) {
            nextStation = it.next();
            length += roadMap.getEdgeWeight(roadMap.getEdge(currentStation, nextStation));
            currentStation = nextStation;
        }
        return length;
    }

    private int calculateSatisfiedDemands(BusRoute route, Map<BusDemand, Integer> demands) {
        int satisfied = 0;
        for (var demand : demands.entrySet()) {
            int departureIndex = route.getStations().indexOf(demand.getKey().getDepartue());
            int destinationIndex = route.getStations().indexOf(demand.getKey().getDestination());
            if (departureIndex == -1 || destinationIndex == -1)
                continue;
            if (departureIndex > destinationIndex)
                continue;
            satisfied += demand.getValue();
        }
        return satisfied;
    }

    private static enum DemandsRate {
        NONE,
        LOW,
        MEDIUM,
        HIGH
    }
}
