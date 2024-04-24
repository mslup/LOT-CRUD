package com.mslup.lot.lotcrud.controller;

import com.mslup.lot.lotcrud.dto.FlightDto;
import com.mslup.lot.lotcrud.exception.FlightNotFoundException;
import com.mslup.lot.lotcrud.filter.FlightFilterCriteria;
import com.mslup.lot.lotcrud.mapper.FlightDtoMapper;
import com.mslup.lot.lotcrud.model.Flight;
import com.mslup.lot.lotcrud.service.FlightService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.OffsetDateTime;
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
 * Kontroler obsługujący zasoby lotów.
 */
@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
@Tag(name = "Loty", description = "Operacje do zarządzania bazą lotów")
public class FlightController {
    private final FlightService flightService;
    private final FlightDtoMapper flightDtoMapper;

    /**
     * Pobiera listę lotów na podstawie kryteriów filtrowania.
     * Parametry mogą być puste.
     * Jeśli kryteria są puste, zwraca wszystkie loty.
     *
     * @param originAirport      Kod lotniska początkowego.
     * @param destinationAirport Kod lotniska docelowego.
     * @param dateFrom           Data początkowa (w formacie ISO-8601: YYYY-MM-DDThh:mm:ss±hh:mm).
     * @param dateTo             Data końcowa (w formacie ISO-8601: YYYY-MM-DDThh:mm:ss±hh:mm).
     * @param seatsCountFrom     Minimalna liczba miejsc.
     * @param seatsCountTo       Maksymalna liczba miejsc.
     * @return {@code ResponseEntity} z listą lotów spełniających kryteria.
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Flight>> getFlights(@RequestParam Optional<String> originAirport,
                                                   @RequestParam
                                                   Optional<String> destinationAirport,
                                                   @RequestParam Optional<OffsetDateTime> dateFrom,
                                                   @RequestParam Optional<OffsetDateTime> dateTo,
                                                   @RequestParam Optional<Integer> seatsCountFrom,
                                                   @RequestParam Optional<Integer> seatsCountTo) {
        FlightFilterCriteria criteria =
            FlightFilterCriteria.builder().originAirport(originAirport.orElse(null))
                .destinationAirport(destinationAirport.orElse(null)).dateFrom(dateFrom.orElse(null))
                .dateTo(dateTo.orElse(null)).seatsCountFrom(seatsCountFrom.orElse(null))
                .seatsCountTo(seatsCountTo.orElse(null)).build();

        return ResponseEntity.ok(flightService.getFlights(criteria));
    }

    /**
     * Dodaje nowy lot.
     *
     * @param flight Lot do dodania.
     * @return {@code ResponseEntity} z dodanym lotem.
     */
    @PostMapping
    @ResponseBody
    ResponseEntity<Flight> addFlight(@Valid @RequestBody FlightDto flight) {
        return ResponseEntity.ok(flightService.saveFlight(flightDtoMapper.apply(flight)));
    }

    /**
     * Pobiera szczegóły lotu na podstawie ID.
     *
     * @param id ID lotu.
     * @return {@code ResponseEntity} ze znalezionym lotem.
     * @throws FlightNotFoundException Jeśli lot nie został znaleziony
     */
    @GetMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Flight> getFlight(@PathVariable long id) throws FlightNotFoundException {
        return ResponseEntity.ok(flightService.findFlight(id));
    }

    /**
     * Aktualizuje szczegóły lotu o danym ID na podstawie podanych parametrów.
     * Parametry mogą być puste.
     *
     * @param id                  ID lotu.
     * @param flightNumber        Nowy numer lotu.
     * @param originAirport       Nowy kod lotniska początkowego.
     * @param destinationAirport  Nowy kod lotniska docelowego.
     * @param departureDateTime   Nowa data i godzina odlotu
     *                            (w formacie ISO-8601: YYYY-MM-DDThh:mm:ss±hh:mm).
     * @param availableSeatsCount Nowa liczba dostępnych miejsc (w przedziale od 10 do 500).
     * @return {@code ResponseEntity} z zaktualizowanym lotem.
     */
    @PatchMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Flight> updateFlight(@PathVariable long id,
                                               @RequestParam Optional<String> flightNumber,
                                               @RequestParam Optional<String> originAirport,
                                               @RequestParam Optional<String> destinationAirport,
                                               @RequestParam
                                               Optional<OffsetDateTime> departureDateTime,
                                               @RequestParam
                                               Optional<@Min(value = 10) @Max(value = 500) Integer>
                                                   availableSeatsCount) {
        Flight patch = Flight.builder().flightNumber(flightNumber.orElse(null))
            .originAirport(originAirport.orElse(null))
            .destinationAirport(destinationAirport.orElse(null))
            .departureDateTime(departureDateTime.orElse(null))
            .availableSeatsCount(availableSeatsCount.orElse(-1)).build();

        Flight patchedFlight = flightService.patchFlight(id, patch);
        return ResponseEntity.ok(patchedFlight);
    }

    /**
     * Usuwa lot na podstawie ID. Jeśli lot nie istnieje, nic się nie dzieje.
     *
     * @param id ID lotu.
     * @return {@code ResponseEntity} bez zawartości.
     */
    @DeleteMapping(path = "/{id}")
    @ApiResponse(responseCode = "204", description = "Operacja usuwania powiodła się")
    @ResponseBody
    public ResponseEntity<Void> deleteFlight(@PathVariable long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

}
