package com.mslup.lot.lotcrud.model;

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

    // Numer lotu.
    @Column(nullable = true)
    private String flightNumber;

    // Kod lotniska początkowego.
    @Column(nullable = true)
    private String originAirport;

    // Kod lotniska docelowego.
    @Column(nullable = true)
    private String destinationAirport;

    // Data i godzina odlotu.
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = true)
    private OffsetDateTime departureDateTime;

    // Liczba dostępnych miejsc.
    @Column(nullable = true)
    private int availableSeatsCount;

    // Pasażerowie przypisani do lotu.
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
        name = "flight_passenger",
        joinColumns = @JoinColumn(name = "flight_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id")
    )
    private Set<Passenger> passengers;

    /**
     * Dodaje pasażera do lotu.
     *
     * @param passenger Pasażer do dodania.
     */
    @Transactional(noRollbackFor = Exception.class)
    public void addPassenger(Passenger passenger) {
        if (passengers.contains(passenger) || availableSeatsCount == 0) {
            return;
        }

        passengers.add(passenger);
        availableSeatsCount--;
    }

    /**
     * Usuwa pasażera z lotu.
     *
     * @param passengerId ID pasażera do usunięcia.
     * @return true jeśli pasażer został usunięty, w przeciwnym razie false.
     */
    @Transactional
    public boolean deletePassenger(Long passengerId) {
        if (passengers.removeIf(p -> p.getId() == passengerId)) {
            availableSeatsCount++;
            return true;
        }
        return false;
    }
}
