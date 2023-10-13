package aero.sitalab.idm.feed.handlers;

import aero.sitalab.idm.feed.models.Action;
import aero.sitalab.idm.feed.models.dto.BaseResponse;
import aero.sitalab.idm.feed.models.dto.Error;
import aero.sitalab.idm.feed.models.dto.GalleryAction;
import aero.sitalab.idm.feed.services.FeedService;
import aero.sitalab.idm.feed.utils.MiscUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

@Component
public class WebSocketHandler {

    private static final int CONNECTED = 1;
    private static final int NOT_CONNECTED = 0;
    private static final String FEEDER_SERVICE_KEEP_ALIVE_HEART_BEAT = "keep-alive-heart-beat";
    private static final X509Certificate[] EMPTY_CERTIFICATES = new X509Certificate[0];
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final AtomicInteger isConnected = new AtomicInteger(NOT_CONNECTED);
    @Autowired
    private FeedService feedService;
    @Value("${app.use.interface}")
    private String useInterface;
    @Value("${app.use.biometric-token}")
    private boolean useBiometricToken;
    @Value("${app.feeder.ws.disable.ssl:false}")
    private boolean disableSsl;
    @Value("${app.feeder.ws.message.size:1}")
    private int messageSize;
    @Value("${app.feeder.ws.url:ws://localhost:8002/gallery}")
    private String webSocketUrl;
    @Value("${app.feeder.ws.connect.interval:30}")
    private int connectInterval;
    @Value("${app.feeder.ws.heartbeat.interval:45}")
    private int heartBeatInterval;
    private WebSocketClient webSocketClient = null;
    private WebSocketSession webSocketSession = null;

    /**
     * Connect the WebSocket when component is initialized by Spring Boot
     */
    @PostConstruct
    public void postConstruct() {
        isConnected.set(NOT_CONNECTED);
        scheduleConnection();
    }

    /**
     * Restart the connection by simply closing the current session
     *
     */
    public BaseResponse restart() {
        try {
            webSocketSession.close(CloseStatus.NORMAL);
        } catch (IOException e) {
            Error error = new Error();
            error.setError("websocket restart");
            error.setMessage(e.getLocalizedMessage());
            return new BaseResponse(false, error);
        }
        return new BaseResponse(true, null);
    }

    /**
     * Return true if connected
     *
     */
    public BaseResponse isConnected() {
        BaseResponse response = new BaseResponse();
        if (isConnected.get() == CONNECTED) {
            return new BaseResponse(true, null);
        } else {
            Error error = new Error();
            error.setError("websocket");
            error.setMessage("not connected");
            return new BaseResponse(false, error);
        }
    }

    /**
     * On startup connect to the Gallery Service websocket
     */
    private void connect() {
        if (useWebSocketInterface()) {
            try {
                if (isConnected.get() == NOT_CONNECTED) {
                    if (webSocketClient == null) {
                        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                        container.setDefaultMaxBinaryMessageBufferSize(1024 * 1024 * messageSize);
                        container.setDefaultMaxTextMessageBufferSize(1024 * 1024 * messageSize);
                        if (disableSsl)
                            webSocketClient = createStandardWebSocketClientWithSSLDisabled(container);
                        else
                            webSocketClient = new StandardWebSocketClient(container);
                    }

                    webSocketSession = webSocketClient.doHandshake(new TextWebSocketHandler() {

                        @Override
                        public void handleTextMessage(WebSocketSession session, TextMessage message) {
                            handleReceivedMessage(message);
                        }

                        @Override
                        public void afterConnectionEstablished(WebSocketSession session) {
                            log.info("+ ******************************");
                            log.info("+ WebSocket session established: {}", session);
                            log.info("+ ******************************");
                            isConnected.set(CONNECTED);
                        }

                        @Override
                        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
                            log.info("WebSocket session closed: {} {}", session.getId(), status.toString());
                            isConnected.set(NOT_CONNECTED);
                            scheduleConnection();
                        }

                    }, new WebSocketHttpHeaders(), URI.create(webSocketUrl)).get();
                    this.scheduleHeartBeat();
                }
            } catch (Exception e) {
                log.error("Exception accessing WebSocket: {}", e.getLocalizedMessage());
                isConnected.set(NOT_CONNECTED);
                scheduleConnection();
            }
        } else
            log.info("WebSocket interface not being used");
    }

    /**
     * Schedule the keep alive heart beat
     */
    private void scheduleHeartBeat() {
        newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                if (isConnected.get() == CONNECTED) {
                    TextMessage message = new TextMessage(FEEDER_SERVICE_KEEP_ALIVE_HEART_BEAT);
                    webSocketSession.sendMessage(message);
                }
            } catch (Exception e) {
                log.error("Exception sending keep alive heart beat: {}", e.getLocalizedMessage());
                try {
                    webSocketSession.close();
                } catch (IOException ignored) {
                }
                isConnected.set(NOT_CONNECTED);
                scheduleConnection();
            }
        }, 1, heartBeatInterval, TimeUnit.SECONDS);
    }

    /**
     * Schedule the WebSocket connection
     */
    private void scheduleConnection() {
        log.info("scheduling websocket connection in {} seconds", connectInterval);
        newSingleThreadScheduledExecutor().schedule(() -> {
            try {
                log.info("attempting websocket connection to {} with message size: {}MB", webSocketUrl, messageSize);
                if (isConnected.get() == NOT_CONNECTED) {
                    connect();
                }
            } catch (Exception e) {
                log.error("Exception initiating connection: {}", e.getLocalizedMessage());
                isConnected.set(NOT_CONNECTED);
            }
        }, connectInterval, TimeUnit.SECONDS);
    }

    /**
     * Handle the received message - should be a JSON encoded GalleryAction
     *
     * @param message TextMessage
     */
    private void handleReceivedMessage(TextMessage message) {
        String json = message.getPayload();
        if (!json.trim().startsWith("{")) {
            log.error("Received invalid message : {}", json);
            return;
        }
        /*
         * process the GalleryAction message
         */
        try {
            GalleryAction galleryAction = MiscUtil.getOrCreateSerializingObjectMapper().readValue(json, GalleryAction.class);
            /*
             * only handle ADD and UPDATE, anything else is ignored
             */
            try {
                if (galleryAction.getAction().equals(Action.ADD) || galleryAction.getAction().equals(Action.UPDATE)) {
                    if (useBiometricToken)
                        feedService.feedBiometricTokenToSmartPath(galleryAction);
                    else
                        feedService.feedEnrolmentRequestToSmartPath(galleryAction);
                } else {
                    log.info("Ignored GalleryAction message with action : {}", galleryAction.getAction());
                }
            } catch (Exception e) {
                log.error("Unable to process GalleryAction message: {}, exception: {}", json, e.getLocalizedMessage());
            }
        } catch (Exception e) {
            log.error("Unable to parse GalleryAction message: {}, exception: {}", json, e.getLocalizedMessage());
        }
    }

    private boolean useWebSocketInterface() {
        return useInterface.equalsIgnoreCase(FeedService.WEBSOCKET_INTERFACE) || useInterface.equalsIgnoreCase(FeedService.BOTH_INTERFACE);
    }

    /**
     * Disable SSL Certificate validation for the WebSocket client connection
     *
     * @param container WebSocketContainer
     * @return WebSocketClient
     * @throws NoSuchAlgorithmException exception
     * @throws KeyManagementException exception
     */
    private WebSocketClient createStandardWebSocketClientWithSSLDisabled(WebSocketContainer container) throws NoSuchAlgorithmException, KeyManagementException {
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient(container);
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return EMPTY_CERTIFICATES;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        Map<String, Object> properties = new HashMap<>();
        properties.put("org.apache.tomcat.websocket.SSL_CONTEXT", sc);
        standardWebSocketClient.setUserProperties(properties);
        log.info("WebSocket client is running with disabled SSL Certificate validation");
        return standardWebSocketClient;
    }

}