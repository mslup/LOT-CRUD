package com.mslup.lot.lotcrud.controller;

import com.mslup.lot.lotcrud.exception.FlightNotFoundException;
import com.mslup.lot.lotcrud.exception.NoAvailableSeatsException;
import com.mslup.lot.lotcrud.exception.PassengerNotFoundException;
import com.mslup.lot.lotcrud.model.Passenger;
import com.mslup.lot.lotcrud.service.FlightService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kontroler obsługujący operacje związane z pasażerami przypisanymi do danego lotu.
 */
@RequestMapping("/flights")
@RequiredArgsConstructor
@RestController
@Tag(name = "Rezerwacje lotów",
    description = "Operacje do zarządzania pasażerami przypisanymi do danego lotu")
public class FlightPassengerController {
    private final FlightService flightService;

    /**
     * Pobiera listę pasażerów na podstawie ID lotu.
     *
     * @param id ID lotu.
     * @return {@code ResponseEntity} z listą pasażerów.
     * @throws FlightNotFoundException Jeśli lot o podanym ID nie został znaleziony.
     */
    @GetMapping(path = "/{id}/passengers")
    @ResponseBody
    public ResponseEntity<List<Passenger>> getPassengers(@PathVariable long id)
        throws FlightNotFoundException {
        List<Passenger> passengers = flightService.getPassengers(id);
        return ResponseEntity.ok(passengers);
    }

    /**
     * Dodaje nowego pasażera do lotu.
     *
     * @param id          ID lotu.
     * @param passengerId ID pasażera.
     * @return {@code ResponseEntity} ze statusem 200, jeśli operacja się powiodła.
     * @throws PassengerNotFoundException Jeśli pasażer o podanym ID nie został znaleziony.
     * @throws FlightNotFoundException    Jeśli lot o podanym ID nie został znaleziony.
     * @throws NoAvailableSeatsException  Jeśli lot nie ma dostępnych miejsc do zarezerwowania.
     */
    @PostMapping(path = "/{id}/passengers")
    @ResponseBody
    public ResponseEntity<Void> addPassenger(@PathVariable Long id,
                                             @RequestParam Long passengerId)
        throws PassengerNotFoundException, FlightNotFoundException, NoAvailableSeatsException {
        flightService.addPassenger(id, passengerId);
        return ResponseEntity.ok().build();
    }

    /**
     * Usuwa pasażera z lotu.
     *
     * @param id          ID lotu.
     * @param passengerId ID pasażera.
     * @return {@code ResponseEntity} bez zawartości. Jeśli lot nie istnieje,
     *     nic się nie dzieje.
     */
    @DeleteMapping(path = "/{id}/passengers")
    @ApiResponse(responseCode = "204", description = "Operacja usuwania powiodła się")
    @ResponseBody
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id,
                                                @RequestParam Long passengerId) {
        flightService.deletePassenger(id, passengerId);
        return ResponseEntity.noContent().build();
    }
}
