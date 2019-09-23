package org.iw11.backend.model;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.Objects;

public class BusStation {

    private String name;

    private GeoCoordinates forwardLocation;
    private GeoCoordinates backwardsLocation;

    public BusStation(String name, GeoCoordinates forwardLocation, GeoCoordinates backwardsLocation) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
        this.name = name;
        this.forwardLocation = forwardLocation;
        this.backwardsLocation = backwardsLocation;
    }

    public String getName() {
        return name;
    }

    public GeoCoordinates getForwardLocation() {
        return forwardLocation;
    }

    public GeoCoordinates getBackwardsLocation() {
        return backwardsLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusStation that = (BusStation) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
