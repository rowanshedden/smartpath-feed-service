package aero.sitalab.idm.feed.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Value("${swagger.enabled:false}")
	private boolean enableSwagger;

	/**
	 * Swagger configuration
	 *
	 * @return Docket
	 */
	@Bean
	public Docket api() {
		if (enableSwagger)
			return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("aero.sitalab.idm"))
					.paths(PathSelectors.regex("/api.*")).build().apiInfo(apiInfo());
		return null;
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Smart Path Relay", "smart-path-relay", "1.0",
				"https://www.sita.aero/legal/sita-suppliers/terms-and-conditions/",
				new Contact("CEC Singapore (Yudhy Wijaya)", "https://www.sita.aero/about-us/contact-us/customer-experience-centers/singapore-cec/", "yudhy.wijaya@sita.aero"),
				"© 2023 SITA, all rights reserved", "https://www.sita.aero", Collections.emptyList());
	}

}