package com.ryanair.flights.service.flightsservice.service.flights;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.ryanair.flights.service.flightsservice.client.RoutesClient;
import com.ryanair.flights.service.flightsservice.client.SchedulesClient;
import com.ryanair.flights.service.flightsservice.domain.Flight;
import com.ryanair.flights.service.flightsservice.service.graph.AirportGraphService;
import com.ryanair.flights.service.flightsservice.service.routes.RoutesService;
import com.ryanair.flights.service.flightsservice.service.schedules.SchedulesService;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import feign.mock.MockTarget;
import org.junit.Test;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FlightsServiceTest {

    @Test
    public void shouldSuccessfullyGetDirectAndInterconnectingFlights() throws ParseException {

        final FlightsService flightsService = buildFlightsService();

        final List<Flight> flights = flightsService.getFlights("LGW", "2020-10-10T14:00", "JFK", "2020-15-10T14:00");

        assertThat(flights.get(0).getStops(), is("0"));
        assertThat(flights.get(1).getStops(), is("1"));
    }


    private FlightsService buildFlightsService() {

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());

        final RoutesClient routesClient = Feign.builder()
                .decoder(new JacksonDecoder(objectMapper))
                .client(new MockClient().ok(HttpMethod.GET, "/locate/3/routes", getRoutesResponseAsString()))
                .target(new MockTarget<>(RoutesClient.class));

        final SchedulesClient schedulesClient = Feign.builder()
                .decoder(new JacksonDecoder(objectMapper))
                .client(new MockClient()
                        .ok(HttpMethod.GET, "/timtbl/3/schedules/LGW/JFK/years/2020/months/10", getSchedulesResponseAsString("/responses.flights/direct-flight-schedule-response.json"))
                        .ok(HttpMethod.GET, "/timtbl/3/schedules/LGW/STN/years/2020/months/10", getSchedulesResponseAsString("/responses.flights/interconnecting-flight-schedule-leg-1-response.json"))
                        .ok(HttpMethod.GET, "/timtbl/3/schedules/STN/JFK/years/2020/months/10", getSchedulesResponseAsString("/responses.flights/interconnecting-flight-schedule-leg-2-response.json"))
                )
                .target(new MockTarget<>(SchedulesClient.class));

        final SchedulesService ss = new SchedulesService(schedulesClient);

        return new FlightsService(
                new RoutesService(routesClient),
                new AirportGraphService(),
                Arrays.asList(new DirectFlightsRetriever(ss), new InterconnectingFlightsRetriever(ss))
        );
    }

    private String getSchedulesResponseAsString(final String file) {

        final InputStream responseStream = this.getClass().getResourceAsStream(file);

        java.util.Scanner s = new java.util.Scanner(responseStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private String getRoutesResponseAsString() {

        final InputStream responseStream = this.getClass().getResourceAsStream("/responses/routes/connecting-airports-response.json");

        java.util.Scanner s = new java.util.Scanner(responseStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
