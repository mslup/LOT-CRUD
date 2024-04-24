package com.mslup.lot.lotcrud.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) dla danych pasażera używany w zapytaniu POST.
 * Dane są walidowane.
 */
public record PassengerDto(@NotNull(message = "First name cannot be null")
                           @Size(min = 2, max = 40,
                               message = "First name has to have between 2 and 40 characters")
                           String firstName,
                           @Size(min = 2, max = 40,
                               message = "Last name has to have between 2 and 40 characters")
                           String lastName,
                           @Size(min = 5, max = 20,
                               message = "Phone number has to have between 5 and 20 characters")
                           String phoneNumber) {
}
