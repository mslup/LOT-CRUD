package com.mslup.lot.lotcrud.filter;

import java.time.OffsetDateTime;
import lombok.Builder;

/**
 * Klasa reprezentująca kryteria filtrowania lotów.
 */
@Builder
public class FlightFilterCriteria {
    /**
     * Kod lotniska początkowego.
     */
    public String originAirport;

    /**
     * Kod lotniska docelowego.
     */
    public String destinationAirport;

    /**
     * Data początkowa.
     */
    public OffsetDateTime dateFrom;

    /**
     * Data końcowa.
     */
    public OffsetDateTime dateTo;

    /**
     * Minimalna liczba miejsc.
     */
    public Integer seatsCountFrom;

    /**
     * Maksymalna liczba miejsc.
     */
    public Integer seatsCountTo;
}
