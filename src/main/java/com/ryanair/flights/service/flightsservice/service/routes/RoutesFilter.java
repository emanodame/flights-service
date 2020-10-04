package com.ryanair.flights.service.flightsservice.service.routes;

public class RoutesFilter {

    public static boolean isRyanairOperator(final String operator) {
        return operator.equalsIgnoreCase("ryanair");
    }

    public static boolean isNullConnectingAirport(final String connectingAirport) {
        return connectingAirport == null;
    }
}
