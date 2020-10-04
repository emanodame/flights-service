package com.ryanair.flights.service.flightsservice.domain;

import java.util.Collections;
import java.util.List;

public class Schedule {

    private final String month;
    private final List<Day> days;

    public Schedule(final String month, final List<Day> days) {
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
