package pw.react.backend.services;

import static pw.react.backend.utils.MySimpleUtils.intervalsOverlap;
import static pw.react.backend.utils.MySimpleUtils.intervalIsValid;

import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import pw.react.backend.controller.BaseLoggable;
import pw.react.backend.dao.AircraftRepository;
import pw.react.backend.dao.BookingRepository;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.exceptions.ValidationException;
import pw.react.backend.models.Aircraft;
import pw.react.backend.models.Booking;
import pw.react.backend.web.BookingDto;

public class BookingService extends BaseLoggable implements IBookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    // TODO: move the validation stuff to a separate method
    @Override
    public BookingDto save(BookingDto bookingDto) {
        Optional<Aircraft> maybeAircraft = aircraftRepository.findById(bookingDto.aircraftId());

        if (maybeAircraft.isEmpty()) {
            throw new ResourceNotFoundException("Aircraft not found. Cannot book.");
        }
        Aircraft aircraft = maybeAircraft.get();

        Set<Booking> bookingsToCheck = aircraft.getBookings();
        Long bookingId = bookingDto.bookingId();
        if (bookingId != null) {
            bookingsToCheck.removeIf(booking -> booking.getBookingId() == bookingId);

            if (bookingRepository.findById(bookingId).isEmpty()) {
                throw new ValidationException("Booking ID given but couldn't find booking to update");
            }
        }

        if (bookingDto.bookingStartDateTime() == null) {
            throw new ValidationException("No booking start date");
        }

        if (bookingDto.bookingEndDateTime() == null) {
            throw new ValidationException("No booking end date");
        }

        if (!intervalIsValid(
            bookingDto.bookingStartDateTime().toEpochSecond(ZoneOffset.UTC),
            bookingDto.bookingEndDateTime().toEpochSecond(ZoneOffset.UTC)
        )) {
            throw new ValidationException("Invalid time interval");
        }

        // can probably be done faster with a smart algorithm but ehh
        for (Booking booking : bookingsToCheck) {
            if (intervalsOverlap(
                bookingDto.bookingStartDateTime().toEpochSecond(ZoneOffset.UTC),
                bookingDto.bookingEndDateTime().toEpochSecond(ZoneOffset.UTC),
                booking.getBookingStartDateTime().toEpochSecond(ZoneOffset.UTC),
                booking.getBookingEndDateTime().toEpochSecond(ZoneOffset.UTC)
            )) {
                throw new ValidationException("Booking overlaps an existing one");
            }
        }

        Booking booking = bookingRepository.save(BookingDto.convertToBooking(bookingDto, aircraft));
        return BookingDto.valueFrom(booking);
    }
}

