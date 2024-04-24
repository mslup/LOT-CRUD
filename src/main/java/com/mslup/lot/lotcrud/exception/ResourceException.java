package com.mslup.lot.lotcrud.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Klasa bazowa dla niestandardowych wyjątków zdefiniowanych w aplikacji
 * oznaczająca błędy związane z zasobami aplikacji.
 */
@Getter
public class ResourceException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ResourceException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
