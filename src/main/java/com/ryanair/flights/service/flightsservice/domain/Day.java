package com.ryanair.flights.service.flightsservice.domain;

import java.util.Collections;
import java.util.List;

public class Day {

    private final String day;
    private final List<Flight> flights;

    public Day(final String day, final List<Flight> flights) {
        this.day = day;
        this.flights = flights;
    }

    public String getDay() {
        return day;
    }

    public List<Flight> getFlights() {
        return Collections.unmodifiableList(flights);
    }
}
