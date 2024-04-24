package com.mslup.lot.lotcrud.mapper;

import com.mslup.lot.lotcrud.dto.FlightDto;
import com.mslup.lot.lotcrud.model.Flight;
import java.util.HashSet;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 * Klasa mapująca obiekt typu {@link FlightDto} na model lotu {@link Flight}.
 */
@Service
public class FlightDtoMapper implements Function<FlightDto, Flight> {
    @Override
    public Flight apply(FlightDto flight) {
        return Flight.builder()
            .flightNumber(flight.flightNumber())
            .originAirport(flight.originAirport())
            .availableSeatsCount(flight.availableSeatsCount())
            .destinationAirport(flight.destinationAirport())
            .departureDateTime(flight.departureDateTime())
            .passengers(new HashSet<>())
            .build();
    }
}
