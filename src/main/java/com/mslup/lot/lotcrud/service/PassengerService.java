package com.mslup.lot.lotcrud.service;

import static com.mslup.lot.lotcrud.patcher.PassengerPatcher.applyPatchToPassenger;

import com.mslup.lot.lotcrud.exception.PassengerNotFoundException;
import com.mslup.lot.lotcrud.model.Passenger;
import com.mslup.lot.lotcrud.repository.PassengerRepository;
import java.util.List;
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
    public List<Passenger> getPassengers() {
        return passengerRepository.findAll();
    }

    /**
     * Znajduje pasażera o podanym ID.
     *
     * @param id ID pasażera do znalezienia.
     * @return Pasażer o podanym ID, jeśli istnieje.
     */
    public Passenger findPassenger(long id) throws PassengerNotFoundException {
        return passengerRepository.findById(id)
            .orElseThrow(() -> new PassengerNotFoundException(id));
    }

    /**
     * Zapisuje pasażera w bazie.
     *
     * @param passenger Pasażer do zapisania.
     * @return Zapisany pasażer.
     */
    public Passenger savePassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }


    /**
     * Aktualizuje dane pasażera na podstawie podanego ID i wartości do zaktualizowania.
     *
     * @param id            ID pasażera do zaktualizowania.
     * @param valuesToPatch Wartości do zaktualizowania.
     * @return Zaktualizowany pasażer.
     * @throws PassengerNotFoundException Jeśli pasażer o podanym ID nie zostanie znaleziony.
     */
    public Passenger patchPassenger(long id, Passenger valuesToPatch)
        throws PassengerNotFoundException {
        Passenger passenger = findPassenger(id);

        Passenger patchedPassenger = applyPatchToPassenger(passenger, valuesToPatch);
        passengerRepository.save(patchedPassenger);
        return patchedPassenger;
    }

    /**
     * Usuwa pasażera o podanym ID.  Jeżeli taki pasażer nie istnieje, nic się nie dzieje.
     *
     * @param id ID pasażera do usunięcia.
     */
    public void deletePassenger(long id) {
        passengerRepository.deleteById(id);
    }
}