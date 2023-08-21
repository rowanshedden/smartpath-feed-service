package aero.sitalab.idm.feed.services;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import aero.sitalab.idm.feed.configuration.WebSocketConfig;
import aero.sitalab.idm.feed.models.dto.SmartPathFeederServiceAction;
import aero.sitalab.idm.feed.utils.MiscUtil;

@Component
@ConditionalOnBean(WebSocketConfig.class)
public class WebSocketService extends TextWebSocketHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

	public WebSocketService() {
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info(String.format("WebSocket session: %s", session.toString()));
		sessions.add(session);
		super.afterConnectionEstablished(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		logger.info(String.format("WebSocket session: %s status: %s", session.toString(), status.toString()));
		sessions.remove(session);
		super.afterConnectionClosed(session, status);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) {
		if (sessions.isEmpty()) {
			logger.info("WebSocket sessions: none");
			return;
		}
		try {
			if (session != null) {
				logger.info(String.format("WebSocket receive: session id [%s], from [%s], message [%s]", session.getId(), session.getRemoteAddress(),
						message.getPayload()));
				TextMessage response = null;
				SmartPathFeederServiceAction action = (SmartPathFeederServiceAction) MiscUtil.fromJson(message.getPayload(), SmartPathFeederServiceAction.class);
				if (action.getAction().toUpperCase().equals(SmartPathFeederServiceAction.FETCH)) {
					response = new TextMessage("TODO-fetch-something".getBytes());
					for (WebSocketSession webSocketSession : sessions) {
						if (session.getId().equals(webSocketSession.getId())) {
							webSocketSession.sendMessage(response);
							logger.info(String.format("WebSocket send: session id [%s], message [%s]", webSocketSession.getId(), message.getPayload()));
						}
					}
				}
			} else {
				for (WebSocketSession webSocketSession : sessions) {
					webSocketSession.sendMessage(message);
					logger.info(String.format("WebSocket send: session id [%s], message [%s]", webSocketSession.getId(), message.getPayload()));
				}
			}
		} catch (Exception e) {
			logger.error("WebSocket error: {}", e.getLocalizedMessage());
		}
	}

}