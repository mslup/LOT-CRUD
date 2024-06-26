package com.mslup.lot.lotcrud;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Główna klasa aplikacji.
 */
@SpringBootApplication
@RequiredArgsConstructor
public class LotCrudApplication {
    public static void main(String[] args) {
        SpringApplication.run(LotCrudApplication.class, args);
    }
}
