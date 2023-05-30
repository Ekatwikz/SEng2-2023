package pw.react.backend.web;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import pw.react.backend.models.Aircraft;
import pw.react.backend.models.Booking;
import pw.react.backend.utils.JsonDateDeserializer;
import pw.react.backend.utils.JsonDateSerializer;

public record BookingDto(Long bookingId,
    Long aircraftId,

    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    LocalDateTime bookingStartDateTime,
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    LocalDateTime bookingEndDateTime) {

    public static final BookingDto EMPTY = new BookingDto(0L, 0L, null, null);

    public static BookingDto valueFrom(Booking booking) {
        return new BookingDto(booking.getBookingId(),
            booking.getAircraft().getAircraftId(),
            booking.getBookingStartDateTime(),
            booking.getBookingEndDateTime()
        );
    }

    public static Booking convertToBooking(BookingDto bookingDto) {
        Booking booking = new Booking();

        booking.setBookingId(bookingDto.bookingId());
        booking.setBookingStartDateTime(bookingDto.bookingStartDateTime());
        booking.setBookingEndDateTime(bookingDto.bookingEndDateTime());

        return booking;
    }

    public static Booking convertToBooking(BookingDto bookingDto, Aircraft aircraft) {
        Booking booking = BookingDto.convertToBooking(bookingDto);

        booking.setAircraft(aircraft);

        return booking;
    }
}

