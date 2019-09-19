package org.iw11.backend.rest;

import org.iw11.backend.model.BusDemand;
import org.iw11.backend.model.BusStation;
import org.iw11.backend.planner.RoutePlanner;
import org.iw11.backend.rest.model.DemandApiModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RestController {

    private static final String METHOD_POST_DEMAND = "/api/demand";

    private static final String CONTENT_TYPE = "application/json";

    private RoutePlanner routePlanner;

    @Autowired
    public RestController(RoutePlanner routePlanner) {
        this.routePlanner =routePlanner;
    }

    @RequestMapping(path = METHOD_POST_DEMAND, method = RequestMethod.POST, consumes = CONTENT_TYPE)
    public ResponseEntity postDemand(@RequestBody DemandApiModel demandRequest) {
        var demand = new BusDemand(new BusStation(demandRequest.getDeparture()),
                new BusStation(demandRequest.getDestination()));
        routePlanner.increaseDemand(demand);
        return ResponseEntity.ok().build();
    }
}
