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
import java.util.HashSet;
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
        Flight flight = Flight.builder()
            .flightNumber("FL1234")
            .originAirport("JFK")
            .destinationAirport("LAX")
            .departureDateTime(OffsetDateTime.parse("2024-04-23T15:00:00-04:00"))
            .availableSeatsCount(200)
            .build();

        // When
        Flight returnedFlight = flightService.saveFlight(flight);

        // Then
        assertThat(returnedFlight.getId()).isEqualTo(1);
        assertThat(returnedFlight).usingRecursiveComparison().isEqualTo(flight);
    }

    public void prepareData() {
        flightService.saveFlight(Flight.builder()
            .flightNumber("FL5678")
            .originAirport("JFK")
            .destinationAirport("LAX")
            .departureDateTime(OffsetDateTime.parse("2024-04-24T10:00:00+01:00"))
            .availableSeatsCount(150)
            .passengers(new HashSet<>())
            .build());
        flightService.saveFlight(Flight.builder()
            .flightNumber("FL91011")
            .originAirport("LAX")
            .destinationAirport("JFK")
            .departureDateTime(OffsetDateTime.parse("2024-04-25T22:00:00+04:00"))
            .availableSeatsCount(300)
            .passengers(new HashSet<>())
            .build());
        flightService.saveFlight(Flight.builder()
            .flightNumber("FL1213")
            .originAirport("JFK")
            .destinationAirport("LAX")
            .departureDateTime(OffsetDateTime.parse("2024-04-26T09:00:00-07:00"))
            .availableSeatsCount(180)
            .passengers(new HashSet<>())
            .build());
        flightService.saveFlight(Flight.builder()
            .flightNumber("FL1415")
            .originAirport("BOS")
            .destinationAirport("MIA")
            .departureDateTime(OffsetDateTime.parse("2024-04-27T14:00:00-04:00"))
            .availableSeatsCount(220)
            .passengers(new HashSet<>())
            .build());
        flightService.saveFlight(Flight.builder()
            .flightNumber("FL1617")
            .originAirport("NRT")
            .destinationAirport("LAX")
            .departureDateTime(OffsetDateTime.parse("2024-04-28T21:00:00+09:00"))
            .availableSeatsCount(250)
            .passengers(new HashSet<>())
            .build());
        flightService.saveFlight(Flight.builder()
            .flightNumber("FL1819")
            .originAirport("AMS")
            .destinationAirport("JFK")
            .departureDateTime(OffsetDateTime.parse("2024-04-29T11:00:00+01:00"))
            .availableSeatsCount(300)
            .passengers(new HashSet<>())
            .build());
        flightService.saveFlight(Flight.builder()
            .flightNumber("FL2021")
            .originAirport("JFK")
            .destinationAirport("CAI")
            .departureDateTime(OffsetDateTime.parse("2024-04-30T17:00:00+03:00"))
            .availableSeatsCount(350)
            .passengers(new HashSet<>())
            .build());
    }

    @Test
    @Order(2)
    public void givenFlightList_whenGetAllFlights_thenAllFlightsAreReturned() {
        // Given
        prepareData();

        // When
        List<Flight> flightList = flightService.getFlights();

        // Then
        assertThat(flightList).isNotNull();
        assertThat(flightList.size()).isEqualTo(8);
    }

    @Test
    @Order(3)
    public void givenFlight_whenPatch_thenPatchedCorrectly() {
        // Given
        Flight flight = flightService.findFlight(1);
        Flight patch = Flight.builder().destinationAirport("WAW").availableSeatsCount(200).build();

        // When
        flightService.patchFlight(1, patch);

        // Then
        Flight patchedFlight = flightService.findFlight(1);

        assertThat(patchedFlight.getDestinationAirport()).isEqualTo("WAW");
        assertThat(patchedFlight.getAvailableSeatsCount()).isEqualTo(200);
        assertThat(patchedFlight.getFlightNumber()).isEqualTo(flight.getFlightNumber());
        assertThat(patchedFlight.getOriginAirport()).isEqualTo(flight.getOriginAirport());
        assertThat(patchedFlight.getDepartureDateTime()).isEqualTo(flight.getDepartureDateTime());
    }

    @Test
    @Order(4)
    public void givenFlights_whenDelete_thenDeleted() {
        // Given

        // When
        flightService.deleteFlight(8);

        // Then
        List<Flight> flights = flightService.getFlights();
        assertThat(flights.size()).isEqualTo(7);
        assertThrows(FlightNotFoundException.class, () -> flightService.findFlight(8));
    }

    @Test
    @Order(5)
    public void givenAirportCriteria_whenFilter_thenReturnCorrect() {
        // Given
        FlightFilterCriteria criteria =
            FlightFilterCriteria.builder().originAirport("JFK").destinationAirport("LAX").build();

        // When
        List<Flight> flights = flightService.getFlights(criteria);

        // Then
        assertThat(flights.size()).isEqualTo(2);

        for (Flight flight : flights) {
            assertThat(flight.getOriginAirport()).isEqualTo("JFK");
            assertThat(flight.getDestinationAirport()).isEqualTo("LAX");
        }
    }

    @Test
    @Order(6)
    public void givenDateFromOrTo_whenFilter_thenReturnCorrect() {
        // Given

        // When
        OffsetDateTime date = OffsetDateTime.parse("2024-04-28T21:00:00+09:00");
        FlightFilterCriteria criteriaAfter = FlightFilterCriteria.builder().dateFrom(date).build();
        List<Flight> flightsAfter = flightService.getFlights(criteriaAfter);

        FlightFilterCriteria criteriaBefore = FlightFilterCriteria.builder().dateTo(date).build();
        List<Flight> flightsBefore = flightService.getFlights(criteriaBefore);

        // Then
        assertThat(flightsAfter.size()).isEqualTo(2);
        for (Flight flight : flightsAfter) {
            assertThat(flight.getDepartureDateTime()).isAfterOrEqualTo(date);
        }

        assertThat(flightsBefore.size()).isEqualTo(6);
        for (Flight flight : flightsBefore) {
            assertThat(flight.getDepartureDateTime()).isBeforeOrEqualTo(date);
        }
    }

    @Test
    @Order(7)
    public void givenDateBetweenCriteria_whenFilter_thenReturnCorrect() {
        // Given

        // When
        OffsetDateTime dateFrom = OffsetDateTime.parse("2024-04-25T22:00:00+04:00");
        OffsetDateTime dateTo = OffsetDateTime.parse("2024-04-28T21:00:00+09:00");
        FlightFilterCriteria criteria =
            FlightFilterCriteria.builder().dateFrom(dateFrom).dateTo(dateTo).build();
        List<Flight> flights = flightService.getFlights(criteria);

        // Then
        assertThat(flights.size()).isEqualTo(4);
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

        passengerService.savePassenger(Passenger.builder()
            .firstName("John")
            .lastName("Doe")
            .phoneNumber("123-456-7890")
            .build());

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
