package com.ryanair.flights.service.flightsservice.service.flights;

import com.ryanair.flights.service.flightsservice.domain.Airport;
import com.ryanair.flights.service.flightsservice.domain.AirportGraph;
import com.ryanair.flights.service.flightsservice.domain.Flight;

import java.time.LocalDate;
import java.util.List;

public interface FlightsRetriever {

    List<Flight> retrieveFlights(final AirportGraph airportGraph,
                                 final Airport departureAirport,
                                 final Airport arrivalAirport,
                                 final LocalDate departureLocalDate,
                                 final LocalDate arrivalLocalDate);
}
