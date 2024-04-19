package com.mslup.lot.lotcrud;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        Optional<Flight> returnedFlight = flightService.addFlight(flight);

        // Then
        assertThat(returnedFlight).isPresent();
        assertThat(returnedFlight.get().getId()).isEqualTo(1);
        assertThat(flight).usingRecursiveComparison().isEqualTo(returnedFlight);
    }

    @Test
    @Order(2)
    public void givenFlightList_whenGetAllFlights_thenAllFlightsAreReturned() {
        // Given
        flightService.addFlight(Flight.builder().build());
        flightService.addFlight(Flight.builder().build());
        flightService.addFlight(Flight.builder().build());
        flightService.addFlight(Flight.builder().build());

        // When
        List<Flight> flightList = flightService.getAllFlights();

        // Then
        assertThat(flightList).isNotNull();
        assertThat(flightList.size()).isEqualTo(5);
    }
}
