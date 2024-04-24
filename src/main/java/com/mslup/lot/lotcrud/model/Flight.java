package com.mslup.lot.lotcrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mslup.lot.lotcrud.exception.NoAvailableSeatsException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

/**
 * Klasa reprezentująca lot.
 */
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

    /**
     * Numer lotu.
     */
    @Column(nullable = false)
    //@NotNull(message = "Flight number cannot be null - model")
    private String flightNumber;

    /**
     * Kod lotniska początkowego.
     */
    @Column(nullable = false)
    private String originAirport;

    /**
     * Kod lotniska docelowego.
     */
    @Column(nullable = false)
    private String destinationAirport;

    /**
     * Data i godzina odlotu.
     */
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    private OffsetDateTime departureDateTime;

    /**
     * Liczba dostępnych miejsc.
     */
    @Column(nullable = false)
    private int availableSeatsCount;

    /**
     * Pasażerowie przypisani do lotu.
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "flight_passenger",
        joinColumns = @JoinColumn(name = "flight_id"),
        inverseJoinColumns = @JoinColumn(name = "passenger_id")
    )
    @JsonIgnore
    private Set<Passenger> passengers;

    /**
     * Dodaje pasażera do lotu.
     *
     * @param passenger Pasażer do dodania.
     */
    public void addPassenger(Passenger passenger) throws NoAvailableSeatsException {
        if (passengers.contains(passenger)) {
            return;
        }

        if (availableSeatsCount == 0) {
            throw new NoAvailableSeatsException(id);
        }

        passengers.add(passenger);
        availableSeatsCount--;
    }

    /**
     * Usuwa pasażera z lotu.
     *
     * @param passenger Pasażer do usunięcia.
     */
    @Transactional
    public void deletePassenger(Passenger passenger) {
        if (!passengers.remove(passenger)) {
            return;
        }
        availableSeatsCount++;
    }
}
