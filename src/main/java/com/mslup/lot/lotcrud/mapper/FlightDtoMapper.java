package com.mslup.lot.lotcrud.mapper;

import com.mslup.lot.lotcrud.dto.FlightDto;
import com.mslup.lot.lotcrud.model.Flight;
import java.util.function.Function;
import org.springframework.stereotype.Service;

@Service
public class FlightDtoMapper implements Function<FlightDto, Flight> {
    @Override
    public Flight apply(FlightDto flight) {
        return null; // todo
    }
}
