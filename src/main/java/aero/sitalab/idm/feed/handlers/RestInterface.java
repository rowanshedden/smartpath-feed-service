package aero.sitalab.idm.feed.handlers;

import aero.sitalab.idm.feed.exception.ServiceException;
import aero.sitalab.idm.feed.models.dto.Error;
import aero.sitalab.idm.feed.models.dto.RawHttpResult;
import aero.sitalab.idm.feed.models.dto.smartpath.OauthTokenRequest;
import aero.sitalab.idm.feed.models.dto.smartpath.OauthTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Instant;

@Component
public class RestInterface {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;

    private HttpHeaders headers;
    private OauthTokenResponse token;
    private long lastAccessInSeconds;

    @PostConstruct
    void postConstruct() {
        headers = new HttpHeaders();
    }

    /**
     * Call the Oauth server to get the access token for Smart Path Hub API
     *
     * @param request
     */
    public OauthTokenResponse call(String accessTokenUrl, OauthTokenRequest request) {
        if (token != null) {
            long accessTokenExpiry = token.getExpiresIn();
            long secondsSinceLastAccess = (Instant.now().getEpochSecond() - lastAccessInSeconds);
            logger.debug("Access token secondsSinceLastAccess: {}, accessTokenExpiry: {}", secondsSinceLastAccess, accessTokenExpiry - 1);
            if (secondsSinceLastAccess >= accessTokenExpiry - 1) {
                logger.debug("Access token being refreshed ...");
                getToken(accessTokenUrl, request);
            }
        } else {
            token = OauthTokenResponse.builder().build();
            getToken(accessTokenUrl, request);
        }
        return token;
    }

    private void getToken(String accessTokenUrl, OauthTokenRequest request) {
        try {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            ResponseEntity<OauthTokenResponse> result = restTemplate.exchange(accessTokenUrl, HttpMethod.POST,
                    new HttpEntity<String>(request.toFormUrlEncoded(), headers), OauthTokenResponse.class);
            token = result.getBody();
            token.setSuccess(true);
            lastAccessInSeconds = (Instant.now().getEpochSecond());
        } catch (Exception e) {
            logger.error("token exception: {}", e.getLocalizedMessage());
            token.setSuccess(false);
            token.setError(new Error(System.currentTimeMillis(), 1001, "authentication error", e.getLocalizedMessage(), null));
        }
    }

    /**
     * Call the API
     *
     * @param domain
     * @param path
     * @param httpMethod
     * @param body
     * @return
     */
    public RawHttpResult call(String domain, String path, HttpMethod httpMethod, String body) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        RawHttpResult result = new RawHttpResult();

        if (!path.startsWith("/"))
            path = "/" + path;
        String url = domain + path;
        try {
            switch (httpMethod.name().toUpperCase()) {
                case "GET":
                    result = doGet(url, result);
                    break;
                case "POST":
                    result = doPost(url, body, result);
                    break;
                case "PUT":
                    result = doPut(url, body, result);
                    break;
                case "DELETE":
                    doDelete(url);
                    break;
            }
        } catch (Exception e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
        return result;
    }

    private RawHttpResult doGet(String url, RawHttpResult result) {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        result = this.processResult(url, response, result);
        return result;
    }

    private RawHttpResult doPost(String url, String body, RawHttpResult result) {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<String>(body, headers), String.class);
        result = this.processResult(url, response, result);
        return result;
    }

    private RawHttpResult doPut(String url, String body, RawHttpResult result) {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<String>(body, headers), String.class);
        result = this.processResult(url, response, result);
        return result;
    }

    private void doDelete(String url) {
        restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
    }

    private RawHttpResult processResult(String url, ResponseEntity<String> response, RawHttpResult result) {
        result.setHttpStatus("" + response.getStatusCodeValue());
        result.setBody(response.getBody());
        if (response.getStatusCodeValue() > 200) {
            Error error = new Error();
            error.setField(url);
            error.setMessage("");
            error.setStatus(response.getStatusCodeValue());
            error.setError("Smart Path Feed Error");
            result.setError(error);
            result.setSuccess(false);
        } else {
            result.setSuccess(true);
        }
        return result;
    }

}
