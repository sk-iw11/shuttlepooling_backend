package org.iw11.backend.model;

import com.google.common.base.Preconditions;

import java.util.Objects;

public class BusDemand {

    private BusStation departue;
    private BusStation destination;

    public BusDemand(BusStation departue, BusStation destination) {
        Preconditions.checkNotNull(departue);
        Preconditions.checkNotNull(destination);
        this.departue = departue;
        this.destination = destination;
    }

    public BusStation getDepartue() {
        return departue;
    }

    public BusStation getDestination() {
        return destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusDemand busDemand = (BusDemand) o;
        return departue.equals(busDemand.departue) &&
                destination.equals(busDemand.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departue, destination);
    }

    @Override
    public String toString() {
        return departue.getName() + " -> " + destination.toString();
    }
}
