package com.mslup.lot.lotcrud.service;

import com.mslup.lot.lotcrud.exception.PassengerNotFoundException;
import com.mslup.lot.lotcrud.model.Passenger;
import com.mslup.lot.lotcrud.repository.PassengerRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Serwis obsługujący operacje na pasażerach.
 */
@Service
@RequiredArgsConstructor
public class PassengerService {
    private final PassengerRepository passengerRepository;

    /**
     * Pobiera wszystkich pasażerów.
     *
     * @return Lista wszystkich pasażerów.
     */
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    /**
     * Znajduje pasażera o podanym ID.
     *
     * @param id ID pasażera do znalezienia.
     * @return Pasażer o podanym ID, jeśli istnieje.
     */
    public Optional<Passenger> findPassenger(long id) {
        return passengerRepository.findById(id);
    }

    /**
     * Zapisuje pasażera.
     *
     * @param passenger Pasażer do zapisania.
     * @return Zapisany pasażer.
     */
    public Passenger savePassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    /**
     * Metoda pomocnicza do zastosowania zmiany na pasażerze.
     *
     * @param passenger Aktualny pasażer.
     * @param valuesToPatch Wartości do zaktualizowania.
     * @return Zaktualizowany pasażer.
     */
    private Passenger applyPatchToPassenger(Passenger passenger, Passenger valuesToPatch) {
        if (valuesToPatch.getFirstName() != null) {
            passenger.setFirstName(valuesToPatch.getFirstName());
        }
        if (valuesToPatch.getLastName() != null) {
            passenger.setLastName(valuesToPatch.getLastName());
        }
        if (valuesToPatch.getPhoneNumber() != null) {
            passenger.setPhoneNumber(valuesToPatch.getPhoneNumber());
        }
        return passenger;
    }

    /**
     * Aktualizuje dane pasażera na podstawie podanego ID i wartości do zaktualizowania.
     *
     * @param id ID pasażera do zaktualizowania.
     * @param valuesToPatch Wartości do zaktualizowania.
     * @return Zaktualizowany pasażer.
     * @throws PassengerNotFoundException Jeśli pasażer o podanym ID nie zostanie znaleziony.
     */
    public Passenger patchPassenger(long id, Passenger valuesToPatch)
        throws PassengerNotFoundException {
        Passenger passenger = findPassenger(id).orElseThrow(PassengerNotFoundException::new);

        Passenger patchedPassenger = applyPatchToPassenger(passenger, valuesToPatch);
        passengerRepository.save(patchedPassenger);
        return patchedPassenger;
    }

    /**
     * Usuwa pasażera o podanym ID.
     *
     * @param id ID pasażera do usunięcia.
     * @return Opcjonalny pasażer, jeśli został usunięty.
     */
    public Optional<Passenger> deletePassenger(long id) {
        Optional<Passenger> passenger = passengerRepository.findById(id);
        passengerRepository.deleteById(id);
        return passenger;
    }
}