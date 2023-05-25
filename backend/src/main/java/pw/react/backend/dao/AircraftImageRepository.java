package pw.react.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import pw.react.backend.models.AircraftImage;

public interface AircraftImageRepository extends JpaRepository<AircraftImage, Long> { }

