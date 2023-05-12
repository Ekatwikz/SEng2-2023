package pw.react.backend.web;

import pw.react.backend.models.AircraftImage;

public record AircraftImageInfo(Long aircraftImageId,
        Long aircraftId,
        String fileName,
        String fileType,
        long fileSize) {
    public static AircraftImageInfo valueFrom(AircraftImage aircraftImage) {
        return new AircraftImageInfo(
                aircraftImage.getAircraftImageId(),
                aircraftImage.getAircraft().getAircraftId(),
                aircraftImage.getFileName(),
                aircraftImage.getFileType(),
                aircraftImage.getFileSize());
    }
}

