package com.ryanair.flights.service.flightsservice.service.routes;

import com.ryanair.flights.service.flightsservice.client.RoutesClient;
import com.ryanair.flights.service.flightsservice.domain.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ryanair.flights.service.flightsservice.service.routes.RoutesFilter.isNullConnectingAirport;
import static com.ryanair.flights.service.flightsservice.service.routes.RoutesFilter.isRyanairOperator;
import static java.util.stream.Collectors.toList;

@Service
public class RoutesService {

    private final RoutesClient routesClient;

    @Autowired
    public RoutesService(final RoutesClient routesClient) {
        this.routesClient = routesClient;
    }

    public List<Route> getRoutes() {

        final List<Route> routes = routesClient.getRoutes();

        return routes.stream()
                .filter(route -> isNullConnectingAirport(route.getConnectingAirport()))
                .filter(route -> isRyanairOperator(route.getOperator()))
                .collect(toList());
    }
}