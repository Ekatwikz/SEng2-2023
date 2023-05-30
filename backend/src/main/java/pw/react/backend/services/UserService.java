package pw.react.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import pw.react.backend.dao.UserRepository;
import pw.react.backend.exceptions.ValidationException;
import pw.react.backend.models.User;

import java.util.Optional;

public class UserService implements IUserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User validateAndSave(User user) {
        if (isValidUser(user)) {
            log.info("User is valid");
            Optional<User> dbUser = userRepository.findByUsername(user.getUsername());
            if (dbUser.isPresent()) {
                log.info("User already exists. Updating it.");
                user.setId(dbUser.get().getId());
            }
            user = userRepository.save(user);
            log.info("User was saved.");
        }
        return user;
    }

    private boolean isValidUser(User user) {
        if (user != null) {
            if (!isValid(user.getUsername())) {
                log.error("Empty username.");
                throw new ValidationException("Empty username.");
            }
            if (!isValid(user.getPassword())) {
                log.error("Empty user password.");
                throw new ValidationException("Empty user password.");
            }
            if (!isValid(user.getEmail())) {
                log.error("UEmpty email.");
                throw new ValidationException("Empty email.");
            }

            if (!isValid(user.getFirstName())) {
                log.error("UEmpty firstName.");
                throw new ValidationException("Empty firstName.");
            }
            if (!isValid(user.getLastName())) {
                log.error("UEmpty lastName.");
                throw new ValidationException("Empty lastName.");
            }

            return true;
        }
        log.error("User is null.");
        throw new ValidationException("User is null.");
    }

    private boolean isValid(String value) {
        return value != null && !value.isBlank();
    }

    @Override
    public User updatePassword(User user, String password) {
        if (isValidUser(user)) {
            if (passwordEncoder != null) {
                log.debug("Encoding password.");
                user.setPassword(passwordEncoder.encode(password));
            } else {
                log.debug("Password in plain text.");
                user.setPassword(password);
            }
            user = userRepository.save(user);
        }
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
