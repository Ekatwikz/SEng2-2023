package pw.react.backend.models;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "aircrafts")
public class Aircraft implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftId;

    private String aicraftName;
    private String aircraftType;
    private String aircraftRegistration;
    private String aircraftDescription;

    @OneToMany(mappedBy = "aircraft")
    private Set<Booking> bookings;

    @OneToMany(mappedBy = "aircraft")
    private Set<AircraftImage> images;

    public Long getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(final Long aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getAicraftName() {
        return aicraftName;
    }

    public void setAicraftName(final String aicraftName) {
        this.aicraftName = aicraftName;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(final String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getAircraftRegistration() {
        return aircraftRegistration;
    }

    public void setAircraftRegistration(final String aircraftRegistration) {
        this.aircraftRegistration = aircraftRegistration;
    }

    public String getAircraftDescription() {
        return aircraftDescription;
    }

    public void setAircraftDescription(final String aircraftDescription) {
        this.aircraftDescription = aircraftDescription;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(final Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Set<AircraftImage> getImages() {
        return images;
    }

    public void setImages(final Set<AircraftImage> images) {
        this.images = images;
    }
}

