package aero.sitalab.idm.feed.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import aero.sitalab.idm.feed.services.WebSocketService;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	WebSocketService webSocketService;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
		webSocketHandlerRegistry.addHandler(webSocketService, "/api/events").setAllowedOrigins("*");
		logger.info("WebSocket configured");
	}

}
