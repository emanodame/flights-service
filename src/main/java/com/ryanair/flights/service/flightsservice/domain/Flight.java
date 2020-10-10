package com.ryanair.flights.service.flightsservice.domain;

import java.util.Collections;
import java.util.List;

public class Flight {

    private final String stops;
    private final List<Leg> legs;

    public Flight(final String stops, final List<Leg> legs) {
        this.stops = stops;
        this.legs = legs;
    }

    public String getStops() {
        return stops;
    }

    public List<Leg> getLegs() {
        return Collections.unmodifiableList(legs);
    }
}
