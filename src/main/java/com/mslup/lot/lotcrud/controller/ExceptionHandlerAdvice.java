package com.mslup.lot.lotcrud.controller;

import com.mslup.lot.lotcrud.exception.ResourceException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Klasa definiująca obsługę wyjatków przez kontrolery REST.
 */
@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    /**
     * Obsługa wyjątków typu {@link ResourceException}.
     *
     * @param e Wyjątek do obsłużenia
     * @return Odpowiedź ze statusem HTTP i wiadomością błędu
     */
    @ExceptionHandler(ResourceException.class)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "404", description = "Zasób nieznaleziony", content = @Content),
        @ApiResponse(responseCode = "409", description = "Konflikt zasobów", content = @Content)})
    public ResponseEntity<?> handleException(ResourceException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

    /**
     * Obsługuje wyjątek {@link MethodArgumentNotValidException}.
     *
     * @param ex      wyjątek rzucany, gdy argument metody nie jest prawidłowy
     * @param headers nagłówki HTTP, które będą wysłane do klienta
     * @param status  status odpowiedzi HTTP
     * @param request bieżące żądanie sieciowe
     * @return odpowiedź {@code ResponseEntity} zawierająca szczegóły błędów walidacji
     */
    @Override
    @ApiResponse(responseCode = "400", description = "Nieprawidłowe żądanie")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
        WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
