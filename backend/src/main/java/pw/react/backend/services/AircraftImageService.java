package pw.react.backend.services;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import pw.react.backend.controller.BaseLoggable;
import pw.react.backend.dao.AircraftImageRepository;
import pw.react.backend.dao.AircraftRepository;
import pw.react.backend.exceptions.InvalidFileException;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.models.Aircraft;
import pw.react.backend.models.AircraftImage;

public class AircraftImageService extends BaseLoggable implements IAircraftImageService {
    @Autowired
    AircraftRepository aircraftRepository;

    @Autowired
    AircraftImageRepository aircraftImageRepository;

    @Override
    public AircraftImage storeAircraftImage(Long aircraftId, MultipartFile file) {
        Optional<Aircraft> maybeAircraft = aircraftRepository.findById(aircraftId);
        if (maybeAircraft.isEmpty()) {
            throw new ResourceNotFoundException("Aircraft not found. Can't store image.");
        }

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new InvalidFileException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            AircraftImage aircraftImage = new AircraftImage(maybeAircraft.get(), file, file.getBytes());
            return aircraftImageRepository.save(aircraftImage);
        } catch (IOException ex) {
            throw new InvalidFileException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
