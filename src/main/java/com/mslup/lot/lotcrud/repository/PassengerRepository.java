package com.mslup.lot.lotcrud.repository;

import com.mslup.lot.lotcrud.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

}