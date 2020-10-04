package com.ryanair.flights.service.flightsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {

    private final String airportFrom;
    private final String airportTo;
    private final String connectingAirport;
    private final String operator;

    public Route(final String airportFrom,
                 final String airportTo,
                 final String connectingAirport,
                 final String operator) {

        this.airportFrom = airportFrom;
        this.airportTo = airportTo;
        this.connectingAirport = connectingAirport;
        this.operator = operator;
    }

    public String getAirportFrom() {
        return airportFrom;
    }

    public String getAirportTo() {
        return airportTo;
    }

    public String getConnectingAirport() {
        return connectingAirport;
    }

    public String getOperator() {
        return operator;
    }
}