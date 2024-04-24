package com.mslup.lot.lotcrud;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.mslup.lot.lotcrud.exception.FlightNotFoundException;
import com.mslup.lot.lotcrud.exception.PassengerNotFoundException;
import com.mslup.lot.lotcrud.filter.FlightFilterCriteria;
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
public class FlightServiceTest extends LotCrudApplicationTests {
    @Autowired
    private FlightService flightService;
    @Autowired
    private PassengerService passengerService;


    // todo: add example data before all tests, remove passengerService
    @Test
    @Order(1)
    public void givenFlight_whenAddFlight_thenFlightIsAdded() {
        // Given
        Flight flight = Flight.builder()
            .destinationAirport("WWA")
            .originAirport("NYC")
            //.departureDateTime(OffsetDateTime.of(2024, 5, 15, 14, 32, 0, 0, ZoneOffset.ofHours(1)))
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
        List<Flight> flightList = flightService.getFlights();

        // Then
        assertThat(flightList).isNotNull();
        assertThat(flightList.size()).isEqualTo(5);
    }

    @Test
    @Order(3)
    public void givenFlight_whenPatch_thenPatchedCorrectly() {
        // Given
        Optional<Flight> flight = flightService.findFlight(1);
        assertThat(flight).isPresent();
        Flight patch = Flight.builder()
            .destinationAirport("BUD")
            .availableSeatsCount(200)
            .build();

        // When
        try {
            flightService.patchFlight(1, patch);
        } catch (FlightNotFoundException e) {
            fail("Flight with id = 1 not found");
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

    @Test
    @Order(4)
    public void givenFlights_whenDelete_thenDeleted() {
        // Given

        // When
        flightService.deleteFlight(4);

        // Then
        List<Flight> flights = flightService.getFlights();
        assertThat(flights.size()).isEqualTo(4);

        Optional<Flight> flight = flightService.findFlight(4);
        assertThat(flight).isNotPresent();
    }

    @Test
    @Order(5)
    public void givenAirportCriteria_whenFilter_thenReturnCorrect() {
        // Given
        flightService.saveFlight(Flight.builder()
            .originAirport("WWA")
            .destinationAirport("KVN")
            .build());
        flightService.saveFlight(Flight.builder()
            .originAirport("WWA")
            .destinationAirport("KVN")
            .build());
        flightService.saveFlight(Flight.builder()
            .originAirport("KVN")
            .destinationAirport("WWA")
            .build());
        flightService.saveFlight(Flight.builder()
            .originAirport("KRK")
            .destinationAirport("KVN")
            .build());
        flightService.saveFlight(Flight.builder()
            .originAirport("NYC")
            .destinationAirport("KVN")
            .build());

        FlightFilterCriteria criteria = FlightFilterCriteria.builder()
            .originAirport("WWA")
            .destinationAirport("KVN")
            .build();

        // When
        List<Flight> flights = flightService.getFlights(criteria);

        // Then
        assertThat(flights.size()).isEqualTo(2);

        for (Flight flight : flights) {
            assertThat(flight.getOriginAirport()).isEqualTo("WWA");
            assertThat(flight.getDestinationAirport()).isEqualTo("KVN");
        }
    }

    @Test
    @Order(6)
    public void givenDateFromOrTo_whenFilter_thenReturnCorrect() {
        // Given
        // todo: parsing datetime
        {
            flightService.saveFlight(Flight.builder()
                .departureDateTime(OffsetDateTime.of(2024,
                    5,
                    15,
                    14,
                    32,
                    0,
                    0,
                    ZoneOffset.ofHours(1)))
                .build());
            flightService.saveFlight(Flight.builder()
                .departureDateTime(OffsetDateTime.of(2024,
                    6,
                    15,
                    14,
                    32,
                    0,
                    0,
                    ZoneOffset.ofHours(1)))
                .build());
            flightService.saveFlight(Flight.builder()
                .departureDateTime(OffsetDateTime.of(2024,
                    7,
                    15,
                    14,
                    32,
                    0,
                    0,
                    ZoneOffset.ofHours(1)))
                .build());
            flightService.saveFlight(Flight.builder()
                .departureDateTime(OffsetDateTime.of(2023,
                    7,
                    15,
                    14,
                    32,
                    0,
                    0,
                    ZoneOffset.ofHours(1)))
                .build());
        }

        // When
        OffsetDateTime date = OffsetDateTime.of(2024,
            6,
            15,
            14,
            32,
            0,
            0,
            ZoneOffset.ofHours(1));
        FlightFilterCriteria criteriaAfter = FlightFilterCriteria.builder()
            .dateFrom(date)
            .build();
        List<Flight> flightsAfter = flightService.getFlights(criteriaAfter);

        FlightFilterCriteria criteriaBefore = FlightFilterCriteria.builder()
            .dateTo(date)
            .build();
        List<Flight> flightsBefore = flightService.getFlights(criteriaBefore);

        // Then
        assertThat(flightsAfter.size()).isEqualTo(2);
        for (Flight flight : flightsAfter) {
            assertThat(flight.getDepartureDateTime()).isAfterOrEqualTo(date);
        }

        assertThat(flightsBefore.size()).isEqualTo(3);
        for (Flight flight : flightsBefore) {
            assertThat(flight.getDepartureDateTime()).isBeforeOrEqualTo(date);
        }
    }

    @Test
    @Order(7)
    public void givenDateBetweenCriteria_whenFilter_thenReturnCorrect() {
        // Given

        // When
        OffsetDateTime dateFrom = OffsetDateTime.of(2024,
            6,
            10,
            0,
            0,
            0,
            0,
            ZoneOffset.ofHours(-4));
        OffsetDateTime dateTo = OffsetDateTime.of(2024,
            6,
            20,
            0,
            0,
            0,
            0,
            ZoneOffset.ofHours(10));
        FlightFilterCriteria criteria = FlightFilterCriteria.builder()
            .dateFrom(dateFrom)
            .dateTo(dateTo)
            .build();
        List<Flight> flights = flightService.getFlights(criteria);

        // Then
        assertThat(flights.size()).isEqualTo(1);
        for (Flight flight : flights) {
            assertThat(flight.getDepartureDateTime()).isBetween(dateFrom, dateTo);
        }
    }

    // todo: tests for seatcount? test for multicriteria



    @Test
    @Order(8)
    //@Transactional
    public void givenFlight_whenAddPassenger_thenPassengerIsAdded() {
        // Given
        Optional<Flight> flight = flightService.findFlight(1);
        assertThat(flight).isPresent();
        long seatsCount = flight.get().getAvailableSeatsCount();

        passengerService.savePassenger(Passenger.builder().build());

        // When
        try {
            flightService.addPassenger(1, 1);
        } catch (PassengerNotFoundException e) {
            fail("Passenger with id = 1 not found");
        } catch (FlightNotFoundException e) {
            fail("Flight with id = 1 not found");
        }

        // Then
        Optional<Flight> flightAfter = flightService.findFlight(1);
        assertThat(flightAfter).isPresent();
        assertThat(flightAfter.get().getAvailableSeatsCount()).isEqualTo(seatsCount - 1);
        assertThat(flightAfter.get().getPassengers())
            .extracting(Passenger::getId)
            .contains(1L);
        Optional<Passenger> passenger = passengerService.findPassenger(1);
        assertThat(passenger).isPresent();
        assertThat(passenger.get().getBookings().size()).isEqualTo(1);
        assertThat(passenger.get().getBookings()).extracting(Flight::getId).contains(1L);
    }

    @Test
    @Order(9)
    public void givenFlight_whenDeletePassenger_thenPassengerIsDeleted() {
        // Given
        Optional<Flight> flight = flightService.findFlight(1);
        assertThat(flight).isPresent();
        long seatsCount = flight.get().getAvailableSeatsCount();

        // When
        try {
            flightService.deletePassenger(1, 1);
        } catch (FlightNotFoundException e) {
            fail("Flight with id = 1 not found");
        }

        // Then
        Optional<Flight> flightAfter = flightService.findFlight(1);
        assertThat(flightAfter).isPresent();
        assertThat(flightAfter.get().getAvailableSeatsCount()).isEqualTo(seatsCount + 1);
        assertThat(flightAfter.get().getPassengers().size()).isEqualTo(0);
        Optional<Passenger> passenger = passengerService.findPassenger(1);
        assertThat(passenger).isPresent();
        assertThat(passenger.get().getBookings().size()).isEqualTo(0);
    }

}
