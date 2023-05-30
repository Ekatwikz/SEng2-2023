package pw.react.backend.controller;

import static java.util.stream.Collectors.toList;
import static pw.react.backend.utils.MySimpleUtils.valueIsInInterval;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pw.react.backend.dao.BookingRepository;
import pw.react.backend.exceptions.ValidationException;
import pw.react.backend.models.Booking;
import pw.react.backend.services.IBookingService;
import pw.react.backend.web.BookingDto;

@RestController
@RequestMapping(path = BookingController.BOOKINGS_PATH)
public class BookingController extends BaseLoggable {
    public static final String BOOKINGS_PATH = "/logic/api/bookings";

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    IBookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(
        @RequestHeader final HttpHeaders headers,
        @RequestBody final BookingDto bookingDto
    ) {
        logHeaders(headers);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.save(bookingDto));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings(
        @RequestHeader final HttpHeaders headers,

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @RequestParam(required = false) final LocalDateTime bookingsStartDateTime,

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @RequestParam(required = false) final LocalDateTime bookingsEndDateTime
    ) {
        logHeaders(headers);

        final List<Booking> bookings = bookingRepository.findAll();
        final List<BookingDto> bookingDtos = bookings.stream()
        .map(BookingDto::valueFrom)
        .toList();

        Stream<BookingDto> bookingDtoStream = bookingDtos.stream();

        Long bookingsStartDateTimeAsEpochSecond = null;
        if (bookingsStartDateTime != null) {
            bookingsStartDateTimeAsEpochSecond = bookingsStartDateTime.toEpochSecond(ZoneOffset.UTC);
        }
        final Long tmpHack1 = bookingsStartDateTimeAsEpochSecond; // TODO: make these proper lol

        Long bookingsEndDateTimeAsEpochSecond = null;
        if (bookingsEndDateTime != null) {
            bookingsEndDateTimeAsEpochSecond = bookingsEndDateTime.toEpochSecond(ZoneOffset.UTC);
        }
        final Long tmpHack2 = bookingsEndDateTimeAsEpochSecond;

        if (bookingsStartDateTimeAsEpochSecond != null
        && bookingsEndDateTimeAsEpochSecond != null
        && bookingsStartDateTimeAsEpochSecond > bookingsEndDateTimeAsEpochSecond) {
            throw new ValidationException("Date range is invalid");
        }

        if (bookingsStartDateTime != null) {
            bookingDtoStream = bookingDtoStream.filter((final BookingDto bookingDto) -> {
                return valueIsInInterval(bookingDto.bookingStartDateTime().toEpochSecond(ZoneOffset.UTC),
                    tmpHack1,
                    Long.MAX_VALUE
                );
            });
        }

        if (bookingsEndDateTime != null) {
            bookingDtoStream = bookingDtoStream.filter((final BookingDto bookingDto) -> {
                return valueIsInInterval(bookingDto.bookingEndDateTime().toEpochSecond(ZoneOffset.UTC),
                    Long.MIN_VALUE,
                    tmpHack2
                );
            });
        }

        return ResponseEntity.status(HttpStatus.OK).body(bookingDtoStream.collect(toList()));
    }
}

