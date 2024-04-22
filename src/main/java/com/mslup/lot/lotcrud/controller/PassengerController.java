package com.mslup.lot.lotcrud.controller;

import com.mslup.lot.lotcrud.exception.PassengerNotFoundException;
import com.mslup.lot.lotcrud.model.Passenger;
import com.mslup.lot.lotcrud.service.PassengerService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/passengers")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Passenger>> getPassengers() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Passenger> addPassenger(@RequestBody Passenger passenger) {
        return new ResponseEntity<>(passengerService.savePassenger(passenger), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Passenger> getPassenger(@PathVariable long id) {
        return passengerService.findPassenger(id)
            .map(ResponseEntity::ok)
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Passenger> updatePassenger(
        @PathVariable long id,
        @RequestParam Optional<String> firstName,
        @RequestParam Optional<String> lastName,
        @RequestParam Optional<String> phoneNumber) {
        Passenger patch = Passenger.builder()
            .firstName(firstName.orElse(null))
            .lastName(lastName.orElse(null))
            .phoneNumber(phoneNumber.orElse(null))
            .build();

        try {
            Passenger patchedPassenger = passengerService.patchPassenger(id, patch);
            return ResponseEntity.ok(patchedPassenger);
        } catch (PassengerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{id}")
    @ResponseBody
    public ResponseEntity<Passenger> deletePassenger(@PathVariable long id) {
        return passengerService.deletePassenger(id)
            .map(ResponseEntity::ok)
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}


