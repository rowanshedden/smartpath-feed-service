package aero.sitalab.idm.feed;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestConfig {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	TestConfig() {
		logger.info("TestConfig");
	}

	@PreDestroy
	public void cleanup() {
	}

	public void destroy() {
		cleanup();
	}
	
}
