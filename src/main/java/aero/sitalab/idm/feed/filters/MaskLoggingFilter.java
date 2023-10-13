package aero.sitalab.idm.feed.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Mask sensitive information in log files ... refer -
 * https://www.baeldung.com/logback-mask-sensitive-data
 * 
 */
@Component
public class MaskLoggingFilter extends PatternLayout {

	@Value("${app.mask.logging:false}")
	private boolean isMaskLogging;

	private Pattern multilinePattern;
	private final List<String> maskPatterns = new ArrayList<>();

	public void addMaskPattern(String maskPattern) {
		maskPatterns.add(maskPattern);
		multilinePattern = Pattern.compile(maskPatterns.stream().collect(Collectors.joining("|")), Pattern.MULTILINE);
	}

	@Override
	public String doLayout(ILoggingEvent event) {
		return maskMessage(super.doLayout(event));
	}

	private String maskMessage(String message) {
		// check the isMaskLogging switch
		if (!isMaskLogging || multilinePattern == null) {
			return message;
		}
		StringBuilder sb = new StringBuilder(message);
		Matcher matcher = multilinePattern.matcher(sb);
		while (matcher.find()) {
			IntStream.rangeClosed(1, matcher.groupCount()).forEach(group -> {
				if (matcher.group(group) != null) {
					IntStream.range(matcher.start(group), matcher.end(group)).forEach(i -> sb.setCharAt(i, '~'));
				}
			});
		}
		return sb.toString().replaceAll("[~]+", "...");
	}
}