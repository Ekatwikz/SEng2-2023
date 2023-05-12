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
    private String fileSize;
    private String fileType;

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
    public String getFileSize() {
        return fileSize;
    }
    public void setFileSize(final String fileSize) {
        this.fileSize = fileSize;
    }
    public String getFileType() {
        return fileType;
    }
    public void setFileType(final String fileType) {
        this.fileType = fileType;
    }
}

