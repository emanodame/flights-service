package com.ryanair.flights.service.flightsservice.client;

import com.ryanair.flights.service.flightsservice.domain.Schedule;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers(value = {"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
public interface SchedulesClient {

    @RequestLine("GET /timtbl/3/schedules/{departure}/{arrival}/years/{year}/months/{month}")
    Schedule getSchedules(
            @Param("departure") final String departure,
            @Param("arrival") final String arrival,
            @Param("year") final String year,
            @Param("month") final String month
    );
}