package com.mslup.lot.lotcrud;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.mslup.lot.lotcrud.exception.PassengerNotFoundException;
import com.mslup.lot.lotcrud.model.Passenger;
import com.mslup.lot.lotcrud.service.PassengerService;
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

    public void prepareData() {
        passengerService.savePassenger(Passenger.builder()
            .firstName("John")
            .lastName("Doe")
            .phoneNumber("123-456-7890")
            .build());
        passengerService.savePassenger(Passenger.builder()
            .firstName("Jane")
            .lastName("Smith")
            .phoneNumber("098-765-4321")
            .build());
        passengerService.savePassenger(Passenger.builder()
            .firstName("Alice")
            .lastName("Johnson")
            .phoneNumber("456-123-7890")
            .build());
    }

    @Test
    @Order(1)
    public void givenPassenger_whenAddPassenger_thenPassengerIsAdded() {
        // Given
        prepareData();
        int passengersCount = passengerService.getPassengers().size();
        Passenger passenger = Passenger.builder()
            .firstName("Jan")
            .lastName("Kowalski")
            .phoneNumber("+48111222333")
            .build();

        // When
        Passenger returnedPassenger = passengerService.savePassenger(passenger);

        // Then
        int newPassengersCount = passengerService.getPassengers().size();
        assertThat(newPassengersCount).isEqualTo(passengersCount + 1);
        assertThat(returnedPassenger).usingRecursiveComparison().isEqualTo(passenger);
    }

    @Test
    @Order(2)
    public void givenPassenger_whenPatch_thenPatchedCorrectly() {
        // Given
        Passenger passenger = passengerService.findPassenger(1);
        Passenger patch = Passenger.builder().firstName("Adam").lastName("Krawczyk").build();

        // When
        try {
            passengerService.patchPassenger(1, patch);
        } catch (PassengerNotFoundException e) {
            fail("Passenger with id = 1 not found");
        }

        // Then
        Passenger patchedPassenger = passengerService.findPassenger(1);
        assertThat(patchedPassenger.getFirstName()).isEqualTo("Adam");
        assertThat(patchedPassenger.getLastName()).isEqualTo("Krawczyk");
        assertThat(patchedPassenger.getPhoneNumber()).isEqualTo(passenger.getPhoneNumber());
    }

    @Test
    @Order(3)
    public void givenPassengers_whenDelete_thenDeleted() {
        // Given
        int passengersCount = passengerService.getPassengers().size();

        // When
        passengerService.deletePassenger(2);

        // Then
        int newPassengersCount = passengerService.getPassengers().size();
        assertThat(newPassengersCount).isEqualTo(passengersCount - 1);
        assertThrows(PassengerNotFoundException.class, () -> passengerService.findPassenger(2));
    }
}
