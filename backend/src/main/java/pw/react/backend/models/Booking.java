package pw.react.backend.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bookings")
public class Booking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    private LocalDateTime bookingStartDateTime;
    private LocalDateTime bookingEndDateTime;

    @ManyToOne
    @JoinColumn(name="aircraft_id", nullable=false)
    private Aircraft aircraft;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDateTime getBookingStartDateTime() {
        return bookingStartDateTime;
    }

    public void setBookingStartDateTime(LocalDateTime bookingStartDateTime) {
        this.bookingStartDateTime = bookingStartDateTime;
    }

    public LocalDateTime getBookingEndDateTime() {
        return bookingEndDateTime;
    }

    public void setBookingEndDateTime(LocalDateTime bookingEndDateTime) {
        this.bookingEndDateTime = bookingEndDateTime;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }
}

