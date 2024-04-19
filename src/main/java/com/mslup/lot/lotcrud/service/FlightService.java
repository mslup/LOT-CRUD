package com.mslup.lot.lotcrud.service;

import com.mslup.lot.lotcrud.model.Flight;
import com.mslup.lot.lotcrud.repository.FlightRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;

    public Optional<Flight> addFlight(Flight flight) {
        return Optional.of(flightRepository.save(flight));
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
}
