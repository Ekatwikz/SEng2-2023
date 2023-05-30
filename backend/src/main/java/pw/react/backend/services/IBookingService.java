package pw.react.backend.services;

import pw.react.backend.web.BookingDto;

public interface IBookingService {
    BookingDto save(BookingDto bookingDto);
}

