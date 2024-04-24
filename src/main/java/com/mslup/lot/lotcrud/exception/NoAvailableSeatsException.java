package com.mslup.lot.lotcrud.exception;

import org.springframework.http.HttpStatus;

/**
 * Wyjątek sygnalizujący brak wolnych miejsc w danym locie.
 */
public class NoAvailableSeatsException extends ResourceException {
    public NoAvailableSeatsException(Long id) {
        super(HttpStatus.CONFLICT,
            String.format("Flight with id = %s has no available seats left", id));
    }
}