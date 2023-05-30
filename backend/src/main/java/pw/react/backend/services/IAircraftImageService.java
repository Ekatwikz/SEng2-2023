package pw.react.backend.services;

import org.springframework.web.multipart.MultipartFile;

import pw.react.backend.models.AircraftImage;

public interface IAircraftImageService {
    AircraftImage storeAircraftImage(Long aircraftId, MultipartFile file);
}

