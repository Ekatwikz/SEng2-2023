package pw.react.backend.web;

import javax.validation.constraints.Email;

import pw.react.backend.models.User;

public record UserDto(Long id,
    String username,
    String password,

    @Email
    String email,
    String firstName,
    String lastName) {
    public static UserDto valueFrom(User user) {
        return new UserDto(user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName()
        );
    }

    public static User convertToUser(UserDto userDto) {
        User user = new User();

        user.setId(userDto.id());
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());

        return user;
    }
}
