package pw.react.backend.web;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import pw.react.backend.controller.CertificateController;
import pw.react.backend.models.Certificate;

public record CertificateInfo(Long certificateId,
    String certificateName,
    LocalDateTime expiryDate,
    Long ownerId,
    String fileName,
    Long fileSize,
    String fileType,
    String certificateFile
) {
    public static CertificateInfo valueFrom(Certificate certificate, HttpServletRequest httpServletRequest) {
        return new CertificateInfo (
            certificate.getCertificateId(),
            certificate.getCertificateName(),
            certificate.getExpiryDate(),
            certificate.getOwner().getId(),
            certificate.getFileName(),
            certificate.getFileSize(),
            certificate.getFileType(),

            String.format("%s://%s:%d%s/%d/file",
                httpServletRequest.getScheme(),
                httpServletRequest.getServerName(),
                httpServletRequest.getServerPort(),
                CertificateController.CERTIFICATE_PATH,
                certificate.getCertificateId()
            )
        );
    }
}

