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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DirectFlightsRetrieverTest {

    final SchedulesService schedulesService = buildSchedulesService();
    final FlightsRetriever directFlightsRetriever = new DirectFlightsRetriever(schedulesService);

    @Test
    public void shouldSuccessfullyRetrieveDirectFlights() {

        final AirportGraph airportGraph = new AirportGraph(Collections.singletonMap(new Airport("BAU"), Arrays.asList(new Airport("MAD"), new Airport("HAW"))));

        final Airport departureAirport = new Airport("BAU");
        final Airport arrivalAirport = new Airport("MAD");

        final LocalDate departureLocalDate = LocalDate.of(2020, 10, 10);
        final LocalDate arrivalLocalDate = LocalDate.of(2020, 10, 12);

        final List<Flight> flights = directFlightsRetriever.retrieveFlights(airportGraph, departureAirport, arrivalAirport, departureLocalDate, arrivalLocalDate);

        assertThat(flights.get(0).getStops(), is("0"));

        assertThat(flights.get(0).getLegs().get(0).getDepartureAirport(), is("BAU"));
        assertThat(flights.get(0).getLegs().get(0).getArrivalAirport(), is("MAD"));
        assertThat(flights.get(0).getLegs().get(0).getDepartureDateTime(), is("2020-10-4T06:50"));
        assertThat(flights.get(0).getLegs().get(0).getArrivalDateTime(), is("2020-10-4T10:25"));
    }

    private SchedulesService buildSchedulesService() {

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());

        final SchedulesClient sc = Feign.builder()
                .decoder(new JacksonDecoder(objectMapper))
                .client(new MockClient().ok(HttpMethod.GET, "/timtbl/3/schedules/BAU/MAD/years/2020/months/10", getSchedulesResponseAsString()))
                .target(new MockTarget<>(SchedulesClient.class));

        return new SchedulesService(sc);
    }

    private String getSchedulesResponseAsString() {

        final InputStream responseStream = this.getClass().getResourceAsStream("/responses.flights/direct-flight-schedule-response.json");

        java.util.Scanner s = new java.util.Scanner(responseStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

