package com.ryanair.flights.service.flightsservice.domain;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AirportGraph {

    private final Map<Airport, List<Airport>> airports;

    public AirportGraph(final Map<Airport, List<Airport>> airports) {
        this.airports = airports;
    }

    public Map<Airport, List<Airport>> getAirports() {
        return Collections.unmodifiableMap(airports);
    }
}
