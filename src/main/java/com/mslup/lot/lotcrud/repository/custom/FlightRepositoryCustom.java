package com.mslup.lot.lotcrud.repository.custom;

import com.mslup.lot.lotcrud.filter.FlightFilterCriteria;
import com.mslup.lot.lotcrud.model.Flight;
import java.util.List;

/**
 * Interfejs dla niestandardowej implementacji repozytorium lotów.
 */
public interface FlightRepositoryCustom {
    List<Flight> filterFlights(FlightFilterCriteria criteria);
}
