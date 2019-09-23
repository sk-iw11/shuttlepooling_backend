package org.iw11.backend.rest;

import org.iw11.backend.map.RoadMapService;
import org.iw11.backend.model.BusDemand;
import org.iw11.backend.model.GeoCoordinates;
import org.iw11.backend.planner.BusTracker;
import org.iw11.backend.planner.RoutePlanner;
import org.iw11.backend.rest.model.AssignedBusModel;
import org.iw11.backend.rest.model.BusLocationModel;
import org.iw11.backend.rest.model.DemandApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RestController {

    private static final String METHOD_POST_DEMAND = "/api/demand";
    private static final String METHOD_GET_BUS = "/api/bus";
    private static final String METHOD_POST_LOCATION = "/api/bus/location";

    private static final String POST_CONTENT_TYPE = "application/json";

    private RoutePlanner routePlanner;
    private BusTracker busTracker;
    private RoadMapService roadMapService;

    @Autowired
    public RestController(RoutePlanner routePlanner, BusTracker busTracker, RoadMapService roadMapService) {
        this.routePlanner =routePlanner;
        this.busTracker = busTracker;
        this.roadMapService = roadMapService;
    }

    @RequestMapping(path = METHOD_POST_DEMAND, method = RequestMethod.POST, consumes = POST_CONTENT_TYPE)
    public ResponseEntity postDemand(@RequestBody DemandApiModel demandRequest) {
        var stationDeparture = roadMapService.getStation(demandRequest.getDeparture());
        var stationDestination = roadMapService.getStation(demandRequest.getDestination());
        if (stationDeparture.isEmpty() || stationDestination.isEmpty())
            return ResponseEntity.badRequest().build();

        var demand = new BusDemand(stationDeparture.get(), stationDestination.get());

        var bus = busTracker.getBusForDemand(demand);
        if (bus.isPresent())
            return ResponseEntity.ok().body(AssignedBusModel.create(bus.get().left, bus.get().right));

        routePlanner.increaseDemand(demand);
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(path = METHOD_GET_BUS, method = RequestMethod.GET)
    public ResponseEntity getBus(@RequestParam String departure, @RequestParam String destination) {
        var stationDeparture = roadMapService.getStation(departure);
        var stationDestination = roadMapService.getStation(destination);
        if (stationDeparture.isEmpty() || stationDestination.isEmpty())
            return ResponseEntity.badRequest().build();

        var bus = busTracker.getBusForDemand(new BusDemand(stationDeparture.get(), stationDestination.get()));
        if (bus.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().body(AssignedBusModel.create(bus.get().left, bus.get().right));
    }

    @RequestMapping(path = METHOD_POST_LOCATION, method = RequestMethod.POST, consumes = POST_CONTENT_TYPE)
    public ResponseEntity postLocation(@RequestBody BusLocationModel location) {
        busTracker.updateBusLocation(location.getName(), new GeoCoordinates(location.getLatitude(), location.getLongitude()));
        return ResponseEntity.ok().build();
    }
}
