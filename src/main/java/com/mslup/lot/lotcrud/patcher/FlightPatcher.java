package com.mslup.lot.lotcrud.patcher;

import com.mslup.lot.lotcrud.model.Flight;

/**
 * Klasa pomocnicza zmieniająca dane lotu.
 */
public class FlightPatcher {
    public static Flight applyPatchToFlight(Flight flight, Flight valuesToPatch) {
        if (valuesToPatch.getFlightNumber() != null) {
            flight.setFlightNumber(valuesToPatch.getFlightNumber());
        }
        if (valuesToPatch.getOriginAirport() != null) {
            flight.setOriginAirport(valuesToPatch.getOriginAirport());
        }
        if (valuesToPatch.getDestinationAirport() != null) {
            flight.setDestinationAirport(valuesToPatch.getDestinationAirport());
        }
        if (valuesToPatch.getDepartureDateTime() != null) {
            flight.setDepartureDateTime(valuesToPatch.getDepartureDateTime());
        }
        if (valuesToPatch.getAvailableSeatsCount() != -1) {
            flight.setAvailableSeatsCount(valuesToPatch.getAvailableSeatsCount());
        }
        return flight;
    }
}
