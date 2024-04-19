package com.mslup.lot.lotcrud;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.mslup.lot.lotcrud.model.Flight;
import com.mslup.lot.lotcrud.model.Passenger;
import com.mslup.lot.lotcrud.service.FlightService;
import com.mslup.lot.lotcrud.service.PassengerService;
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
public class PassengerServiceTest extends LotCrudApplicationTests {
    @Autowired
    private PassengerService passengerService;

    @Test
    @Order(1)
    public void givenFlight_whenAddFlight_thenFlightIsAdded() {
        // Given
        Passenger passenger = Passenger.builder()
            .firstName("Jan")
            .lastName("Kowalski")
            .phoneNumber("+48111222333")
            .build();

        // When
        Optional<Passenger> returnedPassenger = passengerService.savePassenger(passenger);

        // Then
        assertThat(returnedPassenger).isPresent();
        assertThat(returnedPassenger.get().getId()).isEqualTo(1);
        assertThat(returnedPassenger.get()).usingRecursiveComparison().isEqualTo(passenger);
    }

    @Test
    @Order(2)
    public void givenFlightList_whenGetAllFlights_thenAllFlightsAreReturned() {
        // Given
        passengerService.savePassenger(Passenger.builder().build());
        passengerService.savePassenger(Passenger.builder().build());
        passengerService.savePassenger(Passenger.builder().build());
        passengerService.savePassenger(Passenger.builder().build());

        // When
        List<Passenger> passengers = passengerService.getAllPassengers();

        // Then
        assertThat(passengers).isNotNull();
        assertThat(passengers.size()).isEqualTo(5);
    }
}
