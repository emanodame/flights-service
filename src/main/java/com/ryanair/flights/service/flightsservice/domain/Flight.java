package com.ryanair.flights.service.flightsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flight {

    private final String number;
    private final String departureTime;
    private final String arrivalTime;

    public Flight(final String number,
                  final String departureTime,
                  final String arrivalTime) {

        this.number = number;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getNumber() {
        return number;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }
}
