package com.mslup.lot.lotcrud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.mslup.lot.lotcrud.exception.PassengerNotFoundException;
import com.mslup.lot.lotcrud.model.Passenger;
import com.mslup.lot.lotcrud.repository.PassengerRepository;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private final PassengerRepository passengerRepository;
    private final ObjectMapper objectMapper;

    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Optional<Passenger> findPassenger(long id) {
        return passengerRepository.findById(id);
    }

    public Passenger savePassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

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

    public Passenger patchPassenger(long id, Passenger valuesToPatch)
        throws PassengerNotFoundException {
        Passenger passenger = findPassenger(id).orElseThrow(PassengerNotFoundException::new);

        Passenger patchedPassenger = applyPatchToPassenger(passenger, valuesToPatch);
        passengerRepository.save(patchedPassenger);
        return patchedPassenger;
    }
}