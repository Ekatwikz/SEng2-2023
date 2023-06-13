package pw.react.backend.web;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public record ExceptionDetailsDto(LocalDateTime timestamp,
    HttpStatus status,
    String errorMessage,
    String path
) {
}

