package pw.react.backend.web;

import pw.react.backend.models.Aircraft;

public record AircraftDto(Long aircraftId,
    String aircraftName,
    String aircraftType,
    String aircraftRegistration,
    String aircraftDescription) {

    public static final AircraftDto EMPTY = new AircraftDto(0L, "", "", "", "");

    public static AircraftDto valueFrom(Aircraft aircraft) {
        return new AircraftDto(aircraft.getAircraftId(),
            aircraft.getAicraftName(),
            aircraft.getAircraftType(),
            aircraft.getAircraftRegistration(),
            aircraft.getAircraftDescription()
        );
    }

    public static Aircraft convertToAircraft(AircraftDto aircraftDto) {
        Aircraft aircraft = new Aircraft();

        aircraft.setAircraftId(aircraftDto.aircraftId());
        aircraft.setAicraftName(aircraftDto.aircraftName());
        aircraft.setAircraftType(aircraftDto.aircraftType());
        aircraft.setAircraftRegistration(aircraftDto.aircraftRegistration());
        aircraft.setAircraftDescription(aircraftDto.aircraftDescription());

        return aircraft;
    }
}

