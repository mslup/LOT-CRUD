package com.mslup.lot.lotcrud.service;

import com.mslup.lot.lotcrud.model.Passenger;
import com.mslup.lot.lotcrud.repository.PassengerRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private final PassengerRepository passengerRepository;

    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Optional<Passenger> savePassenger(Passenger passenger) {
        return Optional.of(passengerRepository.save(passenger));
    }
}