package pw.react.backend.web;

import javax.validation.constraints.Email;

import pw.react.backend.models.User;

public record LoginResponse(Long id,
    String username,
    @Email
    String email,
    String firstName,
    String lastName,
    String jwttoken) {
    public static LoginResponse valueFrom(User user, String tokenValue) {
        return new LoginResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            tokenValue
        );
    }
}
