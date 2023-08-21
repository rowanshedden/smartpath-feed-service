package aero.sitalab.idm.feed.configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("server")
public class CorsConfig {

	private int port;
	private Cors cors;

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Cors getCors() {
		return this.cors;
	}

	public void setCors(Cors cors) {
		this.cors = cors;
	}

	public static class Cors {
		private List<String> origins = new ArrayList<>();

		public List<String> getOrigins() {
			return this.origins;
		}

		public void setOrigins(List<String> origins) {
			this.origins = origins;
		}
	}
}
