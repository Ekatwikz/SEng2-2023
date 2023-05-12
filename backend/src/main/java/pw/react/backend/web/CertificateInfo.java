package pw.react.backend.web;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import pw.react.backend.models.Certificate;
import pw.react.backend.utils.JsonDateDeserializer;
import pw.react.backend.utils.JsonDateSerializer;

public record CertificateInfo(Long certificateId,
    String certificateName,

    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    LocalDateTime expiryDate,

    Long ownerId,
    String fileName,
    String fileSize,
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

