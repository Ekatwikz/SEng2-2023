package pw.react.backend.web;

import java.time.LocalDateTime;

import pw.react.backend.models.Certificate;

public record CertificateInfo(Long certificateId,
    String certificateName,
    LocalDateTime expiryDate,
    Long ownerId,
    String fileName,
    Long fileSize,
    String fileType) {
    public static CertificateInfo valueFrom(Certificate certificate) {
        return new CertificateInfo (certificate.getCertificateId(),
            certificate.getCertificateName(),
            certificate.getExpiryDate(),
            certificate.getOwner().getId(),
            certificate.getFileName(),
            certificate.getFileSize(),
            certificate.getFileType());
    }
}

