package com.mslup.lot.lotcrud.service;

import com.mslup.lot.lotcrud.exception.FlightNotFoundException;
import com.mslup.lot.lotcrud.filter.FlightFilterCriteria;
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

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Optional<Flight> findFlight(long id) {
        return flightRepository.findById(id);
    }

    public List<Flight> getFlights() {
        return flightRepository.findAll();
    }

    public List<Flight> getFlights(FlightFilterCriteria criteria) {
        return flightRepository.filterFlights(criteria);
    }

    private Flight applyPatchToFlight(Flight flight, Flight valuesToPatch) {
        if (valuesToPatch.getFlightNumber() != null) {
            flight.setFlightNumber(valuesToPatch.getFlightNumber());
        }
        if (valuesToPatch.getOriginAirport() != null) {
            flight.setOriginAirport(valuesToPatch.getOriginAirport());
        }
        if (valuesToPatch.getDestinationAirport() != null) {
            flight.setDestinationAirport(valuesToPatch.getDestinationAirport());
        }
        if (valuesToPatch.getDepartureDateTime() != null) {
            flight.setDepartureDateTime(valuesToPatch.getDepartureDateTime());
        }
        if (valuesToPatch.getAvailableSeatsCount() != -1) {
            flight.setAvailableSeatsCount(valuesToPatch.getAvailableSeatsCount());
        }
        return flight;

    }

    public Flight patchFlight(long id, Flight valuesToPatch)
        throws FlightNotFoundException {
        Flight flight = findFlight(id).orElseThrow(FlightNotFoundException::new);

        Flight patchedFlight = applyPatchToFlight(flight, valuesToPatch);
        flightRepository.save(patchedFlight);
        return patchedFlight;
    }

    public Optional<Flight> deleteFlight(long id) {
        Optional<Flight> flight = flightRepository.findById(id);
        flightRepository.deleteById(id);
        return flight;
    }
}
