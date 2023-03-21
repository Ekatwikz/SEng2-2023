package pw.react.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.models.User;
import pw.react.backend.services.IUserService;
import pw.react.backend.web.UserDto;

import java.util.Optional;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping(path = JwtUserController.USERS_PATH)
@Profile({"jwt"})
public class JwtUserController {
    public static final String USERS_PATH = "/logic/api/users";
    private static final Logger log = LoggerFactory.getLogger(JwtUserController.class);

    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;

    public JwtUserController(IUserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void init() {
        userService.setPasswordEncoder(passwordEncoder);
    }

    @PostMapping(path = "")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User user = UserDto.convertToUser(userDto);
        user = userService.validateAndSave(user);
        log.info("Password is going to be encoded.");
        userService.updatePassword(user, user.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.valueFrom(user));
    }

    // not the most secure...
    @GetMapping(path = "/{userId}")
    public ResponseEntity<UserDto> getUser(@RequestBody UserDto userDto,
        @PathVariable Long userId) {
        Optional<User> maybeUser = userService.findById(userId);

        if (maybeUser.isEmpty()) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        log.info("User info fetched.");
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.valueFrom(maybeUser.get()));
    }
}
