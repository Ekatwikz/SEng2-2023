package pw.react.backend.services;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

import pw.react.backend.models.User;

public interface IUserService {
    User validateAndSave(User user);
    User updatePassword(User user, String password);
    void setPasswordEncoder(PasswordEncoder passwordEncoder);
    Optional<User> findById(Long id);
}
