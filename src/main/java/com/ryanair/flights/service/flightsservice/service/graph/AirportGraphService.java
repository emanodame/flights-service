package com.ryanair.flights.service.flightsservice.service.graph;

import com.ryanair.flights.service.flightsservice.domain.Airport;
import com.ryanair.flights.service.flightsservice.domain.AirportGraph;
import com.ryanair.flights.service.flightsservice.domain.Route;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AirportGraphService {

    public AirportGraph buildAirportGraph(final List<Route> routes) {

        final Map<Airport, List<Airport>> airportMap = new HashMap<>();

        for (Route route : routes) {
            airportMap.putIfAbsent(new Airport(route.getAirportFrom()), new ArrayList<>());

            final List<Airport> connectingAirports = airportMap.get(new Airport(route.getAirportFrom()));
            connectingAirports.add(new Airport(route.getAirportTo()));

            airportMap.put(new Airport(route.getAirportFrom()), connectingAirports);
        }

        return new AirportGraph(airportMap);
    }
}
