package com.ryanair.flights.service.flightsservice.service.flights;

import com.ryanair.flights.service.flightsservice.domain.Airport;
import com.ryanair.flights.service.flightsservice.domain.AirportGraph;
import com.ryanair.flights.service.flightsservice.domain.Flight;
import com.ryanair.flights.service.flightsservice.domain.Route;
import com.ryanair.flights.service.flightsservice.service.graph.AirportGraphService;
import com.ryanair.flights.service.flightsservice.service.routes.RoutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FlightsService {

    private final RoutesService routesService;
    private final AirportGraphService airportGraphService;

    private final List<FlightsRetriever> flightRetrievers;

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US);
    private final static Function<Date, LocalDate> toLocalDate = (date -> date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

    @Autowired
    public FlightsService(final RoutesService routesService,
                          final AirportGraphService airportGraphService,
                          final List<FlightsRetriever> flightRetrievers) {

        this.routesService = routesService;
        this.airportGraphService = airportGraphService;
        this.flightRetrievers = flightRetrievers;
    }

    public List<Flight> getFlights(final String departure,
                                   final String departureDateTime,
                                   final String arrival,
                                   final String arrivalDateTime) throws ParseException {

        final List<Route> routes = routesService.getRoutes();
        final AirportGraph airportGraph = airportGraphService.buildAirportGraph(routes);

        final LocalDate departureLocalDate = toLocalDate.apply(dateFormat.parse(departureDateTime));
        final LocalDate arrivalLocalDate = toLocalDate.apply(dateFormat.parse(arrivalDateTime));

        return retrieveDirectAndInterconnectingFlights(airportGraph, new Airport(departure), new Airport(arrival), departureLocalDate, arrivalLocalDate);
    }

    private List<Flight> retrieveDirectAndInterconnectingFlights(final AirportGraph airportGraph,
                                                                 final Airport departureAirport,
                                                                 final Airport arrivalAirport,
                                                                 final LocalDate departureLocalDate,
                                                                 final LocalDate arrivalLocalDate) {

        return flightRetrievers.stream()
                .flatMap(flightRetriever -> flightRetriever.retrieveFlights(airportGraph, departureAirport, arrivalAirport, departureLocalDate, arrivalLocalDate).stream())
                .collect(Collectors.toList());
    }
}
