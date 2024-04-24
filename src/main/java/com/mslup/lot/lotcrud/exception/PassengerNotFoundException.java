package com.mslup.lot.lotcrud.exception;

import org.springframework.http.HttpStatus;

/**
 * Wyjątek sygnalizujący brak znalezienia pasażera.
 */
public class PassengerNotFoundException extends ResourceException {
    public PassengerNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, String.format("Passenger with id = %s not found", id));
    }
}
