package aero.sitalab.idm.feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import aero.sitalab.idm.feed.handlers.RestInterface;
import aero.sitalab.idm.feed.services.FeedService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest()
public class ServiceTests {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private CommandLineRunner commandLineRunner;

	private MockRestServiceServer mockServer;

	@BeforeEach
	public void init() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Autowired
	FeedService service;

	@Autowired
	private RestInterface restInterface;

	@Value("${app.feeder.grant_type}")
	private String grantType;

	@Value("${app.feeder.client_secret}")
	private String clientSecret;

	@Value("${app.feeder.client_id}")
	private String clientId;

	@Value("${app.feeder.scope}")
	private String scope;
	

	private String readFile(String fileName) throws IOException {
		var classLoader = getClass().getClassLoader();
		return new String(classLoader.getResourceAsStream(fileName).readAllBytes());
	}

}
