package com.mslup.lot.lotcrud.controller;

import com.mslup.lot.lotcrud.exception.FlightNotFoundException;
import com.mslup.lot.lotcrud.filter.FlightFilterCriteria;
import com.mslup.lot.lotcrud.model.Flight;
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

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Flight>> getFlights(
        @RequestParam Optional<String> originAirport,
        @RequestParam Optional<String> destinationAirport,
        @RequestParam Optional<OffsetDateTime> dateFrom,
        @RequestParam Optional<OffsetDateTime> dateTo,
        @RequestParam Optional<Long> seatsCountFrom,
        @RequestParam Optional<Long> seatsCountTo
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

    @PostMapping
    ResponseEntity<Flight> addFlight(@RequestBody Flight flight) {
        return new ResponseEntity<>(flightService.saveFlight(flight), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Flight> getFlight(@PathVariable long id) {
        return flightService.findFlight(id)
            .map(ResponseEntity::ok)
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Flight> updateFlight(
        @PathVariable long id,
        @RequestParam Optional<String> flightNumber,
        @RequestParam Optional<String> originAirport,
        @RequestParam Optional<String> destinationAirport,
        @RequestParam Optional<OffsetDateTime> departureDateTime,
        @RequestParam Optional<Long> availableSeatsCount) {
        Flight patch = Flight.builder()
            .flightNumber(flightNumber.orElse(null))
            .originAirport(originAirport.orElse(null))
            .destinationAirport(destinationAirport.orElse(null))
            .departureDateTime(departureDateTime.orElse(null))
            .availableSeatsCount(availableSeatsCount.orElse(-1L))
            .build();

        try {
            Flight patchedFlight = flightService.patchFlight(id, patch);
            return ResponseEntity.ok(patchedFlight);
        } catch (FlightNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Flight> deleteFlight(@PathVariable long id) {
        return flightService.deleteFlight(id)
            .map(ResponseEntity::ok)
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
