package com.mslup.lot.lotcrud.patcher;

import com.mslup.lot.lotcrud.model.Passenger;

/**
 * Klasa pomocnicza zmieniająca dane pasażera.
 */
public class PassengerPatcher {
    public static Passenger applyPatchToPassenger(Passenger passenger, Passenger valuesToPatch) {
        if (valuesToPatch.getFirstName() != null) {
            passenger.setFirstName(valuesToPatch.getFirstName());
        }
        if (valuesToPatch.getLastName() != null) {
            passenger.setLastName(valuesToPatch.getLastName());
        }
        if (valuesToPatch.getPhoneNumber() != null) {
            passenger.setPhoneNumber(valuesToPatch.getPhoneNumber());
        }
        return passenger;
    }
}
