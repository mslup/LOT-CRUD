package com.mslup.lot.lotcrud.controller;

import com.mslup.lot.lotcrud.dto.PassengerDto;
import com.mslup.lot.lotcrud.exception.PassengerNotFoundException;
import com.mslup.lot.lotcrud.mapper.PassengerDtoMapper;
import com.mslup.lot.lotcrud.model.Passenger;
import com.mslup.lot.lotcrud.service.PassengerService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
 * Kontroler obsługujący zasoby pasażerów.
 */
@RestController
@RequestMapping("/passengers")
@RequiredArgsConstructor
@Tag(name = "Pasażerowie", description = "Operacje do zarządzania bazą pasażerów")
public class PassengerController {
    private final PassengerService passengerService;
    private final PassengerDtoMapper passengerDtoMapper;

    /**
     * Pobiera listę wszystkich pasażerów.
     *
     * @return {@code ResponseEntity} z listą pasażerów.
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Passenger>> getPassengers() {
        return ResponseEntity.ok(passengerService.getPassengers());
    }

    /**
     * Dodaje nowego pasażera.
     *
     * @param passenger Pasażer do dodania.
     * @return {@code ResponseEntity} z dodanym pasażerem.
     */
    @PostMapping
    @ResponseBody
    public ResponseEntity<Passenger> addPassenger(@Valid @RequestBody PassengerDto passenger) {
        return ResponseEntity.ok(
            passengerService.savePassenger(passengerDtoMapper.apply(passenger)));
    }

    /**
     * Pobiera szczegóły pasażera o podanym ID.
     *
     * @param id ID pasażera do znalezienia.
     * @return ResponseEntity ze znalezionym pasażerem.
     */
    @GetMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Passenger> getPassenger(@PathVariable long id) {
        return ResponseEntity.ok(passengerService.findPassenger(id));
    }

    /**
     * Aktualizuje szczegóły pasażera na podstawie ID. Parametry są opcjonalne.
     *
     * @param id          ID pasażera.
     * @param firstName   Nowe imię pasażera (długość od 2 do 40).
     * @param lastName    Nowe nazwisko pasażera (długość od 2 do 40).
     * @param phoneNumber Nowy numer telefonu pasażera (długość od 5 do 20).
     * @return {@code ResponseEntity} z zaktualizowanym pasażerem.
     */
    @PatchMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Passenger> updatePassenger(
        @PathVariable long id,
        @RequestParam Optional<@Size(min = 2, max = 40) String> firstName,
        @RequestParam Optional<@Size(min = 2, max = 40) String> lastName,
        @RequestParam Optional<@Size(min = 5, max = 20) String> phoneNumber)
        throws PassengerNotFoundException {
        Passenger patch = Passenger.builder()
            .firstName(firstName.orElse(null))
            .lastName(lastName.orElse(null))
            .phoneNumber(phoneNumber.orElse(null))
            .build();

        Passenger patchedPassenger = passengerService.patchPassenger(id, patch);
        return ResponseEntity.ok(patchedPassenger);
    }

    /**
     * Usuwa pasażera na podstawie ID. Jeśli pasażer nie istnieje, nic się nie dzieje.
     *
     * @param id ID pasażera.
     * @return {@code ResponseEntity} bez zawartości.
     */
    @DeleteMapping(path = "/{id}")
    @ResponseBody
    @ApiResponse(responseCode = "204", description = "Operacja usuwania powiodła się")
    public ResponseEntity<Void> deletePassenger(@PathVariable long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }
}
