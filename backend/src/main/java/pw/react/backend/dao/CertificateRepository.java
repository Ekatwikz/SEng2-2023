package pw.react.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import pw.react.backend.models.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Long> { }

