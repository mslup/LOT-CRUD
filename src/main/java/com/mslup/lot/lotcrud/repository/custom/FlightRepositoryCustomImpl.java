package com.mslup.lot.lotcrud.repository.custom;

import com.mslup.lot.lotcrud.filter.FlightFilterCriteria;
import com.mslup.lot.lotcrud.model.Flight;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FlightRepositoryCustomImpl implements FlightRepositoryCustom {
    private final EntityManager em;


    @Override
    public List<Flight> filterFlights(FlightFilterCriteria criteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);

        Root<Flight> flight = cq.from(Flight.class);
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.originAirport != null) {
            predicates.add(cb.equal(flight.get("originAirport"), criteria.originAirport));
        }
        if (criteria.destinationAirport != null) {
            predicates.add(cb.equal(flight.get("destinationAirport"), criteria.destinationAirport));
        }
        if (criteria.dateFrom != null) {
            predicates.add(
                cb.greaterThanOrEqualTo(flight.get("departureDateTime"), criteria.dateFrom));
        }
        if (criteria.dateTo != null) {
            predicates.add(
                cb.lessThanOrEqualTo(flight.get("departureDateTime"), criteria.dateTo));
        }
        if (criteria.seatsCountFrom != null) {
            predicates.add(
                cb.greaterThanOrEqualTo(flight.get("availableSeatsCount"),
                    criteria.seatsCountFrom));
        }
        if (criteria.seatsCountTo != null) {
            predicates.add(
                cb.lessThanOrEqualTo(flight.get("availableSeatsCount"),
                    criteria.seatsCountTo));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }
}
