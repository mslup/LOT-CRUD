package com.mslup.lot.lotcrud.controller;

import com.mslup.lot.lotcrud.exception.FlightNotFoundException;
import com.mslup.lot.lotcrud.exception.PassengerNotFoundException;
import com.mslup.lot.lotcrud.filter.FlightFilterCriteria;
import com.mslup.lot.lotcrud.model.Flight;
import com.mslup.lot.lotcrud.model.Passenger;
import com.mslup.lot.lotcrud.service.FlightService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kontroler obsługujący loty..
 */
@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    /**
     * Pobiera listę lotów na podstawie kryteriów filtrowania.
     * Jeśli kryteria są puste, zwraca wszystkie loty.
     *
     * @param originAirport    Opcjonalne: kod lotniska początkowego.
     * @param destinationAirport   Opcjonalne: kod lotniska docelowego.
     * @param dateFrom  Opcjonalne: data początkowa.
     * @param dateTo    Opcjonalne: data końcowa.
     * @param seatsCountFrom   Opcjonalne: minimalna liczba miejsc.
     * @param seatsCountTo Opcjonalne: maksymalna liczba miejsc.
     * @return ResponseEntity z listą lotów spełniających kryteria.
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Flight>> getFlights(
        @RequestParam Optional<String> originAirport,
        @RequestParam Optional<String> destinationAirport,
        @RequestParam Optional<OffsetDateTime> dateFrom,
        @RequestParam Optional<OffsetDateTime> dateTo,
        @RequestParam Optional<Integer> seatsCountFrom,
        @RequestParam Optional<Integer> seatsCountTo
    ) {
        FlightFilterCriteria criteria = FlightFilterCriteria.builder()
            .originAirport(originAirport.orElse(null))
            .destinationAirport(destinationAirport.orElse(null))
            .dateFrom(dateFrom.orElse(null))
            .dateTo(dateTo.orElse(null))
            .seatsCountFrom(seatsCountFrom.orElse(null))
            .seatsCountTo(seatsCountTo.orElse(null))
            .build();

        return ResponseEntity.ok(flightService.getFlights(criteria));
    }

    /**
     * Dodaje nowy lot.
     *
     * @param flight    Lot do dodania.
     * @return ResponseEntity z dodanym lotem.
     */
    @PostMapping
    ResponseEntity<Flight> addFlight(@RequestBody Flight flight) {
        return new ResponseEntity<>(flightService.saveFlight(flight), HttpStatus.CREATED);
    }

    /**
     * Pobiera szczegóły lotu na podstawie ID.
     *
     * @param id    ID lotu.
     * @return ResponseEntity z lotem lub kodem NOT_FOUND, jeśli lot nie istnieje.
     */
    @GetMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Flight> getFlight(@PathVariable long id) {
        return flightService.findFlight(id)
            .map(ResponseEntity::ok)
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Aktualizuje szczegóły lotu na podstawie ID.
     *
     * @param id    ID lotu.
     * @param flightNumber  Opcjonalne: nowy numer lotu.
     * @param originAirport    Opcjonalne: nowy kod lotniska początkowego.
     * @param destinationAirport   Opcjonalne: nowy kod lotniska docelowego.
     * @param departureDateTime Opcjonalne: nowa data i godzina odlotu.
     * @param availableSeatsCount   Opcjonalne: nowa liczba dostępnych miejsc.
     * @return ResponseEntity z zaktualizowanym lotem lub kodem NOT_FOUND, jeśli lot nie istnieje.
     */
    @PatchMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Flight> updateFlight(
        @PathVariable long id,
        @RequestParam Optional<String> flightNumber,
        @RequestParam Optional<String> originAirport,
        @RequestParam Optional<String> destinationAirport,
        @RequestParam Optional<OffsetDateTime> departureDateTime,
        @RequestParam Optional<Integer> availableSeatsCount) {
        Flight patch = Flight.builder()
            .flightNumber(flightNumber.orElse(null))
            .originAirport(originAirport.orElse(null))
            .destinationAirport(destinationAirport.orElse(null))
            .departureDateTime(departureDateTime.orElse(null))
            .availableSeatsCount(availableSeatsCount.orElse(-1))
            .build();

        try {
            Flight patchedFlight = flightService.patchFlight(id, patch);
            return ResponseEntity.ok(patchedFlight);
        } catch (FlightNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Usuwa lot na podstawie ID.
     *
     * @param id    ID lotu.
     * @return ResponseEntity z kodem OK lub kodem NOT_FOUND, jeśli lot nie istnieje.
     */
    @DeleteMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Flight> deleteFlight(@PathVariable long id) {
        return flightService.deleteFlight(id)
            .map(ResponseEntity::ok)
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Pobiera listę pasażerów na podstawie ID lotu.
     *
     * @param id    ID lotu.
     * @return ResponseEntity z listą pasażerów lub kodem NOT_FOUND, jeśli lot nie istnieje.
     */
    @GetMapping(path = "/{id}/passengers")
    @ResponseBody
    public ResponseEntity<List<Passenger>> getPassengers(@PathVariable long id) {
        try {
            List<Passenger> passengers = flightService.getPassengers(id);
            return ResponseEntity.ok(passengers);
        } catch (FlightNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Dodaje nowego pasażera do lotu.
     *
     * @param id    ID lotu.
     * @param passengerId   ID pasażera.
     * @return ResponseEntity z kodem OK lub kodem NOT_FOUND, jeśli lot lub pasażer nie istnieje.
     */
    @PostMapping(path = "/{id}/passengers")
    @ResponseBody
    public ResponseEntity<Void> addPassenger(@PathVariable Long id,
                                             @RequestParam Long passengerId) {
        try {
            flightService.addPassenger(id, passengerId);
            return ResponseEntity.ok().build();
        } catch (FlightNotFoundException | PassengerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Usuwa pasażera z lotu.
     *
     * @param id    ID lotu.
     * @param passengerId   ID pasażera.
     * @return ResponseEntity z kodem OK lub kodem NOT_FOUND, jeśli lot nie istnieje.
     */
    @DeleteMapping(path = "/{id}/passengers")
    @ResponseBody
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id,
                                                @RequestParam Long passengerId) {
        try {
            flightService.deletePassenger(id, passengerId);
            return ResponseEntity.ok().build();
        } catch (FlightNotFoundException  e) {
            return ResponseEntity.notFound().build();
        }
    }
}
