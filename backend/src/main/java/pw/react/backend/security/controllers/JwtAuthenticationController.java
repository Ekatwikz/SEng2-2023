package pw.react.backend.security.controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import pw.react.backend.models.User;
import pw.react.backend.security.models.JwtRequest;
import pw.react.backend.security.services.JwtTokenService;
import pw.react.backend.security.services.JwtUserDetailsService;
import pw.react.backend.web.LoginResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping
@Profile({"jwt"})
public class JwtAuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final JwtUserDetailsService userDetailsService;

    public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, JwtUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> createAuthenticationToken(@Valid @RequestBody JwtRequest authenticationRequest,
                                                       HttpServletRequest request) throws Exception {
        authenticate(authenticationRequest.username(), authenticationRequest.password());
        final User userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username());
        final String tokenValue = jwtTokenService.generateToken(userDetails, request);

        return ResponseEntity.ok(LoginResponse.valueFrom(userDetails, tokenValue));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> invalidateToken(HttpServletRequest request) {
        boolean result = jwtTokenService.invalidateToken(request);
        return result ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // @DeleteMapping("/logic/api/auth")
    // public ResponseEntity<Void> removeInvalidTokens() {
    //     jwtTokenService.removeTokens();
    //     return ResponseEntity.accepted().build();
    // }
}
