package com.ryanair.flights.service.flightsservice.service.schedules;

import com.ryanair.flights.service.flightsservice.client.SchedulesClient;
import com.ryanair.flights.service.flightsservice.domain.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulesService {

    private final SchedulesClient schedulesClient;

    @Autowired
    public SchedulesService(final SchedulesClient schedulesClient) {
        this.schedulesClient = schedulesClient;
    }

    public Schedule getSchedules(final String departure,
                                       final String arrival,
                                       final String year,
                                       final String month) {

        return schedulesClient.getSchedules(departure, arrival, year, month);
    }
}
