package com.mslup.lot.lotcrud.exception;

import org.springframework.http.HttpStatus;

/**
 * Wyjątek sygnalizujący brak znalezienia lotu.
 */
public class FlightNotFoundException extends ResourceException {
    public FlightNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, String.format("Flight with id = %s not found", id));
    }
}
