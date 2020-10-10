package com.ryanair.flights.service.flightsservice.service.flights;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.ryanair.flights.service.flightsservice.client.SchedulesClient;
import com.ryanair.flights.service.flightsservice.domain.Airport;
import com.ryanair.flights.service.flightsservice.domain.AirportGraph;
import com.ryanair.flights.service.flightsservice.domain.Flight;
import com.ryanair.flights.service.flightsservice.service.schedules.SchedulesService;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import feign.mock.MockTarget;
import org.junit.Test;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InterconnectingFlightsRetrieverTest {

    final SchedulesService schedulesService = buildSchedulesService();
    final FlightsRetriever interconnectingFlightRetriever = new InterconnectingFlightsRetriever(schedulesService);

    @Test
    public void shouldSuccessfullyRetrieveInterconnectingFlights() {

        final Map<Airport, List<Airport>> airportMap = new HashMap<>();
        airportMap.put(new Airport("BAU"), Collections.singletonList(new Airport("HEA")));
        airportMap.put(new Airport("HEA"), Collections.singletonList(new Airport("MAD")));

        final AirportGraph airportGraph = new AirportGraph(airportMap);

        final Airport departureAirport = new Airport("BAU");
        final Airport arrivalAirport = new Airport("MAD");

        final LocalDate departureLocalDate = LocalDate.of(2020, 10, 10);
        final LocalDate arrivalLocalDate = LocalDate.of(2020, 10, 12);

        final List<Flight> flights = interconnectingFlightRetriever.retrieveFlights(airportGraph, departureAirport, arrivalAirport, departureLocalDate, arrivalLocalDate);

        assertThat(flights.get(0).getStops(), is("1"));

        assertThat(flights.get(0).getLegs().get(0).getDepartureAirport(), is("BAU"));
        assertThat(flights.get(0).getLegs().get(0).getDepartureDateTime(), is("13:50"));

        assertThat(flights.get(0).getLegs().get(0).getArrivalAirport(), is("HEA"));
        assertThat(flights.get(0).getLegs().get(0).getArrivalDateTime(), is("14:20"));

        assertThat(flights.get(0).getLegs().get(1).getDepartureAirport(), is("HEA"));
        assertThat(flights.get(0).getLegs().get(1).getDepartureDateTime(), is("16:20"));

        assertThat(flights.get(0).getLegs().get(1).getArrivalAirport(), is("MAD"));
        assertThat(flights.get(0).getLegs().get(1).getArrivalDateTime(), is("20:20"));
    }

    private SchedulesService buildSchedulesService() {

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());

        final SchedulesClient sc = Feign.builder().decoder(new JacksonDecoder(objectMapper))
                .client(new MockClient()
                        .ok(HttpMethod.GET, "/timtbl/3/schedules/BAU/HEA/years/2020/months/10", getSchedulesResponseAsString("/responses.flights/interconnecting-flight-schedule-leg-1-response.json"))
                        .ok(HttpMethod.GET, "/timtbl/3/schedules/HEA/MAD/years/2020/months/10", getSchedulesResponseAsString("/responses.flights/interconnecting-flight-schedule-leg-2-response.json"))
                )
                .target(new MockTarget<>(SchedulesClient.class));

        return new SchedulesService(sc);
    }

    private String getSchedulesResponseAsString(final String path) {

        final InputStream responseStream = this.getClass().getResourceAsStream(path);

        java.util.Scanner s = new java.util.Scanner(responseStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
