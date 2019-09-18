package org.iw11.backend.benchmark;

import org.iw11.backend.model.BusDemand;
import org.iw11.backend.model.BusRoute;
import org.iw11.backend.model.BusStation;
import org.iw11.backend.planner.RoutesGenerator;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractSimulationScenario {

    private int lowThreshold;
    private int mediumThreshold;
    private int highThreshold;

    private double averageMinPerKm;

    private Graph<BusStation, DefaultWeightedEdge> roadMap;

    private RoutesGenerator routesGenerator = new RoutesGenerator();

    protected abstract Map<BusDemand, Integer> generateDemands(LocalTime currentTime);
    protected abstract int getUpdateDuration(LocalTime currentTime);

    protected AbstractSimulationScenario(Graph<BusStation, DefaultWeightedEdge> roadMap, double averageMinPerKm,
                                         int lowThreshold, int mediumThreshold, int highThreshold) {
        this.lowThreshold = lowThreshold;
        this.mediumThreshold = mediumThreshold;
        this.highThreshold = highThreshold;
        this.averageMinPerKm = averageMinPerKm;
        this.roadMap = roadMap;
    }

    public void run() {

        double totalOvercomeDistance = 0.0;
        int totalRides = 0;
        int peopleCount = 0;

        LocalTime currentTime = StandardTimes.TIME_7;
        LocalTime routeCompleteTime = StandardTimes.TIME_7;

        while (currentTime.compareTo(StandardTimes.TIME_21) < 0) {

            if (currentTime.compareTo(routeCompleteTime) < 0) {
                currentTime = currentTime.plusMinutes(getUpdateDuration(currentTime));
                continue;
            }

            System.out.println(currentTime);

            var demands = generateDemands(currentTime);
            if (demands.isEmpty()) {
                currentTime = currentTime.plusMinutes(getUpdateDuration(currentTime));
                continue;
            }

            var routes = routesGenerator.generateRoutes(roadMap, demands);
            var route = routes.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
                    .get().getKey();

            double length = calculateRouteLength(route);
            int satisfiedDemands = calculateSatisfiedDemands(route, demands);

            totalOvercomeDistance += length;
            totalRides++;
            peopleCount += satisfiedDemands;

            double routeTime = length * averageMinPerKm;
            routeCompleteTime = routeCompleteTime.plusMinutes((long)routeTime);

            currentTime = currentTime.plusMinutes(getUpdateDuration(currentTime));
        }

        System.out.println("\n\n\n###### RESULTS ######");
        System.out.println("Total overcome distance: " + totalOvercomeDistance);
        System.out.println("Total completed rides: " + totalRides);
        System.out.println("Total people transfered: " + peopleCount);

    }

    protected int generateDemandsNumber(DemandsRate priority) {
        switch (priority) {
            case LOW:
                return ThreadLocalRandom.current().nextInt(0, lowThreshold);
            case MEDIUM:
                return ThreadLocalRandom.current().nextInt(lowThreshold + 1, mediumThreshold);
            case HIGH:
                return ThreadLocalRandom.current().nextInt(mediumThreshold + 1, highThreshold);
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

    protected enum DemandsRate {
        NONE,
        LOW,
        MEDIUM,
        HIGH
    }
}
