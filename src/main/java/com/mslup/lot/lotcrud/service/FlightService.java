package com.mslup.lot.lotcrud.service;

import com.mslup.lot.lotcrud.exception.FlightNotFoundException;
import com.mslup.lot.lotcrud.exception.PassengerNotFoundException;
import com.mslup.lot.lotcrud.filter.FlightFilterCriteria;
import com.mslup.lot.lotcrud.model.Flight;
import com.mslup.lot.lotcrud.model.Passenger;
import com.mslup.lot.lotcrud.repository.FlightRepository;
import com.mslup.lot.lotcrud.repository.PassengerRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Serwis obsługujący operacje na lotach.
 */
@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;

    /**
     * Zapisuje lot.
     *
     * @param flight Lot do zapisania.
     * @return Zapisany lot.
     */
    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    /**
     * Znajduje lot o podanym ID.
     *
     * @param id ID lotu do znalezienia.
     * @return Lot o podanym ID, jeśli istnieje.
     */
    public Optional<Flight> findFlight(long id) {
        return flightRepository.findById(id);
    }

    /**
     * Pobiera wszystkie loty.
     *
     * @return Lista wszystkich lotów.
     */
    public List<Flight> getFlights() {
        return flightRepository.findAll();
    }

    /**
     * Pobiera loty na podstawie określonych kryteriów filtrowania.
     *
     * @param criteria Kryteria filtrowania lotów.
     * @return Lista lotów spełniających podane kryteria.
     */
    public List<Flight> getFlights(FlightFilterCriteria criteria) {
        return flightRepository.filterFlights(criteria);
    }

    // todo: move logic to different class
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

    /**
     * Aktualizuje dane lotu na podstawie podanego ID i wartości do zaktualizowania.
     *
     * @param id ID lotu do zaktualizowania.
     * @param valuesToPatch Wartości do zaktualizowania.
     * @return Zaktualizowany lot.
     * @throws FlightNotFoundException Jeśli lot o podanym ID nie zostanie znaleziony.
     */
    public Flight patchFlight(long id, Flight valuesToPatch)
        throws FlightNotFoundException {
        Flight flight = findFlight(id).orElseThrow(FlightNotFoundException::new);

        Flight patchedFlight = applyPatchToFlight(flight, valuesToPatch);
        flightRepository.save(patchedFlight);
        return patchedFlight;
    }

    /**
     * Usuwa lot o podanym ID.
     *
     * @param id ID lotu do usunięcia.
     * @return Opcjonalny lot, jeśli został usunięty.
     */
    public Optional<Flight> deleteFlight(long id) {
        Optional<Flight> flight = flightRepository.findById(id);
        flightRepository.deleteById(id);
        return flight;
    }

    /**
     * Pobiera listę pasażerów przypisanych do lotu o podanym ID.
     *
     * @param id ID lotu.
     * @return Lista pasażerów przypisanych do lotu.
     * @throws FlightNotFoundException Jeśli lot o podanym ID nie zostanie znaleziony.
     */
    public List<Passenger> getPassengers(long id) throws FlightNotFoundException {
        return flightRepository.findById(id)
            .map(value -> value.getPassengers().stream().toList())
            .orElseThrow(FlightNotFoundException::new);
    }

    /**
     * Dodaje pasażera do lotu o podanym ID.
     *
     * @param flightId ID lotu.
     * @param passengerId ID pasażera.
     * @throws FlightNotFoundException Jeśli lot o podanym ID nie zostanie znaleziony.
     * @throws PassengerNotFoundException Jeśli pasażer o podanym ID nie zostanie znaleziony.
     */
    public void addPassenger(long flightId, long passengerId)
        throws FlightNotFoundException, PassengerNotFoundException {
        Flight flight =
            flightRepository.findById(flightId).orElseThrow(FlightNotFoundException::new);
        Passenger passenger =
            passengerRepository.findById(passengerId).orElseThrow(PassengerNotFoundException::new);

        flight.addPassenger(passenger);

        flightRepository.save(flight);
    }

    /**
     * Usuwa pasażera o podanym ID z lotu o podanym ID.
     *
     * @param flightId ID lotu.
     * @param passengerId ID pasażera.
     * @throws FlightNotFoundException Jeśli lot o podanym ID nie zostanie znaleziony.
     */
    // todo: rename?? to remove, or use 'bookings'
    public void deletePassenger(long flightId, long passengerId) throws FlightNotFoundException {
        Flight flight =
            flightRepository.findById(flightId).orElseThrow(FlightNotFoundException::new);

        flight.deletePassenger(passengerId);

        flightRepository.save(flight);
    }
}
