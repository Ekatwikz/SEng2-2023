package pw.react.backend.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import pw.react.backend.dao.CertificateRepository;
import pw.react.backend.dao.UserRepository;
import pw.react.backend.exceptions.InvalidFileException;
import pw.react.backend.models.Certificate;
import pw.react.backend.models.User;
import pw.react.backend.web.CertificateInfo;

public class CertificateService implements ICertificateService {
    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Certificate save(Long userId,
        MultipartFile file,
        LocalDate expiryDate,
        String certificateName) {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new RuntimeException("Couldn't find User ");
        }

        if (file.isEmpty()) {
            throw new RuntimeException("File is Empty?!");
        }

        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new InvalidFileException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Certificate certificate = new Certificate(maybeUser.get(), expiryDate, file, certificateName, file.getBytes());
            //repository.findByCompanyId(companyId).ifPresent(companyLogo -> newCompanyLogo.setId(companyLogo.getId())); // ???
            return certificateRepository.save(certificate);
        } catch (IOException ex) {
            throw new InvalidFileException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Optional<List<CertificateInfo>> getByUser(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByUser'");
    }

    @Override
    public List<CertificateInfo> getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }
}
