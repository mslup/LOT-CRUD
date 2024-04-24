package com.mslup.lot.lotcrud;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.mslup.lot.lotcrud.exception.PassengerNotFoundException;
import com.mslup.lot.lotcrud.model.Passenger;
import com.mslup.lot.lotcrud.service.PassengerService;
import java.util.List;
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
    public void givenPassenger_whenAddPassenger_thenPassengerIsAdded() {
        // Given
        Passenger passenger =
            Passenger.builder().firstName("Jan").lastName("Kowalski").phoneNumber("+48111222333")
                .build();

        // When
        Passenger returnedPassenger = passengerService.savePassenger(passenger);

        // Then
        assertThat(returnedPassenger.getId()).isEqualTo(1);
        assertThat(returnedPassenger).usingRecursiveComparison().isEqualTo(passenger);
    }

    @Test
    @Order(2)
    public void givenPassengerList_whenGetAllPassengers_thenAllPassengersAreReturned() {
        // Given
        passengerService.savePassenger(Passenger.builder().build());
        passengerService.savePassenger(Passenger.builder().build());
        passengerService.savePassenger(Passenger.builder().build());
        passengerService.savePassenger(Passenger.builder().build());

        // When
        List<Passenger> passengers = passengerService.getPassengers();

        // Then
        assertThat(passengers).isNotNull();
        assertThat(passengers.size()).isEqualTo(5);
    }

    @Test
    @Order(3)
    public void givenPassenger_whenPatch_thenPatchedCorrectly() {
        // Given
        Passenger passenger = passengerService.findPassenger(1);
        Passenger patch = Passenger.builder().firstName("Adam").lastName("Krawczyk").build();

        // When
        try {
            passengerService.patchPassenger(1, patch);
        } catch (PassengerNotFoundException e) {
            fail();
        }

        // Then
        Passenger patchedPassenger = passengerService.findPassenger(1);
        assertThat(patchedPassenger.getFirstName()).isEqualTo("Adam");
        assertThat(patchedPassenger.getLastName()).isEqualTo("Krawczyk");
        assertThat(patchedPassenger.getPhoneNumber()).isEqualTo(passenger.getPhoneNumber());
    }

    @Test
    @Order(4)
    public void givenPassengers_whenDelete_thenDeleted() {
        // Given

        // When
        passengerService.deletePassenger(3);

        // Then
        List<Passenger> passengers = passengerService.getPassengers();
        assertThat(passengers.size()).isEqualTo(4);
        assertThrows(PassengerNotFoundException.class, () ->  passengerService.findPassenger(3));
    }
}
