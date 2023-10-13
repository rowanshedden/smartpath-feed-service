package aero.sitalab.idm.feed;

import aero.sitalab.idm.feed.handlers.RestInterfaceHandler;
import aero.sitalab.idm.feed.models.dto.GalleryAction;
import aero.sitalab.idm.feed.models.dto.smartpath.OauthTokenRequest;
import aero.sitalab.idm.feed.models.dto.smartpath.OauthTokenResponse;
import aero.sitalab.idm.feed.services.FeedService;
import aero.sitalab.idm.feed.utils.MiscUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest()
public class ServiceTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    FeedService service;
    @Autowired
    private FeedService feedService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CommandLineRunner commandLineRunner;
    private MockRestServiceServer mockServer;
    @Autowired
    private RestInterfaceHandler restInterface;
    @Value("${app.feeder.username}")
    private String username;
    @Value("${app.feeder.password}")
    private String password;
    @Value("${app.feeder.access.token.url}")
    private String accessTokenUrl;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void get_oauth_access_token() {
        try {
            GalleryAction action = (GalleryAction) MiscUtil.fromJson(this.readFile("gallery-action.json"), GalleryAction.class);
            OauthTokenResponse oauthTokenResponse = (OauthTokenResponse) MiscUtil.fromJson(this.readFile("access-token.json"), OauthTokenResponse.class);
            /*
             * mock the Agent REST call
             */
            mockServer.expect(ExpectedCount.once(), requestTo(new URI("https://57.1.226.8:8443/oauth/token?grant_type=client_credentials")))
                    .andExpect(method(HttpMethod.POST))
                    .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(oauthTokenResponse)));
            // run the test
            OauthTokenRequest oauthTokenRequest = OauthTokenRequest.builder()
                    .username(username)
                    .password(password)
                    .build();
            OauthTokenResponse accessTokenResponse = restInterface.call(accessTokenUrl, oauthTokenRequest);
            // check the result
            mockServer.verify();
            assertThat(accessTokenResponse).isNotNull();
            assertThat(accessTokenResponse.getAccessToken()).isEqualTo(oauthTokenResponse.getAccessToken());
            assertThat(accessTokenResponse.getExpiresIn()).isEqualTo(oauthTokenResponse.getExpiresIn());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getLocalizedMessage());
        }
    }

    private String readFile(String fileName) throws IOException {
        var classLoader = getClass().getClassLoader();
        return new String(classLoader.getResourceAsStream(fileName).readAllBytes());
    }

}
