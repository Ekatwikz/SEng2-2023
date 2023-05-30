package pw.react.backend.controller;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import pw.react.backend.dao.AircraftImageRepository;
import pw.react.backend.dao.AircraftRepository;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.models.Aircraft;
import pw.react.backend.models.AircraftImage;
import pw.react.backend.services.IAircraftImageService;
import pw.react.backend.web.AircraftImageInfo;

@RestController
@RequestMapping(path = AircraftController.AIRCRAFT_PATH)
public class AircraftImageController extends BaseLoggable {
    @Autowired
    AircraftRepository aircraftRepository;

    @Autowired
    AircraftImageRepository aircraftImageRepository;

    @Autowired
    IAircraftImageService aircraftImageService;

    @PostMapping("/{aircraftId}/images")
    public ResponseEntity<AircraftImageInfo> uploadAircraftImage(@RequestHeader HttpHeaders headers,
        @PathVariable Long aircraftId,
        @RequestParam("file") MultipartFile file) {
        logHeaders(headers);

        AircraftImage aircraftImage = aircraftImageService.storeAircraftImage(aircraftId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(AircraftImageInfo.valueFrom(aircraftImage));
    }

    @Operation(summary = "Get the Nth (0-indexed) image for an existing aircraft")
    @GetMapping("/{aircraftId}/images/{imageNumber}")
    public ResponseEntity<Resource> getOfferImageByImageNumber(@RequestHeader HttpHeaders headers,
        @PathVariable Long aircraftId,
        @PathVariable Integer imageNumber
    ) {
        logHeaders(headers);
        Optional<Aircraft> maybeAircraft = aircraftRepository.findById(aircraftId);

        if (maybeAircraft.isEmpty()) {
            throw new ResourceNotFoundException("Couldn't find aircraft. Can't get images.");
        }

        Aircraft aircraft = maybeAircraft.get();

        AircraftImage aircraftImage;
        try {
            aircraftImage = new ArrayList<AircraftImage>(aircraft.getImages()).get(imageNumber);
        } catch(Exception ex) {
            throw new ResourceNotFoundException("Cant fiiind thaattttt >:(");
        }

        return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.parseMediaType(aircraftImage.getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + aircraftImage.getFileName() + "\"")
        .body(new ByteArrayResource(aircraftImage.getData()));
    }

    @GetMapping("/{aircraftId}/images")
    public ResponseEntity<Collection<AircraftImageInfo>> getOfferImagesInfo(@RequestHeader HttpHeaders headers,
        @PathVariable Long aircraftId) {
        logHeaders(headers);

        Optional<Aircraft> maybeAircraft = aircraftRepository.findById(aircraftId);

        if (maybeAircraft.isEmpty()) {
            throw new ResourceNotFoundException("NO PLANE, NO PIX LADDIE! HUEHUEHUEHUEHUEHUEHUEHUEHUEHUEHUEHUE");
        }

        return ResponseEntity.status(HttpStatus.OK)
        .body(maybeAircraft.get().getImages().stream().map(AircraftImageInfo::valueFrom)
            .collect(toList()));
    }
}

