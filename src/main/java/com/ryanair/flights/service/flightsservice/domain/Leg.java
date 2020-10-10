package com.ryanair.flights.service.flightsservice.domain;

public class Leg {

    private final String departureAirport;
    private final String arrivalAirport;
    private final String departureDateTime;
    private final String arrivalDateTime;

    public Leg(final String departureAirport,
               final String arrivalAirport,
               final String departureDateTime,
               final String arrivalDateTime) {

        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    public String getArrivalDateTime() {
        return arrivalDateTime;
    }
}
