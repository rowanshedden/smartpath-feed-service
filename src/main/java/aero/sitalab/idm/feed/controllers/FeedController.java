package aero.sitalab.idm.feed.controllers;

import aero.sitalab.idm.feed.handlers.WebSocketHandler;
import aero.sitalab.idm.feed.models.dto.BaseResponse;
import aero.sitalab.idm.feed.models.dto.Error;
import aero.sitalab.idm.feed.models.dto.InterfaceResponse;
import aero.sitalab.idm.feed.models.dto.smartpath.biometrictoken.BiometricTokenRequest;
import aero.sitalab.idm.feed.models.dto.smartpath.biometrictoken.BiometricTokenResponse;
import aero.sitalab.idm.feed.models.dto.smartpath.enrolment.EnrolmentRequest;
import aero.sitalab.idm.feed.models.dto.smartpath.enrolment.EnrolmentResponse;
import aero.sitalab.idm.feed.services.FeedService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"Smart Path Relay API"}, description = "Smart Path relay service")
public class FeedController extends Api_1_0 {

    @Value("${app.use.interface}")
    private String useInterface;

    @Autowired
    private FeedService feedService;

    @Autowired
    private WebSocketHandler galleryWebSocketClient;

    @ApiOperation(value = "Feed a biometric token to Smart Path", authorizations = {
            @Authorization(value = "Bearer")}, response = BiometricTokenResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Smart Path Hub receives a biometric token")})
    @RequestMapping(value = "feed/biometric-token", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> feedBiometricToken(@Validated @RequestBody BiometricTokenRequest request) {
        BiometricTokenResponse response = feedService.feedBiometricTokenToSmartPath(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Feed an enrolment request to Smart Path", authorizations = {
            @Authorization(value = "Bearer")}, response = BiometricTokenResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Smart Path Hub receives an enrolment request")})
    @RequestMapping(value = "feed/enrolment-request", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> feedEnrolmentRequest(@Validated @RequestBody EnrolmentRequest request) {
        EnrolmentResponse response = feedService.feedEnrolmentRequestToSmartPath(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Show which interface is being used to obtain the gallery record", authorizations = {
            @Authorization(value = "Bearer")}, response = BaseResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "active interface is returned")})
    @RequestMapping(value = "feed/interface", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> showInterface() {
        InterfaceResponse response = new InterfaceResponse();
        if (useInterface.equalsIgnoreCase(FeedService.WEBSOCKET_INTERFACE) || useInterface.equalsIgnoreCase(FeedService.WEBHOOK_INTERFACE) || useInterface.equalsIgnoreCase(FeedService.BOTH_INTERFACE)) {
            response.setActiveinterface(useInterface);
            response.setSuccess(true);
        }
        else {
            Error error = new Error();
            error.setMessage("No interfaces are supported");
            error.setField("api/v1.0/feed/interface");
            response.setError(error);
        }
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Restart the Gallery WebSocket client", authorizations = {@Authorization(value = "Bearer")}, response = BaseResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "WebSocket client restarted")})
    @RequestMapping(value = "websocket/restart/gallery", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> restartClient() {
        BaseResponse response = galleryWebSocketClient.restart();
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Check status of websocket connection (true = connected)", authorizations = {@Authorization(value = "Bearer")}, response = BaseResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "WebSocket client is connected")})
    @RequestMapping(value = "websocket/connected", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> isConnected() {
        BaseResponse response = galleryWebSocketClient.isConnected();
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

}