package org.iw11.backend.util;

import org.iw11.backend.model.GeoCoordinates;

public final class CoordinatesUtil {

    private static final double R = 6371; //Earth's radius in km

    private CoordinatesUtil() { }

    public static double getDistance(GeoCoordinates pointA, GeoCoordinates pointB) {
        double deltaLat = degreeToRadian(pointB.getLatitude() - pointA.getLatitude());
        double deltaLon = degreeToRadian(pointB.getLongitude() - pointA.getLongitude());
        double a =
                Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                        Math.cos(degreeToRadian(pointA.getLatitude())) * Math.cos(degreeToRadian(pointB.getLatitude())) *
                                Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    }

    private static double degreeToRadian(double degree) {
        return degree * (Math.PI / 180);
    }
}
