package pw.react.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import pw.react.backend.models.User;

@SpringBootTest
@AutoConfigureMockMvc
public class MockMvcTests {
    @Autowired
    private MockMvc mockMvc;

    private final String baseUrl = "/logic/api";

    @Test
    public void registrationShouldReturnValidResponse() throws Exception {
	// Setup Dto
	final User newUser = new User();
	newUser.setUsername(String.format("TESTING_%s", System.currentTimeMillis()));
	newUser.setPassword("testing");
	newUser.setFirstName("testing_firstname");
	newUser.setLastName("testing_lastname");
	newUser.setEmail("testing@example.com");

	// POST Dto and assert that the response is as expected
	mockMvc.perform(post(baseUrl + "/users")
	    .contentType(MediaType.APPLICATION_JSON)
	    .content(new ObjectMapper().writeValueAsString(newUser)))
	    .andExpect(status().isCreated())
	    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	    .andExpect(jsonPath("$.username").value(newUser.getUsername()))
	    .andExpect(jsonPath("$.email").value(newUser.getEmail()))
	    .andExpect(jsonPath("$.firstName").value(newUser.getFirstName()))
	    .andExpect(jsonPath("$.lastName").value(newUser.getLastName()));
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
