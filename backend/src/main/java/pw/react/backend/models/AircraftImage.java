package pw.react.backend.models;

import java.util.Arrays;
import java.util.Objects;

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
@Table(name = "aircraft_images")
public class AircraftImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftImageId;

    private String fileName;
    private String fileType;
    private Long fileSize;

    @ManyToOne
    @JoinColumn(name="aircraft_id", nullable=false)
    private Aircraft aircraft;

    @Lob
    private byte[] data;

    public AircraftImage() {
        // must be default constructible
        // to send lol
    }

    public AircraftImage(final Aircraft aircraft, final MultipartFile file, final byte[] data) {
        this.fileName = StringUtils.cleanPath(file.getOriginalFilename());
        this.fileType = file.getContentType();
        this.fileSize = file.getSize();
        this.aircraft = aircraft;
        this.data = data;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(aircraftImageId, fileName, fileType, aircraft.getAircraftId());
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AircraftImage that = (AircraftImage) o;
        return aircraftImageId == that.aircraftImageId
        && aircraft.getAircraftId() == that.aircraft.getAircraftId()
        && fileName.equals(that.fileName)
        && fileType.equals(that.fileType)
        && Arrays.equals(data, that.data);
    }

    public Long getAircraftImageId() {
        return aircraftImageId;
    }

    public void setAircraftImageId(final Long id) {
        this.aircraftImageId = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(final String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(final Long fileSize) {
        this.fileSize = fileSize;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(final Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(final byte[] data) {
        this.data = data;
    }
}

