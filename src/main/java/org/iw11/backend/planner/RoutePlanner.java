package org.iw11.backend.planner;

import org.iw11.backend.map.RoadMapService;
import org.iw11.backend.model.BusDemand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class RoutePlanner {

    private static final Logger LOG = LoggerFactory.getLogger(RoutePlanner.class);

    private static final int INIT_DELAY = 5;
    private static final int SPIN_PERIOD = 3;

    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    private Map<BusDemand, AtomicInteger> demandsMap = new ConcurrentHashMap<>();

    private final RoadMapService roadMapService;
    private final BusTracker busTracker;

    private final RoutesGenerator routesGenerator;

    @Autowired
    public RoutePlanner(RoadMapService roadMapService, BusTracker busTracker) {
        this.roadMapService = roadMapService;
        this.busTracker = busTracker;
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

    public void increaseDemand(BusDemand demand) {
        var atomic = demandsMap.putIfAbsent(demand, new AtomicInteger(1));
        if (atomic != null)
            atomic.incrementAndGet();
    }

    public void satisfyDemand(BusDemand demand) {
        demandsMap.remove(demand);
    }

    public Map<BusDemand, Integer> getDemands() {
        var demands = new HashMap<BusDemand, Integer>();
        demandsMap.entrySet().forEach((entry -> demands.put(entry.getKey(), entry.getValue().get())));
        return demands;
    }

    private void spin() {
        var demands = getDemands();
        if (demands.isEmpty()) {
            LOG.info("Demands are empty, wait for the next spin");
            return;
        }

        var routes = routesGenerator.generateRoutes(roadMapService.getRoadMap(), demands).entrySet().stream()
                .sorted((entry1, entry2) -> entry1.getValue() < entry1.getValue() ? 1 : -1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        busTracker.assignRoutes(routes);

        demands.keySet().stream()
                .filter(demand -> routes.stream().anyMatch(route -> route.canSatisfyDemand(demand)))
                .forEach(this::satisfyDemand);

        LOG.info("Routes scheduled");
    }
}
