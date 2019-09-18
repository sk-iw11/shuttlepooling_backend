package org.iw11.backend.benchmark;

import org.iw11.backend.Stations;
import org.iw11.backend.model.BusDemand;
import org.iw11.backend.model.BusStation;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class WorkdaySimulationScenario extends AbstractSimulationScenario {

    public WorkdaySimulationScenario(Graph<BusStation, DefaultWeightedEdge> roadMap) {
        super(roadMap, 2.86, 3, 5, 12);
    }

    @Override
    protected Map<BusDemand, Integer> generateDemands(LocalTime currentTime) {
        var demands = new HashMap<BusDemand, Integer>();

        // From 7:00 to 10:00
        if (currentTime.compareTo(StandardTimes.TIME_7) >= 0 && currentTime.compareTo(StandardTimes.TIME_10) < 0) {
            demands.put(new BusDemand(Stations.USADBA, Stations.TECHNOPARK), generateDemandsNumber(DemandsRate.LOW));
            demands.put(new BusDemand(Stations.PARKING, Stations.TECHNOPARK), generateDemandsNumber(DemandsRate.MEDIUM));
            demands.put(new BusDemand(Stations.USADBA, Stations.SKOLTECH), generateDemandsNumber(DemandsRate.MEDIUM));
            demands.put(new BusDemand(Stations.PARKING, Stations.SKOLTECH), generateDemandsNumber(DemandsRate.MEDIUM));
            demands.put(new BusDemand(Stations.USADBA, Stations.NOBEL_STREET), generateDemandsNumber(DemandsRate.MEDIUM));
            demands.put(new BusDemand(Stations.PARKING, Stations.NOBEL_STREET), generateDemandsNumber(DemandsRate.MEDIUM));
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.SKOLTECH), generateDemandsNumber(DemandsRate.HIGH));
        }

        // From 10:00 to 12:00
        else if (currentTime.compareTo(StandardTimes.TIME_10) >= 0 && currentTime.compareTo(StandardTimes.TIME_12) < 0) {
            demands.put(new BusDemand(Stations.NOBEL_STREET, Stations.TECHNOPARK), generateDemandsNumber(DemandsRate.LOW));
            demands.put(new BusDemand(Stations.NOBEL_STREET, Stations.SKOLTECH), generateDemandsNumber(DemandsRate.MEDIUM));
        }

        // From 12:00 to 14:00
        else if (currentTime.compareTo(StandardTimes.TIME_12) >= 0 && currentTime.compareTo(StandardTimes.TIME_14) < 0) {
            demands.put(new BusDemand(Stations.SKOLTECH, Stations.TECHNOPARK), generateDemandsNumber(DemandsRate.HIGH));
            demands.put(new BusDemand(Stations.NOBEL_STREET, Stations.TECHNOPARK), generateDemandsNumber(DemandsRate.HIGH));
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.SKOLTECH), generateDemandsNumber(DemandsRate.MEDIUM));
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.NOBEL_STREET), generateDemandsNumber(DemandsRate.HIGH));
        }

        // From 14:00 to 17:00
        else if (currentTime.compareTo(StandardTimes.TIME_14) >= 0 && currentTime.compareTo(StandardTimes.TIME_17) < 0) {
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.NOBEL_STREET), generateDemandsNumber(DemandsRate.LOW));
            demands.put(new BusDemand(Stations.SKOLTECH, Stations.NOBEL_STREET), generateDemandsNumber(DemandsRate.MEDIUM));
            demands.put(new BusDemand(Stations.SKOLTECH, Stations.USADBA), generateDemandsNumber(DemandsRate.LOW));
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.PARKING), generateDemandsNumber(DemandsRate.LOW));
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.USADBA), generateDemandsNumber(DemandsRate.LOW));
        }

        // From 17:00 to 21:00
        else if (currentTime.compareTo(StandardTimes.TIME_17) >= 0 && currentTime.compareTo(StandardTimes.TIME_21) < 0) {
            demands.put(new BusDemand(Stations.SKOLTECH, Stations.USADBA), generateDemandsNumber(DemandsRate.LOW));
            demands.put(new BusDemand(Stations.SKOLTECH, Stations.PARKING), generateDemandsNumber(DemandsRate.LOW));
            demands.put(new BusDemand(Stations.SKOLTECH, Stations.TECHNOPARK), generateDemandsNumber(DemandsRate.HIGH));
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.USADBA), generateDemandsNumber(DemandsRate.LOW));
            demands.put(new BusDemand(Stations.TECHNOPARK, Stations.PARKING), generateDemandsNumber(DemandsRate.LOW));
            demands.put(new BusDemand(Stations.NOBEL_STREET, Stations.TECHNOPARK), generateDemandsNumber(DemandsRate.HIGH));
            demands.put(new BusDemand(Stations.NOBEL_STREET, Stations.USADBA), generateDemandsNumber(DemandsRate.MEDIUM));
        }

        return demands;
    }

    @Override
    protected int getUpdateDuration(LocalTime currentTime) {
        return 15;
    }
}
