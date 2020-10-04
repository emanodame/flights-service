package com.ryanair.flights.service.flightsservice.service.schedules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.ryanair.flights.service.flightsservice.client.SchedulesClient;
import com.ryanair.flights.service.flightsservice.domain.Schedule;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.mock.HttpMethod;
import feign.mock.MockClient;
import feign.mock.MockTarget;
import org.junit.Test;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SchedulesServiceTest {

    @Test
    public void shouldSuccessfullyGetSchedules() {

        final SchedulesClient schedulesClient = buildSchedulesClient();

        final SchedulesService schedulesService = new SchedulesService(schedulesClient);

        final Schedule schedule = schedulesService.getSchedules("DUB", "WRO", "2020", "10");

        assertThat(schedule.getMonth(), is("10"));
        assertThat(schedule.getDays().size(), is(20));
    }

    private SchedulesClient buildSchedulesClient() {

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());

        return Feign.builder().decoder(new JacksonDecoder(objectMapper))
                .client(new MockClient().ok(HttpMethod.GET, "/timtbl/3/schedules/DUB/WRO/years/2020/months/10", getSchedulesResponseAsString()))
                .target(new MockTarget<>(SchedulesClient.class));
    }


    private String getSchedulesResponseAsString() {

        final InputStream responseStream = this.getClass().getResourceAsStream("/responses.schedules/schedules-response.json");

        java.util.Scanner s = new java.util.Scanner(responseStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
