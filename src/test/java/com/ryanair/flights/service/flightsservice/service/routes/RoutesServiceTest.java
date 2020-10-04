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
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RoutesServiceTest {

    @Test
    public void shouldSuccessfullyRetrieveRyanairRoutesOnly() {

        final InputStream responseStream = this.getClass().getResourceAsStream("/non-ryanair-route-response.json");
        final String responseString = convertStreamToString(responseStream);

        final RoutesClient mockedRouteClient = feignBuilder()
                .client(new MockClient().ok(HttpMethod.GET, "/locate/3/routes", responseString))
                .target(new MockTarget<>(RoutesClient.class));

        final RoutesService routesService = new RoutesService(mockedRouteClient);

        final List<Route> routes = routesService.getRoutes();

        assertThat(routes.size(), is(1));
        assertThat(routes.get(0).getOperator(), is("RYANAIR"));
    }

    private Feign.Builder feignBuilder() {

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());

        return Feign.builder().decoder(new JacksonDecoder(objectMapper));
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
