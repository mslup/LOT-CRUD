package com.mslup.lot.lotcrud.filter;

import jakarta.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public class FlightFilterCriteria {
    public String originAirport;
    public String destinationAirport;
    public OffsetDateTime dateFrom;
    public OffsetDateTime dateTo;
    public Long seatsCountFrom;
    public Long seatsCountTo;
}
