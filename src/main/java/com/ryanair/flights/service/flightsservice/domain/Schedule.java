package com.ryanair.flights.service.flightsservice.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class Schedule {

    private final String month;
    private final List<Day> days;

    @JsonCreator
    public Schedule(@JsonProperty("month") final String month, @JsonProperty("days") final List<Day> days) {
        this.month = month;
        this.days = days;
    }

    public String getMonth() {
        return month;
    }

    public List<Day> getDays() {
        return Collections.unmodifiableList(days);
    }
}
