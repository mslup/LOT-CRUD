package com.mslup.lot.lotcrud.repository.custom;

import com.mslup.lot.lotcrud.filter.FlightFilterCriteria;
import com.mslup.lot.lotcrud.model.Flight;
import java.util.List;

public interface FlightRepositoryCustom {
    List<Flight> filterFlights(FlightFilterCriteria criteria);
}
