package org.iw11.backend.demand;

import org.iw11.backend.model.BusDemand;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO: Check concurrency model
 */
@Service
public class DemandsService {

    private Map<BusDemand, AtomicInteger> demandsMap = new ConcurrentHashMap<>();

    public DemandsService() { }

    public void putDemand(BusDemand demand) {
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
}
