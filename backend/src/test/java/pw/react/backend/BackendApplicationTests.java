package pw.react.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

import pw.react.backend.models.User;
import pw.react.backend.web.LoginResponse;

@SpringBootTest
@ActiveProfiles(profiles = {"mysql-dev"})
class BackendApplicationTests {
	@Autowired
    private RestTemplate restTemplate;

	private String url;
	private int port;
	public BackendApplicationTests() {
		port = 8080;
		url = "http://localhost:" + port + "/logic/api";
	}

    @Test
    public void registrationShouldReturnValidResponse() {
		User newUser = new User();
		newUser.setUsername(String.format("TESTING_%s", System.currentTimeMillis()));
		newUser.setPassword("testing");
		newUser.setEmail("testing@example.com");

        LoginResponse response = restTemplate.postForObject(String.format("%s/users", url), newUser, LoginResponse.class);
        assertThat(response).isNotNull();
		assertThat(response instanceof LoginResponse);
    }

	@Test
    public void loginShouldWork() {
		// TODO
	}

	@Test
    public void bookingCreationShouldWork() {
		// TODO
	}

	@Test
    public void bookingUpdatesShouldWork() {
		// TODO
	}

	@Test
    public void certificateCreationShouldWork() {
		// TODO
	}

	@Test
    public void certificateRetrievalShouldWork() {
		// TODO
	}

	@Test
    public void aircraftCreationShouldWork() {
		// TODO
	}

	@Test
    public void aircraftRetrievalShouldWork() {
		// TODO
	}

	@Test
    public void aircraftRetrievalByTimeRangeShouldWork() {
		// TODO
	}
}
