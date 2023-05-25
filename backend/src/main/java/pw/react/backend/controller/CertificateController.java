package pw.react.backend.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pw.react.backend.dao.UserRepository;
import pw.react.backend.models.Certificate;
import pw.react.backend.models.User;
import pw.react.backend.services.ICertificateService;
import pw.react.backend.web.CertificateInfo;

@RestController
@RequestMapping(path = CertificateController.CERTIFICATE_PATH)
public class CertificateController extends BaseController {
    public static final String CERTIFICATE_PATH = "/logic/api/certificates";

    @Autowired
    ICertificateService certificateService;

    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<CertificateInfo> uploadFile(@RequestHeader HttpHeaders headers,
        @RequestParam("id") Long ownerId,
        @RequestParam("file") MultipartFile file,


        @RequestParam("expiryDate")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDateTime expiryDate,

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
}

