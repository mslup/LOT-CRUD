package com.mslup.lot.lotcrud.repository;

import com.mslup.lot.lotcrud.model.Flight;
import com.mslup.lot.lotcrud.repository.custom.FlightRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repozytorium dla klasy Flight.
 */
public interface FlightRepository extends JpaRepository<Flight, Long>, FlightRepositoryCustom {
}
