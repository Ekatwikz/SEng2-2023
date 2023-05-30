package pw.react.backend.controller;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pw.react.backend.dao.AircraftRepository;
import pw.react.backend.models.Aircraft;
import pw.react.backend.services.AircraftService;
import pw.react.backend.web.AircraftDto;

@RestController
@RequestMapping(path = AircraftController.AIRCRAFT_PATH)
public class AircraftController extends BaseLoggable {
    public static final String AIRCRAFT_PATH = "/logic/api/aircrafts";

    @Autowired
    AircraftRepository aircraftRepository;

    @PostMapping
    public ResponseEntity<AircraftDto> createAircraft(
        @RequestHeader HttpHeaders headers,
        @RequestBody AircraftDto aircraftDto
    ) {
        logHeaders(headers);

        Aircraft aircraft = AircraftDto.convertToAircraft(aircraftDto);
        AircraftService.validateAircraft(aircraft);
        Aircraft savedAircraft = aircraftRepository.save(aircraft);

        return ResponseEntity.status(HttpStatus.CREATED).body(AircraftDto.valueFrom(savedAircraft));
    }

    @GetMapping
    public ResponseEntity<List<AircraftDto>> getAllAircrafts(
        @RequestHeader HttpHeaders headers,
        @RequestBody AircraftDto aircraftDto
    ) {
        logHeaders(headers);

        List<Aircraft> aircrafts = aircraftRepository.findAll();
        List<AircraftDto> aircraftDtos = aircrafts.stream()
        .map(AircraftDto::valueFrom)
        .toList();

        return ResponseEntity.status(HttpStatus.OK).body(aircraftDtos);
    }

    @GetMapping
    public ResponseEntity<List<AircraftDto>> getAllAircraftsByPeriod(
        @RequestHeader HttpHeaders headers,
        @RequestBody AircraftDto aircraftDto,
        @RequestParam(required = false) LocalDate dateFrom,
        @RequestParam(required = false) LocalDate dateTo
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}

