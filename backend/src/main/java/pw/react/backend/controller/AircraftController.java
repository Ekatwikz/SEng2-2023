package pw.react.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pw.react.backend.dao.AircraftRepository;
import pw.react.backend.models.Aircraft;
import pw.react.backend.services.IAircraftService;
import pw.react.backend.web.AircraftDto;

@RestController
@RequestMapping(path = AircraftController.AIRCRAFT_PATH)
public class AircraftController extends BaseLoggable {
    public static final String AIRCRAFT_PATH = "/aircrafts";

    @Autowired
    AircraftRepository aircraftRepository;

    @Autowired
    IAircraftService aircraftService;

    @PostMapping
    public ResponseEntity<AircraftDto> createAircraft(
        @RequestHeader final HttpHeaders headers,
        @RequestBody final AircraftDto aircraftDto
    ) {
        logHeaders(headers);

        final Aircraft aircraft = AircraftDto.convertToAircraft(aircraftDto);
        aircraftService.validateAircraft(aircraft);
        final Aircraft savedAircraft = aircraftRepository.save(aircraft);

        return ResponseEntity.status(HttpStatus.CREATED).body(AircraftDto.valueFrom(savedAircraft));
    }

    @GetMapping
    public ResponseEntity<List<AircraftDto>> getAllAircrafts(@RequestHeader final HttpHeaders headers) {
        logHeaders(headers);

        final List<Aircraft> aircrafts = aircraftRepository.findAll();
        final List<AircraftDto> aircraftDtos = aircrafts.stream()
        .map(AircraftDto::valueFrom)
        .toList();

        return ResponseEntity.status(HttpStatus.OK).body(aircraftDtos);
    }
}

