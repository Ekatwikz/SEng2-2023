package pw.react.backend.services;

import pw.react.backend.controller.BaseLoggable;
import pw.react.backend.exceptions.ValidationException;
import pw.react.backend.models.Aircraft;

public class AircraftService extends BaseLoggable implements IAircraftService {
    public static void validateAircraft(Aircraft aircraft) {
        if (aircraft == null) {
            log.error("Aircraft is null.");
            throw new ValidationException("NULL aircraft");
        }

        if (!isValid(aircraft.getAicraftName())) {
            log.error("Empty aircraft name.");
            throw new ValidationException("Empty aircraft name.");
        }

        if (!isValid(aircraft.getAircraftDescription())) {
            log.error("Empty aircraft description.");
            throw new ValidationException("Empty aircraft description.");
        }

        if (!isValid(aircraft.getAircraftRegistration())) {
            log.error("Empty aircraft registration.");
            throw new ValidationException("Empty aircraft registration.");
        }

        if (!isValid(aircraft.getAircraftType())) {
            log.error("Empty aircraft type.");
            throw new ValidationException("Empty aircraft type.");
        }
    }

    private static boolean isValid(String value) {
        return value != null && !value.isBlank();
    }
}

