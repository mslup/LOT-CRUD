package com.mslup.lot.lotcrud.dto;

import java.time.OffsetDateTime;

public record FlightDto(String flightNumber,
                        String originAirport,
                        String destinationAirport,
                        OffsetDateTime departureDateTime,
                        int availableSeatsCount) {
}
