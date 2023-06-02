package pw.react.backend.web;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import pw.react.backend.controller.AircraftController;
import pw.react.backend.models.Aircraft;
import pw.react.backend.models.AircraftImage;

public record AircraftDto(Long aircraftId,
    String aircraftName,
    String aircraftType,
    String aircraftRegistration,
    String aircraftDescription,
    String aircraftImage
) {
    public static final AircraftDto EMPTY = new AircraftDto(0L, "", "", "", "", "");

    public static AircraftDto valueFrom(Aircraft aircraft, HttpServletRequest httpServletRequest) {
        Set<AircraftImage> aircraftImages = aircraft.getImages();
        int numAircraftImages = aircraftImages == null ? 0 : aircraftImages.size();

        return new AircraftDto(aircraft.getAircraftId(),
            aircraft.getAicraftName(),
            aircraft.getAircraftType(),
            aircraft.getAircraftRegistration(),
            aircraft.getAircraftDescription(),

            numAircraftImages > 0 ?
            String.format("%s://%s:%d%s/%d/images/%d",
                httpServletRequest.getScheme(),
                httpServletRequest.getServerName(),
                httpServletRequest.getServerPort(),
                AircraftController.AIRCRAFT_PATH,
                aircraft.getAircraftId(),
                numAircraftImages - 1
            ) : "https://www.vhv.rs/viewpic/hThRw_airplane-silhouette-clipart-png-transparent-png"
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

