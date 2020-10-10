package com.ryanair.flights.service.flightsservice.controller;

import com.ryanair.flights.service.flightsservice.domain.Flight;
import com.ryanair.flights.service.flightsservice.service.flights.FlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/flights")
public class FlightController {

    private final FlightsService flightsService;

    @Autowired
    public FlightController(FlightsService flightsService) {
        this.flightsService = flightsService;
    }

    @RequestMapping("/interconnections")
    public List<Flight> getFlights(@RequestParam final String departure,
                                   @RequestParam final String departureDateTime,
                                   @RequestParam final String arrival,
                                   @RequestParam final String arrivalDateTime) throws ParseException {

        return flightsService.getFlights(departure, departureDateTime, arrival, arrivalDateTime);
    }
}
