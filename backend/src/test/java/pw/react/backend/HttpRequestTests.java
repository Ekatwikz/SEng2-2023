package pw.react.backend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import pw.react.backend.models.User;
import pw.react.backend.web.LoginResponse;

@ActiveProfiles(profiles = {"mysql-docker-dev", "jwt"})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HttpRequestTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    @BeforeEach
    public void setUp() {
	baseUrl = "http://localhost:" + port + "/logic/api/";
    }

    @Test
    public void usersEndpointIsUpAndRequiresAuth() {
	// basic af smoke test, if this fails nothing should work tbh
	final ResponseEntity<?> response = restTemplate.getForEntity(String.format("%s/users", baseUrl), Object.class);
	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void registrationShouldReturnValidResponse() {
		User newUser = new User();
		newUser.setUsername(String.format("TESTING_%s", System.currentTimeMillis()));
		newUser.setPassword("testing");
		newUser.setEmail("testing@example.com");

        LoginResponse response = restTemplate.postForObject(String.format("%s/users", baseUrl), newUser, LoginResponse.class);
        assertThat(response).isNotNull();
		assertThat(response instanceof LoginResponse);
    }

    // @Test
    // public void loginShouldWork() {
    // 	// TODO
    // }

    // @Test
    // public void bookingCreationShouldWork() {
    // 	// TODO
    // }

    // @Test
    // public void bookingUpdatesShouldWork() {
    // 	// TODO
    // }

    // @Test
    // public void certificateCreationShouldWork() {
    // 	// TODO
    // }

    // @Test
    // public void certificateRetrievalShouldWork() {
    // 	// TODO
    // }

    // @Test
    // public void aircraftCreationShouldWork() {
    // 	// TODO
    // }

    // @Test
    // public void aircraftRetrievalShouldWork() {
    // 	// TODO
    // }

    // @Test
    // public void aircraftRetrievalByTimeRangeShouldWork() {
    // 	// TODO
    // }
}
