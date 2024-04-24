package com.mslup.lot.lotcrud.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Klasa reprezentująca pasażera.
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "passengers")
public class Passenger {
    @Id
    @GeneratedValue
    private long id;

    /**
     * Imię pasażera.
     */
    @Column(nullable = true)
    private String firstName;

    /**
     * Nazwisko pasażera.
     */
    @Column(nullable = true)
    private String lastName;

    /**
     * Numer telefonu pasażera.
     */
    @Column(nullable = true)
    private String phoneNumber;

    /**
     * Loty, na które pasażer dokonał rezerwacji.
     */
    @ManyToMany(
        mappedBy = "passengers",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER
    )
    private Set<Flight> bookings;
}
