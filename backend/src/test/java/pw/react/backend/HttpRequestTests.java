package pw.react.backend;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import pw.react.backend.models.User;
import pw.react.backend.web.UserDto;
import pw.react.backend.web.AircraftDto;
import pw.react.backend.web.BookingDto;
import pw.react.backend.web.CertificateInfo;
import pw.react.backend.web.ExceptionDetailsDto;
import pw.react.backend.web.LoginResponse;

@ActiveProfiles(profiles = {"mysql-docker-dev", "jwt"})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HttpRequestTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

    private String baseUrl;
    private DateTimeFormatter dateTimeFormatter;
    private Random randGenerator;
    @BeforeEach // BeforeAll ??
    public void setUp() {
	baseUrl = "http://localhost:" + port + "";
	dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	randGenerator = new Random(System.currentTimeMillis());
    }

    @Test
    public void registrationEndpointIsUpAndRequiresAuth() {
	// basic af smoke test, if this fails nothing should work tbh
	final ResponseEntity<?> response = restTemplate.getForEntity(String.format("%s/registration", baseUrl), Object.class);
	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private User createNewUser() {
	User newUser = new User();
	newUser.setUsername(String.format("TESTING_%d", randGenerator.nextLong()));
	newUser.setPassword("testing");
	newUser.setEmail("testing@example.com");
	newUser.setFirstName("TestFirstName");
	newUser.setLastName("TestLastName");

	return newUser;
    }

    private UserDto registerNewUser(User newUser) {
	ResponseEntity<UserDto> registrationResponseEntity = restTemplate.exchange(
	    String.format("%s/registration", baseUrl),
	    HttpMethod.POST,
	    new HttpEntity<>(newUser),
	    UserDto.class
	);

	assertThat(registrationResponseEntity).isNotNull();
	assertThat(registrationResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	return registrationResponseEntity.getBody();
    }

    @Test
    public void registrationShouldReturnValidResponse() {
	User newUser = createNewUser();
	UserDto registrationResponse = registerNewUser(newUser);

	assertThat(registrationResponse).isNotNull();
	assertThat(registrationResponse.id()).isPositive();
	assertThat(registrationResponse.password()).isNotEmpty();
	assertThat(registrationResponse.email()).isEqualTo(newUser.getEmail());
	assertThat(registrationResponse.username()).isEqualTo(newUser.getUsername());
	assertThat(registrationResponse.firstName()).isEqualTo(newUser.getFirstName());
	assertThat(registrationResponse.lastName()).isEqualTo(newUser.getLastName());
    }

    private LoginResponse loginNewUser(User newUser) {
	ResponseEntity<LoginResponse> loginResponseEntity = restTemplate.exchange(
	    String.format("%s/login", baseUrl),
	    HttpMethod.POST,
	    new HttpEntity<>(newUser),
	    LoginResponse.class
	);

	assertThat(loginResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	return loginResponseEntity.getBody();
    }

    @Test
    public void loginShouldWork() {
	User newUser = createNewUser();
	registerNewUser(newUser);
	LoginResponse loginResponse = loginNewUser(newUser);

	assertThat(loginResponse).isNotNull();
	assertThat(loginResponse.jwttoken()).isNotBlank();
	assertThat(loginResponse.id()).isNotNull();
	assertThat(loginResponse.id()).isPositive();
	assertThat(loginResponse.username()).isEqualTo(newUser.getUsername());
	assertThat(loginResponse.email()).isEqualTo(newUser.getEmail());
	assertThat(loginResponse.firstName()).isEqualTo(newUser.getFirstName());
	assertThat(loginResponse.lastName()).isEqualTo(newUser.getLastName());
    }

    private HttpHeaders setupHeadersWithJwt(LoginResponse loginResponse) {
	// Set headers
	HttpHeaders headers = new HttpHeaders();
	headers.set("Authorization", "Bearer " + loginResponse.jwttoken());

	return headers;
    }

    private CertificateInfo postNewCertificate(String certName, String expiryDateString, LoginResponse loginResponse, Resource fileResource) {
	HttpHeaders headers = setupHeadersWithJwt(loginResponse);
	headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	// Create the request body with the file, +etc
	MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	body.add("file", fileResource); // TODO: test this properly
	body.add("id", loginResponse.id());
	body.add("certificateName", certName);
	body.add("expiryDate", expiryDateString);

	ResponseEntity<CertificateInfo> certResponseEntity = restTemplate.exchange(
	    String.format("%s/user/certificates", baseUrl),
	    HttpMethod.POST,
	    new HttpEntity<>(body, headers),
	    CertificateInfo.class
	);
	assertThat(certResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

	return certResponseEntity.getBody();
    }

    @Test
    public void certificateCreationShouldWork() throws IOException {
	User newUser = createNewUser();
	registerNewUser(newUser);
	LoginResponse loginResponse = loginNewUser(newUser);

	String certName = String.format("Test-ificate_%s", randGenerator.nextLong());
	Resource fileResource = resourceLoader.getResource("classpath:tinyExampleFile.txt");
	File certFile = fileResource.getFile();

	String expiryDateString = "2076-12-31T23:59:59";
	CertificateInfo certificateInfo = postNewCertificate(certName, expiryDateString, loginResponse, fileResource);

	assertThat(certificateInfo).isNotNull();
	assertThat(certificateInfo.ownerId()).isEqualTo(loginResponse.id());
	assertThat(certificateInfo.certificateName()).isEqualTo(certName);
	assertThat(certificateInfo.expiryDate()).isEqualTo(expiryDateString);
	assertThat(certificateInfo.fileName()).isEqualTo(certFile.getName());
	assertThat(certificateInfo.fileSize()).isEqualTo(certFile.length());
	assertThat(certificateInfo.fileType()).isEqualTo("text/plain");
    }

    @Test
    public void certificateRetrievalShouldWork() throws IOException {
	User newUser = createNewUser();
	registerNewUser(newUser);
	LoginResponse loginResponse = loginNewUser(newUser);
	HttpHeaders headers = setupHeadersWithJwt(loginResponse);

	String certName = String.format("Test-ificate_%s", randGenerator.nextLong());
	Resource fileResource = resourceLoader.getResource("classpath:tinyExampleFile.txt");

	postNewCertificate(certName, "2076-12-30T23:59:59", loginResponse, fileResource);

	ResponseEntity<CertificateInfo[]> certsResponseEntity = restTemplate.exchange(
	    String.format("%s/user/certificates", baseUrl),
	    HttpMethod.GET,
	    new HttpEntity<>(headers),
	    CertificateInfo[].class
	);
	assertThat(certsResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

	CertificateInfo[] certificateInfos = certsResponseEntity.getBody();
	assertThat(certificateInfos).isNotNull();

	int certInfoCount = certificateInfos.length;
	assertThat(certInfoCount).isPositive();

	ResponseEntity<CertificateInfo> certByIdResponseEntity = restTemplate.exchange(
	    String.format("%s/user/certificates/%d", baseUrl, certInfoCount),
	    HttpMethod.GET,
	    new HttpEntity<>(headers),
	    CertificateInfo.class
	);
	assertThat(certByIdResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
	CertificateInfo certificateInfo = certByIdResponseEntity.getBody();
	assertThat(certificateInfo).isNotNull();
	assertThat(certificateInfo.ownerId()).isEqualTo(loginResponse.id());
	assertThat(certificateInfo.certificateName()).isEqualTo(certName);
    }

    private AircraftDto createNewAircraftDto() {
	return new AircraftDto(null,
	    String.format("TEST_AIRCRAFT_%s", randGenerator.nextLong()),
	    "Boeing Test IDK",
	    "ABCDEXYZ123",
	    "Something aicraft description blah blah",
	    null
	);
    }

    private AircraftDto postAircraftDto(AircraftDto aircraftDto, LoginResponse loginResponse) {
	ResponseEntity<AircraftDto> aircraftResponseEntity = restTemplate.exchange(
	    String.format("%s/aircrafts", baseUrl),
	    HttpMethod.POST,
	    new HttpEntity<>(aircraftDto, setupHeadersWithJwt(loginResponse)),
	    AircraftDto.class
	);

	assertThat(aircraftResponseEntity).isNotNull();
	assertThat(aircraftResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

	return aircraftResponseEntity.getBody();
    }

    @Test
    public void aircraftCreationShouldWork() {
	User newUser = createNewUser();
	registerNewUser(newUser);
	AircraftDto requestNewAircraftDto = createNewAircraftDto();
	AircraftDto responseNewAircraftDto = postAircraftDto(requestNewAircraftDto, loginNewUser(newUser));

	assertThat(responseNewAircraftDto).isNotNull();
	assertThat(responseNewAircraftDto.aircraftId()).isPositive();
	assertThat(responseNewAircraftDto.aircraftName()).isEqualTo(requestNewAircraftDto.aircraftName());
	assertThat(responseNewAircraftDto.aircraftType()).isEqualTo(requestNewAircraftDto.aircraftType());
	assertThat(responseNewAircraftDto.aircraftRegistration()).isEqualTo(requestNewAircraftDto.aircraftRegistration());
	assertThat(responseNewAircraftDto.aircraftDescription()).isEqualTo(requestNewAircraftDto.aircraftDescription());

	assertThat(responseNewAircraftDto.aircraftImage()).matches("https?://.*"); // TODO: maybe something more thorough here?
    }

    @Test
    public void aircraftRetrievalShouldWork() {
	User newUser = createNewUser();
	registerNewUser(newUser);
	LoginResponse loginResponse = loginNewUser(newUser);
	AircraftDto newAircraftDto = createNewAircraftDto();
	postAircraftDto(newAircraftDto, loginResponse);

	ResponseEntity<AircraftDto[]> aircraftsResponseEntity = restTemplate.exchange(
	    String.format("%s/aircrafts", baseUrl),
	    HttpMethod.GET,
	    new HttpEntity<>(setupHeadersWithJwt(loginResponse)),
	    AircraftDto[].class
	);
	assertThat(aircraftsResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

	AircraftDto[] aircraftDtos = aircraftsResponseEntity.getBody();
	assertThat(aircraftDtos).isNotNull();

	int aircraftCount = aircraftDtos.length;
	assertThat(aircraftCount).isPositive();
	AircraftDto newestDtoInResponse = aircraftDtos[aircraftCount - 1]; // TODO: change to get by ID if/when that endpoint gets added

	assertThat(newestDtoInResponse.aircraftName()).isEqualTo(newAircraftDto.aircraftName());
	assertThat(newestDtoInResponse.aircraftType()).isEqualTo(newAircraftDto.aircraftType());
	assertThat(newestDtoInResponse.aircraftRegistration()).isEqualTo(newAircraftDto.aircraftRegistration());
	assertThat(newestDtoInResponse.aircraftDescription()).isEqualTo(newAircraftDto.aircraftDescription());
    }

    private BookingDto createNewBookingDto(Long bookingId, Long aircraftId, LocalDateTime bookingStartDateTime, LocalDateTime bookingEndDateTime) {
	return new BookingDto(bookingId,
	    aircraftId,
	    bookingStartDateTime,
	    bookingEndDateTime
	);
    }

    private BookingDto createNewBookingDto(Long aircraftId, LocalDateTime bookingStartDateTime, LocalDateTime bookingEndDateTime) {
	return createNewBookingDto(null, aircraftId, bookingStartDateTime, bookingEndDateTime);
    }

    private <T> T postBookingDto(BookingDto bookingDto, LoginResponse loginResponse,
	HttpStatus expectedResponseStatus, Class<T> responseType) {
	ResponseEntity<T> bookingsResponseEntity = restTemplate.exchange(
	    String.format("%s/bookings", baseUrl),
	    HttpMethod.POST,
	    new HttpEntity<>(bookingDto, setupHeadersWithJwt(loginResponse)),
	    responseType
	);

	assertThat(bookingsResponseEntity.getStatusCode()).isEqualTo(expectedResponseStatus);
	return bookingsResponseEntity.getBody();
    }

    private BookingDto postBookingDto(BookingDto bookingDto, LoginResponse loginResponse) {
	return postBookingDto(bookingDto, loginResponse, HttpStatus.CREATED, BookingDto.class);
    }

    @Test
    public void bookingCreationShouldWork() {
	User newUser = createNewUser();
	registerNewUser(newUser);
	LoginResponse loginResponse = loginNewUser(newUser);
	AircraftDto aircraftDto = postAircraftDto(createNewAircraftDto(), loginResponse);

	String startDateTimeString = "2076-12-31T00:00:01";
	String endDateTimeString = "2076-12-31T23:59:59";
	BookingDto requestNewBookingDto = createNewBookingDto(aircraftDto.aircraftId(),
	    LocalDateTime.parse(startDateTimeString, dateTimeFormatter),
	    LocalDateTime.parse(endDateTimeString, dateTimeFormatter));

	BookingDto responseNewBookingDto = postBookingDto(requestNewBookingDto, loginResponse);
	assertThat(responseNewBookingDto).isNotNull();
	assertThat(responseNewBookingDto.bookingId()).isPositive();
	assertThat(responseNewBookingDto.aircraftId()).isEqualTo(requestNewBookingDto.aircraftId());
	assertThat(responseNewBookingDto.bookingStartDateTime()).isEqualTo(startDateTimeString);
	assertThat(responseNewBookingDto.bookingEndDateTime()).isEqualTo(endDateTimeString);
    }

    @Test
    public void overlappingBookingCreationShouldBeDenied() {
	User newUser = createNewUser();
	registerNewUser(newUser);
	LoginResponse loginResponse = loginNewUser(newUser);
	AircraftDto aircraftDto = postAircraftDto(createNewAircraftDto(), loginResponse);

	BookingDto intialResponseNewBookingDto = postBookingDto(
	    createNewBookingDto(aircraftDto.aircraftId(),
		LocalDateTime.parse("2076-12-31T00:00:01", dateTimeFormatter),
		LocalDateTime.parse("2076-12-31T23:59:59", dateTimeFormatter)
	    ),
	    loginResponse
	);
	assertThat(intialResponseNewBookingDto).isNotNull();

	ExceptionDetailsDto rejectionResponseDto = postBookingDto(
	    createNewBookingDto(aircraftDto.aircraftId(),
		LocalDateTime.parse("2076-12-31T00:00:01", dateTimeFormatter),
		LocalDateTime.parse("2076-12-31T23:59:59", dateTimeFormatter)
	    ),
	    loginResponse,
	    HttpStatus.BAD_REQUEST,
	    ExceptionDetailsDto.class
	);
	assertThat(rejectionResponseDto).isNotNull();
	assertThat(rejectionResponseDto.errorMessage()).matches(".*overlap.*"); // TODO: maybe case insensitive or something?
    }

    @Test
    public void bookingUpdatesShouldWork() {
	User newUser = createNewUser();
	registerNewUser(newUser);
	LoginResponse loginResponse = loginNewUser(newUser);
	AircraftDto aircraftDto = postAircraftDto(createNewAircraftDto(), loginResponse);

	BookingDto intialResponseNewBookingDto = postBookingDto(
	    createNewBookingDto(aircraftDto.aircraftId(),
		LocalDateTime.parse("2076-12-31T00:00:01", dateTimeFormatter),
		LocalDateTime.parse("2076-12-31T23:59:59", dateTimeFormatter)
	    ),
	    loginResponse
	);
	assertThat(intialResponseNewBookingDto).isNotNull();

	BookingDto updatedResponseNewBookingDto = postBookingDto(
	    createNewBookingDto(intialResponseNewBookingDto.bookingId(),
		aircraftDto.aircraftId(),
		LocalDateTime.parse("2076-12-31T00:00:00", dateTimeFormatter),
		LocalDateTime.parse("2077-01-01T00:00:00", dateTimeFormatter)
	    ),
	    loginResponse
	);
	assertThat(updatedResponseNewBookingDto).isNotNull();

	assertThat(updatedResponseNewBookingDto.bookingId()).isEqualTo(intialResponseNewBookingDto.bookingId());
	assertThat(updatedResponseNewBookingDto.aircraftId()).isEqualTo(intialResponseNewBookingDto.aircraftId());
    }

    @Test
    public void bookingsRetrievalByTimeRangeShouldWork() {
	User newUser = createNewUser();
	registerNewUser(newUser);
	LoginResponse loginResponse = loginNewUser(newUser);
	AircraftDto aircraftDto = postAircraftDto(createNewAircraftDto(), loginResponse);

	BookingDto requestNewBookingDto1 = createNewBookingDto(aircraftDto.aircraftId(),
	    LocalDateTime.parse("2076-12-31T00:00:01", dateTimeFormatter),
	    LocalDateTime.parse("2076-12-31T06:09:00", dateTimeFormatter));
	BookingDto responseNewBookingDto1 = postBookingDto(requestNewBookingDto1, loginResponse);

	BookingDto requestNewBookingDto2 = createNewBookingDto(aircraftDto.aircraftId(),
	    LocalDateTime.parse("2076-12-31T23:00:00", dateTimeFormatter),
	    LocalDateTime.parse("2076-12-31T23:04:20", dateTimeFormatter));
	BookingDto responseNewBookingDto2 = postBookingDto(requestNewBookingDto2, loginResponse);

	// TODO: fill out the rest of this test
    }
}
