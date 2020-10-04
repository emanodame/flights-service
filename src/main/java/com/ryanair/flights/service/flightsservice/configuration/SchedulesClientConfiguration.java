package com.ryanair.flights.service.flightsservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.ryanair.flights.service.flightsservice.client.SchedulesClient;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulesClientConfiguration {

    @Bean
    public SchedulesClient schedulesClient() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());

        return Feign.builder()
                .decoder(new JacksonDecoder(objectMapper))
                .target(SchedulesClient.class, "https://services-api.ryanair.com/");
    }
}