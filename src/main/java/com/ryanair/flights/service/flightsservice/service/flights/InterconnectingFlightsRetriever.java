package com.ryanair.flights.service.flightsservice.service.flights;

import com.ryanair.flights.service.flightsservice.domain.*;
import com.ryanair.flights.service.flightsservice.service.schedules.SchedulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
public class InterconnectingFlightsRetriever implements FlightsRetriever {

    private final SchedulesService schedulesService;

    @Autowired
    public InterconnectingFlightsRetriever(final SchedulesService schedulesService) {
        this.schedulesService = schedulesService;
    }

    @Override
    public List<Flight> retrieveFlights(final AirportGraph airportGraph,
                                        final Airport departureAirport,
                                        final Airport arrivalAirport,
                                        final LocalDate departureLocalDate,
                                        final LocalDate arrivalLocalDate) {

        return airportGraph.getAirports().get(departureAirport).stream()
                .filter(airport -> isNotDirectFlight(airport, arrivalAirport))
                .filter(airport -> hasInterconnectingFlight(airportGraph.getAirports().get(airport), arrivalAirport))
                .flatMap(interconnectingAirport -> {

                    final Schedule firstLegSchedules = schedulesService.getSchedules(departureAirport.getAirportCode(), interconnectingAirport.getAirportCode(), String.valueOf(departureLocalDate.getYear()), String.valueOf(departureLocalDate.getMonthValue()));
                    final Schedule secondLegSchedules = schedulesService.getSchedules(interconnectingAirport.getAirportCode(), arrivalAirport.getAirportCode(), String.valueOf(departureLocalDate.getYear()), String.valueOf(departureLocalDate.getMonthValue()));

                    final List<Trip> eligibleTrips = buildTrips(firstLegSchedules, secondLegSchedules);

                    return buildFlightsFromTrips(eligibleTrips, departureAirport, interconnectingAirport, arrivalAirport).stream();

                }).collect(toList());
    }

    private boolean isNotDirectFlight(final Airport departureAirport, final Airport arrivalAirport) {
        return !departureAirport.equals(arrivalAirport);
    }

    private boolean hasInterconnectingFlight(final List<Airport> connectingAirports, final Airport arrivalAirport) {
        return connectingAirports.stream().anyMatch(airport -> airport.equals(arrivalAirport));
    }

    private List<Flight> buildFlightsFromTrips(final List<Trip> eligibleTrips,
                                               final Airport departureAirport,
                                               final Airport interconnectingAirport,
                                               final Airport arrivalAirport) {

        return eligibleTrips.stream()
                .flatMap(trip -> trip.secondLegs.stream().map(secondLegFlight -> new Flight("1", Arrays.asList(
                        new Leg(departureAirport.getAirportCode(), interconnectingAirport.getAirportCode(), trip.firstLeg.getDepartureTime(), trip.firstLeg.getArrivalTime()),
                        new Leg(interconnectingAirport.getAirportCode(), arrivalAirport.getAirportCode(), secondLegFlight.getDepartureTime(), secondLegFlight.getArrivalTime()))
                )))
                .collect(toList());
    }

    private List<Trip> buildTrips(final Schedule firstLegSchedules, final Schedule secondLegSchedule) {

        return firstLegSchedules.getDays().stream()
                .flatMap(firstLegDay -> firstLegDay.getScheduleFlights().stream().map(firstLegFlight -> buildTrip(firstLegFlight, secondLegSchedule)))
                .collect(Collectors.toList());
    }

    private Trip buildTrip(final ScheduleFlight firstLegFlight, final Schedule secondLegSchedule) {

        final List<ScheduleFlight> secondLegFlightsWithAtLeastTwoHourDiff =
                secondLegSchedule.getDays().stream()
                        .flatMap(day -> day.getScheduleFlights().stream())
                        .filter(day -> flightIsAtLeastTwoHoursApart(firstLegFlight.getArrivalTime(), day.getDepartureTime()))
                        .collect(toList());

        return new Trip(firstLegFlight, secondLegFlightsWithAtLeastTwoHourDiff);
    }

    private boolean flightIsAtLeastTwoHoursApart(final String arrivalTimeOfFirstLegFlight, final String departureTimeOfSecondLegFlight) {

        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        final BiFunction<Date, Date, Boolean> isTwoHoursApart = (date1, date2) -> ((date2.getTime() - date1.getTime()) / 1000) >= 7200;

        try {

            final Date arrivalTimeOfFirstLegFlightDate = dateFormat.parse(arrivalTimeOfFirstLegFlight);
            final Date departureTimeOfSecondLegFlightDate = dateFormat.parse(departureTimeOfSecondLegFlight);

            return isTwoHoursApart.apply(arrivalTimeOfFirstLegFlightDate, departureTimeOfSecondLegFlightDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    static class Trip {
        final ScheduleFlight firstLeg;
        final List<ScheduleFlight> secondLegs;

        public Trip(final ScheduleFlight firstLeg, final List<ScheduleFlight> secondLegs) {
            this.firstLeg = firstLeg;
            this.secondLegs = secondLegs;
        }
    }
}
