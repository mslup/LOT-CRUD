package com.mslup.lot.lotcrud.controller;

import com.mslup.lot.lotcrud.model.Flight;
import com.mslup.lot.lotcrud.service.FlightService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<List<Flight>> getFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @PostMapping
    ResponseEntity<Flight> addFlight(@RequestBody Flight flight) {
        Optional<Flight> savedFlight = flightService.addFlight(flight);
        // todo: sensible error statuses
        return savedFlight.map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
