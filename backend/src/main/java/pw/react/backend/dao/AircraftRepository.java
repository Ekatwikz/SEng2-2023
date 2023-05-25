package pw.react.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import pw.react.backend.models.Aircraft;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> { }

