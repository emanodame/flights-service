package com.ryanair.flights.service.flightsservice.client;

import com.ryanair.flights.service.flightsservice.domain.Route;
import feign.Headers;
import feign.RequestLine;

import java.util.List;

@Headers(value = {"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
public interface RoutesClient {

    @RequestLine("GET /locate/3/routes")
    List<Route> getRoutes();
}
