package com.mslup.lot.lotcrud.service;

import static com.mslup.lot.lotcrud.patcher.FlightPatcher.applyPatchToFlight;

import com.mslup.lot.lotcrud.exception.FlightNotFoundException;
import com.mslup.lot.lotcrud.exception.NoAvailableSeatsException;
import com.mslup.lot.lotcrud.exception.PassengerNotFoundException;
import com.mslup.lot.lotcrud.filter.FlightFilterCriteria;
import com.mslup.lot.lotcrud.model.Flight;
import com.mslup.lot.lotcrud.model.Passenger;
import com.mslup.lot.lotcrud.repository.FlightRepository;
import com.mslup.lot.lotcrud.repository.PassengerRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
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
     * Zapisuje lot w bazie.
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
     * @throws FlightNotFoundException Jeśli lot o podanym ID nie zostanie znaleziony.
     */
    public Flight findFlight(long id) throws FlightNotFoundException {
        return flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException(id));
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

    /**
     * Aktualizuje dane lotu na podstawie podanego ID i wartości do zaktualizowania.
     *
     * @param id            ID lotu do zaktualizowania.
     * @param valuesToPatch Wartości do zaktualizowania.
     * @return Zaktualizowany lot.
     * @throws FlightNotFoundException Jeśli lot o podanym ID nie zostanie znaleziony.
     */
    public Flight patchFlight(long id, Flight valuesToPatch) throws FlightNotFoundException {
        Flight flight = findFlight(id);

        Flight patchedFlight = applyPatchToFlight(flight, valuesToPatch);
        flightRepository.save(patchedFlight);
        return patchedFlight;
    }

    /**
     * Usuwa lot o podanym ID. Jeżeli taki lot nie istnieje, nic się nie dzieje.
     *
     * @param id ID lotu do usunięcia.
     */
    public void deleteFlight(long id) {
        flightRepository.deleteById(id);
    }

    /**
     * Pobiera listę pasażerów przypisanych do lotu o podanym ID.
     *
     * @param flightId ID lotu.
     * @return Lista pasażerów przypisanych do lotu.
     * @throws FlightNotFoundException Jeśli lot o podanym ID nie zostanie znaleziony.
     */
    public List<Passenger> getPassengers(long flightId) throws FlightNotFoundException {
        return flightRepository.findById(flightId)
            .map(value -> value.getPassengers().stream().toList())
            .orElseThrow(() -> new FlightNotFoundException(flightId));
    }

    /**
     * Dodaje pasażera do lotu o podanym ID.
     *
     * @param flightId    ID lotu.
     * @param passengerId ID pasażera.
     * @throws FlightNotFoundException    Jeśli lot o podanym ID nie zostanie znaleziony.
     * @throws PassengerNotFoundException Jeśli pasażer o podanym ID nie zostanie znaleziony.
     */
    @Transactional
    public void addPassenger(long flightId, long passengerId)
        throws FlightNotFoundException, PassengerNotFoundException, NoAvailableSeatsException {
        Flight flight = flightRepository.findById(flightId)
            .orElseThrow(() -> new FlightNotFoundException(flightId));
        Passenger passenger = passengerRepository.findById(passengerId)
            .orElseThrow(() -> new PassengerNotFoundException(passengerId));

        flight.addPassenger(passenger);
        passenger.getBookings().add(flight);
        flightRepository.save(flight);
        passengerRepository.save(passenger);
    }

    /**
     * Usuwa pasażera o podanym ID z lotu o podanym ID.
     *
     * @param flightId    ID lotu.
     * @param passengerId ID pasażera.
     * @throws FlightNotFoundException Jeśli lot o podanym ID nie zostanie znaleziony.
     * @throws PassengerNotFoundException Jeśli pasażer o podanym ID nie zostanie znaleziony.
     */
    @Transactional
    public void deletePassenger(long flightId, long passengerId)
        throws FlightNotFoundException,
        PassengerNotFoundException {
        Flight flight = flightRepository.findById(flightId)
            .orElseThrow(() -> new FlightNotFoundException(flightId));
        Passenger passenger = passengerRepository.findById(passengerId)
            .orElseThrow(() -> new PassengerNotFoundException(passengerId));

        flight.deletePassenger(passenger);
        passenger.getBookings().remove(flight);

        flightRepository.save(flight);
        passengerRepository.save(passenger);
    }
}
