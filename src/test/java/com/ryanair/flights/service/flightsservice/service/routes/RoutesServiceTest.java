package com.ryanair.flights.service.flightsservice.service.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.ryanair.flights.service.flightsservice.client.RoutesClient;
import com.ryanair.flights.service.flightsservice.domain.Route;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import feign.mock.MockTarget;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RoutesServiceTest {

    @Test
    public void shouldSuccessfullyRetrieveRyanairRoutesOnly() {

        final String responseAsString = getRoutesResponseAsString("/non-ryanair-route-response.json");
        final RoutesService routesService = new RoutesService(buildRoutesClient(responseAsString));

        final List<Route> routes = routesService.getRoutes();

        assertThat(routes.size(), is(1));
        assertThat(routes.get(0).getOperator(), is("RYANAIR"));
    }

    @Test
    public void shouldSuccessfullyNullConnectingAirportsOnly() {

        final String responseAsString = getRoutesResponseAsString("/connecting-airports-response.json");

        final RoutesService routesService = new RoutesService(buildRoutesClient(responseAsString));

        final List<Route> routes = routesService.getRoutes();

        assertThat(routes.size(), is(1));
        assertThat(routes.get(0).getAirportFrom(), is("LGW"));
    }

    private RoutesClient buildRoutesClient(final String responseString) {

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());

        return Feign.builder().decoder(new JacksonDecoder(objectMapper))
                .client(new MockClient().ok(HttpMethod.GET, "/locate/3/routes", responseString))
                .target(new MockTarget<>(RoutesClient.class));
    }

    private String getRoutesResponseAsString(final String fileName) {

        final InputStream responseStream = this.getClass().getResourceAsStream(fileName);

        java.util.Scanner s = new java.util.Scanner(responseStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
