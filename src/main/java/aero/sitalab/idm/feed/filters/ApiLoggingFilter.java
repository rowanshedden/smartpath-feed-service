package aero.sitalab.idm.feed.filters;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import aero.sitalab.idm.feed.models.dto.ApiLogRecord;
import aero.sitalab.idm.feed.utils.MiscUtil;

/**
 * ApiLogging Filter
 * <p>
 * This filter will log the API request and response using the configured Logger
 * and invoke the audit of the API call. The Logger data can be truncated to
 * limit the amount of data being logged, however, the audit log is never
 * truncated.
 * <p>
 * A transaction id is added so that the logged information can be correlated.
 */
@Configuration
public class ApiLoggingFilter extends OncePerRequestFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${logging.dump-headers}")
	String dumpHeaders;

	@Value("${logging.skip}")
	String[] doNotLog;

	@Value("${logging.short}")
	String[] shortForm;

	@Autowired
	private AutowireCapableBeanFactory beanFactory;

	@Bean
	public FilterRegistrationBean<Filter> apiLoggingFilterRegistration() {
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
		Filter apiLoggingFilter = new ApiLoggingFilter();
		beanFactory.autowireBean(apiLoggingFilter);
		filterRegistrationBean.setFilter(apiLoggingFilter);
		filterRegistrationBean.addUrlPatterns("/api/*");
		return filterRegistrationBean;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		if (logger.isDebugEnabled() && dumpHeaders.equalsIgnoreCase("true")) {
			logHeader(request);
		}

		/*
		 * Set the transaction id in order to trace the entry
		 */
		String traceID = UUID.randomUUID().toString();
		String header = request.getHeader("X-Trace-Id");
		if (header != null && !header.isEmpty()) {
			traceID = request.getHeader("X-Trace-Id");
		}
		MDC.put("transactionId", traceID);

		/*
		 * Create the API log record - request start
		 */
		ApiLogRecord apiLog = new ApiLogRecord();
		apiLog.setRqTimestamp(LocalDateTime.now());
		apiLog.setPath(request.getServletPath());
		apiLog.setQuery(request.getQueryString());
		apiLog.setMethod(request.getMethod());

		String apikey = request.getHeader("X-apikey");
		String authorization = request.getHeader("Authorization");
		if (StringUtils.hasText(apikey))
			apiLog.setUser(MiscUtil.extractUsernameFromJWT(apikey.trim()));
		else if (StringUtils.hasText(authorization))
			apiLog.setUser(MiscUtil.extractUsernameFromJWT(authorization.substring("Bearer ".length())).trim());

		MDC.put("userId", apiLog.getUser());

		/*
		 * Process the request through the next filter in the chain
		 */
		ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
		filterChain.doFilter(wrappedRequest, wrappedResponse);

		/*
		 * Continue with the API log record - request/response ended
		 */
		apiLog.setDuration(TimeUnit.NANOSECONDS.toMillis(LocalDateTime.now().getNano() - apiLog.getRqTimestamp().getNano()));
		apiLog.setRsTimestamp(LocalDateTime.now());
		apiLog.setRequest(MiscUtil.toJson(MiscUtil.fromUTF8Bytes(wrappedRequest.getContentAsByteArray()).replaceAll("\n", "").replaceAll("\\\\", "")));
		apiLog.setResponse(
				MiscUtil.toJson(MiscUtil.fromUTF8Bytes(wrappedResponse.getContentAsByteArray()).replaceAll("\n", "").replaceAll("\\\\", "")));
		apiLog.setResponseCode(response.getStatus());

		/*
		 * IMPORTANT: copy content of response back into original response
		 */
		wrappedResponse.copyBodyToResponse();

		/*
		 * Log to console
		 */
		if (okToLog(apiLog.getPath()))
			logger.info(this.applyShortFormSubstitution(apiLog).toJson());
	}

	private boolean okToLog(String apiPath) {
		for (String pathParms : doNotLog) {
			String[] parms = pathParms.split(":");
			if (parms[0].equals("equals") && parms[1].equals(apiPath))
				return false;
			else if (parms[0].equals("startswith") && parms[1].startsWith(apiPath))
				return false;
		}
		return true;
	}

	private ApiLogRecord applyShortFormSubstitution(ApiLogRecord apiLog) {
		for (String pathParms : shortForm) {
			String[] parms = pathParms.split(":");
			if (parms[0].equals("equals") && parms[1].equals(apiLog.getPath())) {
				apiLog.setRequest("...");
				apiLog.setResponse("...");
				break;
			} else if (parms[0].equals("startswith") && parms[1].startsWith(apiLog.getPath())) {
				apiLog.setRequest("...");
				apiLog.setResponse("...");
				break;
			}
		}
		return apiLog;
	}

	/**
	 * Log headers but ignore Elastic Load Balancer health checks
	 * 
	 * @param request
	 */
	private void logHeader(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		if (userAgent == null || !userAgent.startsWith("ELB-HealthChecker")) {
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = headerNames.nextElement();
				logger.debug(headerName + ": " + request.getHeader(headerName));
			}
		}
	}

}