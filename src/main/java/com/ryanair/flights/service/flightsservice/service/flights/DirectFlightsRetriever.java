package com.ryanair.flights.service.flightsservice.service.flights;

import com.ryanair.flights.service.flightsservice.domain.*;
import com.ryanair.flights.service.flightsservice.service.schedules.SchedulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Component
public class DirectFlightsRetriever implements FlightsRetriever {

    private final SchedulesService schedulesService;

    @Autowired
    public DirectFlightsRetriever(final SchedulesService schedulesService) {
        this.schedulesService = schedulesService;
    }

    public List<Flight> retrieveFlights(final AirportGraph airportGraph,
                                        final Airport departureAirport,
                                        final Airport arrivalAirport,
                                        final LocalDate departureLocalDate,
                                        final LocalDate arrivalLocalDate) {

        final List<Airport> airports = airportGraph.getAirports().get(departureAirport).stream()
                .filter(airport -> airport.equals(arrivalAirport))
                .collect(toList());

        if (airports.isEmpty()) {
            return emptyList();
        }

        final Schedule schedule = schedulesService.getSchedules(
                departureAirport.getAirportCode(),
                arrivalAirport.getAirportCode(),
                String.valueOf(departureLocalDate.getYear()),
                String.valueOf(departureLocalDate.getMonthValue())
        );

        return schedule.getDays()
                .stream()
                .flatMap(day -> day.getScheduleFlights().stream().map(scheduleFlight -> {

                    final String flightDepartureDateTime = flightDepartureDateTimeBuilder(departureLocalDate, day, scheduleFlight);
                    final String flightArrivalDateTime = flightArrivalDateTimeBuilder(arrivalLocalDate, day, scheduleFlight);

                    final Leg leg = new Leg(departureAirport.getAirportCode(), arrivalAirport.getAirportCode(), flightDepartureDateTime, flightArrivalDateTime);

                    return new Flight("0", Collections.singletonList(leg));
                })).collect(toList());
    }

    private String flightDepartureDateTimeBuilder(final LocalDate departureLocalDate,
                                                  final Day departureDay,
                                                  final ScheduleFlight scheduleFlight) {

        return departureLocalDate.getYear() + "-" +
                departureLocalDate.getMonthValue() + "-" +
                departureDay.getDay() +
                "T" +
                scheduleFlight.getDepartureTime();
    }

    private String flightArrivalDateTimeBuilder(final LocalDate arrivalLocalDate,
                                                final Day arrivalDay,
                                                final ScheduleFlight scheduleFlight) {

        return arrivalLocalDate.getYear() + "-" +
                arrivalLocalDate.getMonthValue() + "-" +
                arrivalDay.getDay() +
                "T" +
                scheduleFlight.getArrivalTime();
    }
}
