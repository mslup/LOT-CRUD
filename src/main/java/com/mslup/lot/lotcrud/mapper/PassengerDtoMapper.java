package com.mslup.lot.lotcrud.mapper;

import com.mslup.lot.lotcrud.dto.PassengerDto;
import com.mslup.lot.lotcrud.model.Passenger;
import java.util.function.Function;
import org.springframework.stereotype.Service;

/**
 * Klasa mapująca obiekt typu {@link PassengerDto} na model lotu {@link Passenger}.
 */
@Service
public class PassengerDtoMapper implements Function<PassengerDto, Passenger> {
    @Override
    public Passenger apply(PassengerDto passengerDto) {
        return Passenger.builder()
            .firstName(passengerDto.firstName())
            .lastName(passengerDto.lastName())
            .phoneNumber(passengerDto.phoneNumber())
            .build();
    }
}
