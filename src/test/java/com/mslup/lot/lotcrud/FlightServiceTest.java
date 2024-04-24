package com.mslup.lot.lotcrud;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import com.mslup.lot.lotcrud.exception.FlightNotFoundException;
import com.mslup.lot.lotcrud.filter.FlightFilterCriteria;
import com.mslup.lot.lotcrud.model.Flight;
import com.mslup.lot.lotcrud.model.Passenger;
import com.mslup.lot.lotcrud.service.FlightService;
import com.mslup.lot.lotcrud.service.PassengerService;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

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
        Flight flight = Flight.builder().destinationAirport("WWA").originAirport("NYC")
            //.departureDateTime(OffsetDateTime.parse("2007-12-03T10:15:30+01:00"))
            .availableSeatsCount(23).build();

        //OffsetDateTime.parse("")
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
        Flight flight = flightService.findFlight(1);
        Flight patch = Flight.builder().destinationAirport("BUD").availableSeatsCount(200).build();

        // When
        flightService.patchFlight(1, patch);

        // Then
        Flight patchedFlight = flightService.findFlight(1);

        assertThat(patchedFlight.getDestinationAirport()).isEqualTo("BUD");
        assertThat(patchedFlight.getAvailableSeatsCount()).isEqualTo(200L);
        assertThat(patchedFlight.getFlightNumber()).isEqualTo(flight.getFlightNumber());
        assertThat(patchedFlight.getOriginAirport()).isEqualTo(flight.getOriginAirport());
        assertThat(patchedFlight.getDepartureDateTime()).isEqualTo(flight.getDepartureDateTime());
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
        assertThrows(FlightNotFoundException.class, () -> flightService.findFlight(4));
    }

    @Test
    @Order(5)
    public void givenAirportCriteria_whenFilter_thenReturnCorrect() {
        // Given
        flightService.saveFlight(
            Flight.builder().originAirport("WWA").destinationAirport("KVN").build());
        flightService.saveFlight(
            Flight.builder().originAirport("WWA").destinationAirport("KVN").build());
        flightService.saveFlight(
            Flight.builder().originAirport("KVN").destinationAirport("WWA").build());
        flightService.saveFlight(
            Flight.builder().originAirport("KRK").destinationAirport("KVN").build());
        flightService.saveFlight(
            Flight.builder().originAirport("NYC").destinationAirport("KVN").build());

        FlightFilterCriteria criteria =
            FlightFilterCriteria.builder().originAirport("WWA").destinationAirport("KVN").build();

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
        flightService.saveFlight(
            Flight.builder().departureDateTime(OffsetDateTime.parse("2024-05-15T14:32:00+01:00"))
                .build());
        flightService.saveFlight(
            Flight.builder().departureDateTime(OffsetDateTime.parse("2024-06-15T14:32:00+01:00"))
                .build());
        flightService.saveFlight(
            Flight.builder().departureDateTime(OffsetDateTime.parse("2024-07-15T14:32:00+01:00"))
                .build());
        flightService.saveFlight(
            Flight.builder().departureDateTime(OffsetDateTime.parse("2023-07-15T14:32:00+01:00"))
                .build());

        // When
        OffsetDateTime date = OffsetDateTime.parse("2024-06-15T14:32:00+01:00");
        FlightFilterCriteria criteriaAfter = FlightFilterCriteria.builder().dateFrom(date).build();
        List<Flight> flightsAfter = flightService.getFlights(criteriaAfter);

        FlightFilterCriteria criteriaBefore = FlightFilterCriteria.builder().dateTo(date).build();
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
        OffsetDateTime dateFrom = OffsetDateTime.parse("2024-06-10T00:00:00-04:00");
        OffsetDateTime dateTo = OffsetDateTime.parse("2024-06-20T00:00:00+10:00");
        FlightFilterCriteria criteria =
            FlightFilterCriteria.builder().dateFrom(dateFrom).dateTo(dateTo).build();
        List<Flight> flights = flightService.getFlights(criteria);

        // Then
        assertThat(flights.size()).isEqualTo(1);
        for (Flight flight : flights) {
            assertThat(flight.getDepartureDateTime()).isBetween(dateFrom, dateTo);
        }
    }

    @Test
    @Order(8)
    @Transactional
    @Commit
    public void givenFlight_whenAddPassenger_thenPassengerIsAdded() {
        // Given
        Flight flight = flightService.findFlight(1);
        long seatsCount = flight.getAvailableSeatsCount();

        passengerService.savePassenger(Passenger.builder().build());

        // When
        flightService.addPassenger(1, 1);

        // Then
        Flight flightAfter = flightService.findFlight(1);
        assertThat(flightAfter.getAvailableSeatsCount()).isEqualTo(seatsCount - 1);
        assertThat(flightAfter.getPassengers()).extracting(Passenger::getId).contains(1L);
        Passenger passenger = passengerService.findPassenger(1);
        assertThat(passenger.getBookings().size()).isEqualTo(1);
        assertThat(passenger.getBookings()).extracting(Flight::getId).contains(1L);
    }

    @Test
    @Order(9)
    @Transactional
    public void givenFlight_whenDeletePassenger_thenPassengerIsDeleted() {
        // Given
        Flight flight = flightService.findFlight(1);
        long seatsCount = flight.getAvailableSeatsCount();

        // When
        try {
            flightService.deletePassenger(1, 1);
        } catch (FlightNotFoundException e) {
            fail("Flight with id = 1 not found");
        }

        // Then
        Flight flightAfter = flightService.findFlight(1);
        assertThat(flightAfter.getAvailableSeatsCount()).isEqualTo(seatsCount + 1);
        assertThat(flightAfter.getPassengers().size()).isEqualTo(0);
        Passenger passenger = passengerService.findPassenger(1);
        assertThat(passenger.getBookings().size()).isEqualTo(0);
    }

}
