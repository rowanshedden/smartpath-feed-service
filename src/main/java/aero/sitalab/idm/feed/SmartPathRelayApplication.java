package aero.sitalab.idm.feed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import aero.sitalab.idm.feed.utils.MiscUtil;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EntityScan(basePackages = "aero.sitalab.idm")
@ComponentScan(basePackages = "aero.sitalab.idm")
@EnableScheduling
@EnableSwagger2
@SpringBootApplication(scanBasePackages = "aero.sitalab.idm")
public class SmartPathRelayApplication {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${app.name}")
	String name;

	@Value("${app.version}")
	String version;

	public static void main(String[] args) {
		SpringApplication.run(SmartPathRelayApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			logger.info("\n\n─ •✧✧•  {}  {}  •✧✧• ─\n", name, version);
		};
	}

	@Bean
	@Primary
	public ObjectMapper objectMapper() {
		return MiscUtil.getOrCreateSerializingObjectMapper();
	}

	@Bean
	@Primary
	public Gson gson() {
		return MiscUtil.getOrCreateSerializingGsonBuilder();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(getClientHttpRequestFactory());
	}

	/**
	 * Set timeouts (in milliseconds) for RestTemplate calls
	 * 
	 * @return
	 */
	private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(30_000);
		clientHttpRequestFactory.setReadTimeout(50_000);
		return clientHttpRequestFactory;
	}

}