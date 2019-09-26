package org.iw11.backend;

import org.iw11.backend.model.BusStation;
import org.iw11.backend.model.GeoCoordinates;

public final class Stations {

    public static final BusStation SKOLTECH = new BusStation("skoltech", new GeoCoordinates(0.0, 0.0));
    public static final BusStation TECHNOPARK = new BusStation("technopark", new GeoCoordinates(0.0, 0.0));
    public static final BusStation NOBEL_STREET = new BusStation("nobelstreet", new GeoCoordinates(0.0, 0.0));
    public static final BusStation USADBA = new BusStation("usadba", new GeoCoordinates(0.0, 0.0));
    public static final BusStation PARKING = new BusStation("parking", new GeoCoordinates(0.0, 0.0));

    private Stations() { }
}
