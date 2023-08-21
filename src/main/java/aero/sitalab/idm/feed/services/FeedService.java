package aero.sitalab.idm.feed.services;

import aero.sitalab.idm.feed.models.dto.Error;
import aero.sitalab.idm.feed.models.dto.GalleryRecord;
import aero.sitalab.idm.feed.models.dto.Identity;
import aero.sitalab.idm.feed.handlers.RestInterface;
import aero.sitalab.idm.feed.models.dto.GalleryAction;
import aero.sitalab.idm.feed.models.dto.RawHttpResult;
import aero.sitalab.idm.feed.models.dto.RouteDetails;
import aero.sitalab.idm.feed.models.dto.smartpath.*;
import aero.sitalab.idm.feed.models.dto.smartpath.biometrictoken.*;
import aero.sitalab.idm.feed.models.dto.smartpath.enrolment.*;
import aero.sitalab.idm.feed.utils.MiscUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Feed Service
 */
@Service
@DependsOn("ConfigurationsHaveBeenLoaded")
public class FeedService {

    public final static String WEBSOCKET_INTERFACE = "websocket";
    public final static String WEBHOOK_INTERFACE = "webhook";
    public final static String BOTH_INTERFACE = "both";
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private RestInterface restInterface;

    @Value("${app.feeder.gallery.url}")
    private String galleryUrl;

    @Value("${app.feeder.gallery.path}")
    private String galleryPath;

    @Value("${app.feeder.access.token.url}")
    private String accessTokenUrl;

    @Value("${app.feeder.sph.url}")
    private String smartPathHubUrl;

    @Value("${app.feeder.sph.paths}")
    private String smartPathHubPaths;

    @Value("${app.feeder.grant_type}")
    private String grantType;

    @Value("${app.feeder.client_secret}")
    private String clientSecret;

    @Value("${app.feeder.client_id}")
    private String clientId;

    @Value("${app.feeder.scope}")
    private String scope;

    private long lastConfigurationUpdateInSeconds;
    private String[] smartPathHubPath;

    /**
     * Initializer
     */
    @PostConstruct
    private void postConstruct() {
        loadConfiguration();
    }

    /**
     * Feed Smart Path Hub from received enrolment request.
     * <p>
     * The Feed Service will receive an enrolment request from the REST API
     * which will be sent to the Smart Path Hub API defined by configuration
     * property app.feeder.sph.url
     *
     * @param enrolmentRequest
     * @return
     */
    public EnrolmentResponse feedEnrolmentRequestToSmartPath(EnrolmentRequest enrolmentRequest) {
        log.info("feed Smart Path Hub with EnrolmentRequest: {}", enrolmentRequest.toJson());
        return sendEnrolmentRequestToSmartPathHub(enrolmentRequest);
    }

    /**
     * Feed Smart Path Hub from WebSocket ADD message.
     * <p>
     * The Feed Service will receive a GalleryAction message of ADD from the
     * connected Gallery Service over the WebSocket connection. The received
     * GalleryAction will be transformed into an EnrolmentRequest and the Smart
     * Path Hub API defined by configuration property app.feeder.sph.url
     * will be consumed.
     *
     * @param galleryAction
     * @return
     */
    public void feedEnrolmentRequestToSmartPath(GalleryAction galleryAction) {
        log.debug("feed Smart Path Hub with GalleryAction: {}", galleryAction.toJson());
        /*
         * extract the gallery record
         */
        GalleryRecord galleryRecord = galleryAction.getData();
        /*
         * the boarding pass data is not present in the GalleryAction, so it has to be mocked
         */
        String bcbp = "M1ANDREWS/CONNOR      EVGUM42 PEKHNDJL 0022 173Y061D0106 348>3180  9260B1A 01313064030012913121336002510 JL JL 332188577           8";
        List<LiveBiometric> liveBiometrics = new ArrayList<LiveBiometric>();
        LiveBiometric liveBiometric = LiveBiometric.builder()
                .biometricData(galleryRecord.getDtc().getChipImage())
                .biometricType("FACE")
                .build();
        liveBiometrics.add(liveBiometric);
        List<TravelDocumentInfo> travelDocumentInfos = new ArrayList<TravelDocumentInfo>();
        TravelDocumentInfo travelDocumentInfo = TravelDocumentInfo.builder()
                .dateOfBirth(galleryRecord.getDtc().getDateOfBirth())
                .documentCode("MAN")
                .sex(galleryRecord.getDtc().getGender())
                .expiryDate(galleryRecord.getDtc().getExpiryDate())
                .issuingState(galleryRecord.getDtc().getIssuingState())
                .nationality(galleryRecord.getDtc().getNationality())
                .primaryName(galleryRecord.getDtc().getGivenNames())
                .secondaryName(galleryRecord.getDtc().getFamilyName())
                .travelDocumentNumber(galleryRecord.getDtc().getDocumentType() + galleryRecord.getDtc().getDocumentNumber())
                .build();
        travelDocumentInfos.add(travelDocumentInfo);
        GroupInfo groupInfo = GroupInfo.builder()
                .airportCode(galleryRecord.getItinerary().getRouteDetails().get(0).getArrival().getPortCode())
                .location("")
                .terminal("")
                .build();

        /*
         * construct a EnrolmentRequest object
         */
        /*
        {
  "messageId": "c87ab2b1-a22e-411c-8e54-ce4ee829c1d5",
  "correlationId": "300f6c30-9004-4b28-b156-a913ef469f70",
  "messageTs": "2020-02-04T21:31:50.123Z",
  "clientId": "PEK3CKBD11",
  "groupInfo": {
    "airportCode": "PEK",
    "terminal": "C",
    "location": "10"
  },
  "liveBiometric": [
    {
      "biometricType": "FACE",
      "biometricData": "/9j/4AAQSkZJRgABAQEASABIxoF8nw8Jh5PQenNgOdZEA3d4Gm11kzS2a94He35z//Z"
    }
  ],
  "travelDocumentInfo": [
    {
      "travelDocumentNumber": "PP5992615",
      "documentCode": "MAN",
      "primaryName": "ANDREWS",
      "secondaryName": "CONOR",
      "expiryDate": "2027-01-01",
      "issuingState": "GBR",
      "nationality": "GBR",
      "dateOfBirth": "1955-05-13",
      "sex": "F"
    }
  ],
  "BCBP": "M1ANDREWS/CONNOR      EVGUM42 PEKHNDJL 0022 173Y061D0106 348>3180  9260B1A 01313064030012913121336002510 JL JL 332188577           8",
  "MRZ": "P<IRLANDREWS<<CONNOR<<<<<<<<<<<<<<<<<<<<<<<<PP59926157IRL8111100M2612096<<<<<<<<<<<<<<04"
}
         */
        EnrolmentRequest enrolmentRequest = EnrolmentRequest.builder()
                .messageId(UUID.randomUUID().toString())
                .messageTs((String.valueOf(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))))
                .clientId("")
                .correlationId(UUID.randomUUID().toString())
                .groupInfo(groupInfo)
                .bcbp(bcbp)
                .travelDocumentInfo(travelDocumentInfos)
                .liveBiometric(liveBiometrics)
                .mrz(galleryRecord.getDtc().getMrz())
                .build();
        sendEnrolmentRequestToSmartPathHub(enrolmentRequest);
    }

    /**
     * Send the EnrolmentRequest to the Smart Path Hub
     *
     * @param enrolmentRequest
     * @return
     */
    private EnrolmentResponse sendEnrolmentRequestToSmartPathHub(EnrolmentRequest enrolmentRequest) {
        loadConfiguration();
        /*
         *  obtain the JWT (only valid for an hour)
         */
        try {
            OauthTokenResponse accessTokenResponse = restInterface.call(accessTokenUrl, new OauthTokenRequest(grantType, clientSecret, clientId, scope));
            if (accessTokenResponse.getAccessToken() == null) {
                String errorMessage = "Unable to obtain JWT";
                log.error(errorMessage);
                EnrolmentResponse response = EnrolmentResponse.builder().build();
                response.setError(new Error(System.currentTimeMillis(), 1001, "authentication error", errorMessage, null));
                return response;
            }
            log.debug("Oauth token: {}", accessTokenResponse.getAccessToken());
        } catch (Exception e) {
            String errorMessage = "Unable to obtain JWT: " + e.getLocalizedMessage();
            log.error(errorMessage);
            EnrolmentResponse response = EnrolmentResponse.builder().build();
            response.setError(new Error(System.currentTimeMillis(), 1001, "authentication error", errorMessage, null));
            return response;
        }
        /*
         * enrol into Smart Path using the EnrolmentRequest
         */
        try {
            RawHttpResult result = restInterface.call(smartPathHubUrl, smartPathHubPath[0], HttpMethod.POST, enrolmentRequest.toJson());
            String res =  result.toJson();
            log.debug("result: {}", res);
            return (EnrolmentResponse) MiscUtil.fromJson(res, EnrolmentResponse.class);
        } catch (Exception e) {
            String errorMessage = "Unable to enrol: " + e.getLocalizedMessage();
            log.error(errorMessage);
            EnrolmentResponse response = EnrolmentResponse.builder().build();
            response.setError(new Error(System.currentTimeMillis(), 1001, "enrolment error", errorMessage, null));
            return response;
        }
    }

    /**
     * Feed Smart Path Hub from received biometric token.
     * <p>
     * The Feed Service will receive a BiometricToken from the REST API
     * which will be sent to the Smart Path Hub API defined by configuration
     * property app.feeder.sph.url
     *
     * @param biometricToken
     * @return
     */
    public BiometricTokenResponse feedBiometricTokenToSmartPath(BiometricTokenRequest biometricToken) {
        log.info("feed Smart Path Hub with BiometricToken: {}", biometricToken.toJson());
        return sendBiometricTokenToSmartPathHub(biometricToken);
    }

    /**
     * Feed Smart Path Hub from WebSocket ADD message.
     * <p>
     * The Feed Service will receive a GalleryAction message of ADD from the
     * connected Gallery Service over the WebSocket connection. The received
     * GalleryAction will be transformed into a BiometricToken and the Smart
     * Path Hub API defined by configuration property app.feeder.sph.url
     * will be consumed.
     *
     * @param galleryAction
     * @return
     */
    public void feedBiometricTokenToSmartPath(GalleryAction galleryAction) {
        log.debug("feed Smart Path Hub with GalleryAction: {}", galleryAction.toJson());
        /*
         * extract the gallery record
         */
        GalleryRecord galleryRecord = galleryAction.getData();
        /*
         * the boarding pass data is not present in the GalleryAction, so it has to be mocked
         */
        String bcbp = "M1WALL/MART           ENCJCON ISPHNDJL 0022 325Y002D0001 10";
        /*
         * construct a BiometricToken object
         */
        List<Confirmation> confirmations = new ArrayList<Confirmation>();
        Confirmation confirmation = Confirmation.builder()
                .confirmationType("BP")
                .confirmationData(bcbp)
                .build();
        confirmations.add(confirmation);
        String airportCode = null;
        for (RouteDetails routeDetails : galleryRecord.getItinerary().getRouteDetails()) {
            airportCode = routeDetails.getArrival().getPortCode();
            break;
        }
        TransactionInfo transactionInfo = TransactionInfo.builder()
                .airportCode(airportCode)
                .clientChannel("OTH")
                .messageId(UUID.randomUUID().toString())
                .messageTs(String.valueOf(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)))
                .build();
        MemberInfo memberInfo = MemberInfo.builder()
                .id(galleryAction.getUpk())
                .biographic(null)
                .biometric(null)
                .confirmation(confirmations)
                .build();
        BiometricTokenRequest biometricToken = BiometricTokenRequest.builder()
                .transactionInfo(transactionInfo)
                .memberInfo(memberInfo)
                .build();
        sendBiometricTokenToSmartPathHub(biometricToken);
    }

    /**
     * Send the BiometricToken to the Smart Path Hub
     *
     * @param biometricTokenRequest
     * @return
     */
    private BiometricTokenResponse sendBiometricTokenToSmartPathHub(BiometricTokenRequest biometricTokenRequest) {
        loadConfiguration();
        /*
         *  obtain the JWT (only valid for an hour)
         */
        try {
            OauthTokenResponse accessTokenResponse = restInterface.call(accessTokenUrl, new OauthTokenRequest(grantType, clientSecret, clientId, scope));
            log.debug("Oauth token: {}", accessTokenResponse.getAccessToken());
        } catch (Exception e) {
            String errorMessage = "Unable to obtain JWT: " + e.getLocalizedMessage();
            log.error(errorMessage);
            return BiometricTokenResponse.builder()
                    .messageID(biometricTokenRequest.getTransactionInfo().getMessageId())
                    .errorMessage(errorMessage)
                    .errorCode("")
                    .errorReasonCode("")
                    .success(false)
                    .build();
        }
        /*
         * enrol into Smart Path using the BiometricToken
         */
        try {
            RawHttpResult result = restInterface.call(smartPathHubUrl, smartPathHubPath[0], HttpMethod.POST, biometricTokenRequest.toJson());
            String res =  result.toJson();
            log.debug("result: {}", res);
            return (BiometricTokenResponse) MiscUtil.fromJson(res, BiometricTokenResponse.class);
        } catch (Exception e) {
            String errorMessage = "Unable to enrol using BiometricToken: " + e.getLocalizedMessage();
            log.error(errorMessage);
            return BiometricTokenResponse.builder()
                    .messageID(biometricTokenRequest.getTransactionInfo().getMessageId())
                    .errorMessage(errorMessage)
                    .errorCode("")
                    .errorReasonCode("")
                    .success(false)
                    .build();
        }
    }

    /**
     * Load the configuration property values from the Configuration table if
     * last update was more than 30 seconds ago. A really primitive form of
     * caching to prevent thrashing on the Configuration table.
     */
    private void loadConfiguration() {
        long secondsSinceLastUpdate = (Instant.now().getEpochSecond() - lastConfigurationUpdateInSeconds);
        if (secondsSinceLastUpdate > 30) {
            log.debug("secondsSinceLastUpdate: {}", secondsSinceLastUpdate);
            galleryUrl = configurationService.getLastestConfigurationValue("app.feeder.gallery.url");
            galleryPath = configurationService.getLastestConfigurationValue("app.feeder.gallery.path");
            accessTokenUrl = configurationService.getLastestConfigurationValue("app.feeder.access.token.url");
            smartPathHubUrl = configurationService.getLastestConfigurationValue("app.feeder.sph.url");
            smartPathHubPaths = configurationService.getLastestConfigurationValue("app.feeder.sph.paths");
            smartPathHubPath = smartPathHubPaths.split(",");
            grantType = configurationService.getLastestConfigurationValue("app.feeder.grant_type");
            clientSecret = configurationService.getLastestConfigurationValue("app.feeder.client_secret");
            clientId = configurationService.getLastestConfigurationValue("app.feeder.client_id");
            scope = configurationService.getLastestConfigurationValue("app.feeder.scope");
            lastConfigurationUpdateInSeconds = Instant.now().getEpochSecond();
        }
    }

}