package com.ryanair.flights.service.flightsservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class Day {

    private final String day;
    private final List<ScheduleFlight> scheduleFlights;

    public Day(final String day, @JsonProperty("flights") final List<ScheduleFlight> scheduleFlights) {
        this.day = day;
        this.scheduleFlights = scheduleFlights;
    }

    public String getDay() {
        return day;
    }

    public List<ScheduleFlight> getScheduleFlights() {
        return Collections.unmodifiableList(scheduleFlights);
    }
}
