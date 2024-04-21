package com.mslup.lot.lotcrud.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@Table(name = "flights")
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    @Id
    @GeneratedValue
    private long id;

    // todo: disable nullable
    @Column(nullable = true)
    private String flightNumber;

    @Column(nullable = true)
    private String originAirport;

    @Column(nullable = true)
    private String destinationAirport;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE",
        nullable = true)
    private OffsetDateTime departureDateTime;

    @Column(nullable = true)
    private long availableSeatsCount;
}
