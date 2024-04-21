package com.mslup.lot.lotcrud;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.mslup.lot.lotcrud.exception.FlightNotFoundException;
import com.mslup.lot.lotcrud.model.Flight;
import com.mslup.lot.lotcrud.service.FlightService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FlightServiceTest extends LotCrudApplicationTests {
    @Autowired
    private FlightService flightService;

    @Test
    @Order(1)
    public void givenFlight_whenAddFlight_thenFlightIsAdded() {
        // Given
        Flight flight = Flight.builder()
            .destinationAirport("WWA")
            .originAirport("NYC")
            .departureDateTime(OffsetDateTime.of(2024, 5, 15, 14, 32, 0, 0, ZoneOffset.ofHours(1)))
            .availableSeatsCount(23)
            .build();

        // When
        Flight returnedFlight = flightService.saveFlight(flight);

        // Then
        assertThat(returnedFlight.getId()).isEqualTo(1);
        assertThat(returnedFlight).usingRecursiveComparison().isEqualTo(flight);
    }

    @Test
    @Order(2)
    public void givenFlightList_whenGetAllFlights_thenAllFlightsAreReturned() {
        // Given
        flightService.saveFlight(Flight.builder().build());
        flightService.saveFlight(Flight.builder().build());
        flightService.saveFlight(Flight.builder().build());
        flightService.saveFlight(Flight.builder().build());

        // When
        List<Flight> flightList = flightService.getAllFlights();

        // Then
        assertThat(flightList).isNotNull();
        assertThat(flightList.size()).isEqualTo(5);
    }

    @Test
    @Order(3)
    public void givenFlight_whenPatch_thenPatchedCorrectly() {
        // Given
        Optional<Flight> flight = flightService.findFlight(1);
        Flight patch = Flight.builder()
            .destinationAirport("BUD")
            .availableSeatsCount(200L)
            .build();

        // When
        try {
            flightService.patchFlight(1, patch);
        } catch (FlightNotFoundException e) {
            fail();
        }

        // Then
        Optional<Flight> patchedFlight = flightService.findFlight(1);
        assertThat(patchedFlight.orElseThrow().getDestinationAirport()).isEqualTo("BUD");
        assertThat(patchedFlight.orElseThrow().getAvailableSeatsCount()).isEqualTo(200L);
        assertThat(patchedFlight.orElseThrow().getFlightNumber()).isEqualTo(
            flight.get().getFlightNumber());
        assertThat(patchedFlight.orElseThrow().getOriginAirport()).isEqualTo(
            flight.get().getOriginAirport());
        assertThat(patchedFlight.orElseThrow().getDepartureDateTime()).isEqualTo(
            flight.get().getDepartureDateTime());
    }
}
