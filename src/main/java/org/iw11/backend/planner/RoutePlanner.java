package org.iw11.backend.planner;

import org.iw11.backend.demand.DemandsService;
import org.iw11.backend.map.RoadMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class RoutePlanner {

    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    private static final int INIT_DELAY = 5;
    private static final int SPIN_PERIOD = 10;

    private final DemandsService demandsService;
    private final RoadMapService roadMapService;

    private final RoutesGenerator routesGenerator;

    @Autowired
    public RoutePlanner(DemandsService demandsService, RoadMapService roadMapService) {
        this.demandsService = demandsService;
        this.roadMapService = roadMapService;
        this.routesGenerator = new RoutesGenerator();
    }

    @PostConstruct
    private void init() {
        EXECUTOR.scheduleAtFixedRate(this::spin, INIT_DELAY, SPIN_PERIOD, TimeUnit.SECONDS);
    }

    @PreDestroy
    private void destroy() {
        EXECUTOR.shutdown();
    }

    private void spin() {
        var demands = demandsService.getDemands();
        if (demands.isEmpty())
            return;

        var roadMap = roadMapService.getRoadMap();

        var routes = routesGenerator.generateRoutes(roadMap, demands);
    }
}
