package pw.react.backend.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import pw.react.backend.models.Certificate;
import pw.react.backend.web.CertificateInfo;

public interface ICertificateService {
    Certificate save(final Long userId,
        final MultipartFile file,
        final LocalDate expiryDate,
        final String certificateName
    );

    Optional<List<CertificateInfo>> getByUser(Long id);
    List<CertificateInfo> getAll();
}

