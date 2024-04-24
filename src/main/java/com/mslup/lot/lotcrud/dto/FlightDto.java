package com.mslup.lot.lotcrud.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

/**
 * Data Transfer Object (DTO) dla danych lotu używany w zapytaniu POST.
 * Dane są walidowane.
 */
public record FlightDto(@NotNull(message = "Flight number cannot be null")
                        String flightNumber,
                        @NotNull(message = "Origin airport cannot be null")
                        String originAirport,
                        @NotNull(message = "Destination airport cannot be null")
                        String destinationAirport,
                        @NotNull(message = "Departure datetime cannot be null")
                        OffsetDateTime departureDateTime,
                        @Min(value = 10, message = "Seats count has to be greater than or equal 10")
                        @Max(value = 500, message = "Seats count has to be less than or equal 500")
                        int availableSeatsCount) {
}
