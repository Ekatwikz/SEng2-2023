package pw.react.backend.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
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

import pw.react.backend.dao.CertificateRepository;
import pw.react.backend.dao.UserRepository;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.models.Certificate;
import pw.react.backend.models.User;
import pw.react.backend.services.ICertificateService;
import pw.react.backend.web.CertificateInfo;

@RestController
@RequestMapping(path = CertificateController.CERTIFICATE_PATH)
public class CertificateController extends BaseLoggable {
    public static final String CERTIFICATE_PATH = "/user/certificates";

    @Autowired
    ICertificateService certificateService;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<CertificateInfo> uploadFile(@RequestHeader HttpHeaders headers,
        @RequestParam("id") Long ownerId,
        @RequestParam("file") MultipartFile file,

        @RequestParam("expiryDate")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate expiryDate,

        @RequestParam("certificateName") String certificateName) {
        logHeaders(headers);

        Optional<User> maybeUser = userRepository.findById(ownerId);

        if (!maybeUser.isPresent()) {
            throw new RuntimeException("User not found, can't add certificate");
        }

        Certificate certificate = certificateService.save(ownerId, file, expiryDate, certificateName);
        CertificateInfo certificateInfo = CertificateInfo.valueFrom(certificate);
        return ResponseEntity.status(HttpStatus.CREATED).body(certificateInfo);
    }

    @GetMapping("/{certificateId}")
    public ResponseEntity<CertificateInfo> getCertById(
        @RequestHeader HttpHeaders headers,
        @PathVariable Long certificateId) {
        logHeaders(headers);

        Optional<Certificate> maybeCert = certificateRepository.findById(certificateId);
        if (maybeCert.isEmpty()) {
            throw new ResourceNotFoundException("Couldn't find that certificate");
        }

        return ResponseEntity.status(HttpStatus.OK).body(CertificateInfo.valueFrom(maybeCert.get()));
    }

    @GetMapping("/{certificateId}/file")
    public ResponseEntity<Resource> getCertFile(
        @RequestHeader HttpHeaders headers,
        @PathVariable Long certificateId) {
        logHeaders(headers);

        Optional<Certificate> maybeCert = certificateRepository.findById(certificateId);
        if (maybeCert.isEmpty()) {
            throw new RuntimeException("Couldn't find that certificate");
        }

        return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(maybeCert.get().getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + maybeCert.get().getFileName() + "\"")
        .body(new ByteArrayResource(maybeCert.get().getCertificateFile()));
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<CertificateInfo>> getAllCerts(@RequestHeader HttpHeaders headers) {
        logHeaders(headers);

        List<Certificate> certs = certificateRepository.findAll();
        List<CertificateInfo> certInfos = certs.stream()
        .map(CertificateInfo::valueFrom)
        .toList();

        return ResponseEntity.status(HttpStatus.OK).body(certInfos);
    }
}

