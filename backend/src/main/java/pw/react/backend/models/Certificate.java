package pw.react.backend.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "certificates")
public class Certificate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificateId;

    @ManyToOne
    @JoinColumn(name="owner_id", nullable=false)
    private User owner;

    @Lob
    private byte[] certificateFile;

    private String certificateName;
    private LocalDateTime expiryDate;
    private String fileName;
    private Long fileSize;
    private String fileType;

    public Certificate() { }

    public Certificate(User owner, LocalDateTime expiryDate, MultipartFile certificateFile, String certificateName, byte[] bytes) {
        this.owner = owner;
        this.certificateFile = bytes;
        this.certificateName = certificateName;
        this.expiryDate = expiryDate;

        this.fileName = StringUtils.cleanPath(certificateFile.getOriginalFilename());
        this.fileType = certificateFile.getContentType();
        this.fileSize = certificateFile.getSize();
    }

    public Long getCertificateId() {
        return certificateId;
    }
    public void setCertificateId(final Long certificateId) {
        this.certificateId = certificateId;
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(final User owner) {
        this.owner = owner;
    }
    public byte[] getCertificateFile() {
        return certificateFile;
    }
    public void setCertificateFile(final byte[] certificateFile) {
        this.certificateFile = certificateFile;
    }
    public String getCertificateName() {
        return certificateName;
    }
    public void setCertificateName(final String certificateName) {
        this.certificateName = certificateName;
    }
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(final LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }
    public Long getFileSize() {
        return fileSize;
    }
    public void setFileSize(final Long fileSize) {
        this.fileSize = fileSize;
    }
    public String getFileType() {
        return fileType;
    }
    public void setFileType(final String fileType) {
        this.fileType = fileType;
    }
}

