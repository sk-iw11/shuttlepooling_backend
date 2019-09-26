package org.iw11.backend.rest;

import org.iw11.backend.auth.AuthService;
import org.iw11.backend.map.RoadMapService;
import org.iw11.backend.model.BusDemand;
import org.iw11.backend.model.GeoCoordinates;
import org.iw11.backend.planner.BusTracker;
import org.iw11.backend.planner.RoutePlanner;
import org.iw11.backend.rest.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RestController {

    private static final String METHOD_POST_DEMAND = "/api/demand";
    private static final String METHOD_GET_BUS = "/api/bus";
    private static final String METHOD_POST_LOCATION = "/api/bus/location";
    private static final String METHOD_POST_LOGIN = "/api/bus/login";
    private static final String METHOD_GET_ROUTE = "/api/bus/route";
    private static final String METHOD_POST_ROUTE_COMPLETE = "/api/bus/route/complete";

    private static final String POST_CONTENT_TYPE = "application/json";

    private static final String TOKEN_HEADER = "Access-token";

    private RoutePlanner routePlanner;
    private BusTracker busTracker;
    private RoadMapService roadMapService;
    private AuthService authService;

    @Autowired
    public RestController(RoutePlanner routePlanner, BusTracker busTracker, RoadMapService roadMapService, AuthService authService) {
        this.routePlanner = routePlanner;
        this.busTracker = busTracker;
        this.roadMapService = roadMapService;
        this.authService = authService;
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
    public ResponseEntity postLocation(@RequestBody BusLocationModel location, @RequestHeader(TOKEN_HEADER) String token) {
        var bus = authService.getBusByToken(token);
        if (bus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        busTracker.updateBusLocation(bus.get(), new GeoCoordinates(location.getLatitude(), location.getLongitude()));
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = METHOD_POST_LOGIN, method = RequestMethod.POST, consumes = POST_CONTENT_TYPE)
    public ResponseEntity postLogin(@RequestBody BusCredentialsModel credentials) {
        var token = authService.authorize(credentials.getName(), credentials.getPassword());
        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(new TokenModel(token.get()));
    }

    @RequestMapping(path = METHOD_GET_ROUTE, method = RequestMethod.GET)
    public ResponseEntity getRoute(@RequestHeader(TOKEN_HEADER) String token) {
        var bus = authService.getBusByToken(token);
        if (bus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var route = busTracker.getBusRoute(bus.get());
        if (route.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(route.get());
    }

    @RequestMapping(path = METHOD_POST_ROUTE_COMPLETE, method = RequestMethod.POST)
    public ResponseEntity postRouteComplete(@RequestHeader(TOKEN_HEADER) String token) {
        var bus = authService.getBusByToken(token);
        if (bus.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        busTracker.completeBusRoute(bus.get());
        return ResponseEntity.ok().build();
    }
}
