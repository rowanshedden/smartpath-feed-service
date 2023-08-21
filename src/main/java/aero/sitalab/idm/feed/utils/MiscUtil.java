package aero.sitalab.idm.feed.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import aero.sitalab.idm.feed.models.dto.Biographic;
import aero.sitalab.idm.feed.exception.ServiceException;
import aero.sitalab.idm.feed.models.IBiographic;

/**
 * All sorts of utility functions
 * 
 */
public class MiscUtil {

	private static ObjectMapper mObjectMapper;
	private static Gson mGson;
	private static List<String> upkAttributes;

	public static final Random msRand = new Random();
	public static final DateTimeFormatter basicHHMMTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	public static final ZoneId msUTCTimeZoneId = ZoneId.of("UTC");

	/**
	 * Return the object as a JSON string
	 * 
	 * @param arg0
	 * @return
	 */
	public static String toJson(Object arg0) {
		try {
			return getOrCreateSerializingObjectMapper().writeValueAsString(arg0);
		} catch (JsonProcessingException e) {
		}
		return "{}";
	}

	/**
	 * Return the JSON string as an object
	 * 
	 * @param json
	 * @return
	 */
	public static Object fromJson(String json, Class<?> clazz) {
		try {
			return getOrCreateSerializingObjectMapper().readValue(json, clazz);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Return the object or JSON string as a minified JSON string
	 * 
	 * @param arg0
	 * @return
	 */
	public static String toJsonMinified(Object arg0) {
		try {
			Gson gson = getOrCreateSerializingGsonBuilder();
			JsonElement el = null;
			if (arg0 instanceof String) {
				el = JsonParser.parseString((String) arg0);
			} else {
				el = JsonParser.parseString(toJson(arg0));
			}
			return gson.toJson(el);
		} catch (Exception e) {
		}
		return "{}";
	}

	/**
	 * Set the list of UPK attributes
	 * 
	 * @param upkAttributes
	 */
	public static void setUpkAttributes(String attributes) {
		upkAttributes = new ArrayList<String>();
		if (attributes != null) {
			String[] attrs = attributes.toLowerCase().split(",");
			for (int a = 0; a < attrs.length; a++) {
				upkAttributes.add(attrs[a].trim());
			}
		}
	}

	/**
	 * Generate the UPK (Unique Passenger Key) from an ordered list of String
	 * elements and return as a MD5 message digest String. Effectively making
	 * this a one-way encryption algorithm to obfuscate the data.
	 * 
	 * @param biographic
	 * @return
	 */
	public static String generateUPK(IBiographic biographic) {
		LinkedList<String> upkAttributeSet = new LinkedList<String>();
		if (biographic != null) {
			MiscUtil.emrtdDateToIsoDate((Biographic) biographic);
			if (upkAttributes.contains("type[p]")) {
				if (biographic.getDocumentType().toUpperCase().equals("P")) {
					if (upkAttributes.contains("document-number"))
						upkAttributeSet.add(biographic.getDocumentNumber().toUpperCase());
					if (upkAttributes.contains("issuing-state"))
						upkAttributeSet.add(biographic.getIssuingState().toUpperCase());
					if (upkAttributes.contains("given-names"))
						upkAttributeSet.add(biographic.getGivenNames().toUpperCase());
					if (upkAttributes.contains("family-name"))
						upkAttributeSet.add(biographic.getFamilyName().toUpperCase());
					if (upkAttributes.contains("date-of-birth"))
						upkAttributeSet.add(biographic.getDateOfBirth().toUpperCase());
					if (upkAttributes.contains("gender"))
						upkAttributeSet.add(biographic.getGender().toUpperCase());
					if (upkAttributes.contains("issuing-authority"))
						upkAttributeSet.add(biographic.getIssuingAuthority().toUpperCase());
					if (upkAttributes.contains("nationality"))
						upkAttributeSet.add(biographic.getNationality().toUpperCase());
					if (upkAttributes.contains("expiration-date"))
						upkAttributeSet.add(biographic.getExpiryDate().toUpperCase());
				}
			} else if (upkAttributes.contains("type[i]")) {
				if (biographic.getDocumentType().toUpperCase().equalsIgnoreCase("I")) {
					if (upkAttributes.contains("document-number"))
						upkAttributeSet.add(biographic.getDocumentNumber().toUpperCase());
					if (upkAttributes.contains("issuing-state"))
						upkAttributeSet.add(biographic.getIssuingState().toUpperCase());
					if (upkAttributes.contains("given-names"))
						upkAttributeSet.add(biographic.getGivenNames().toUpperCase());
					if (upkAttributes.contains("family-name"))
						upkAttributeSet.add(biographic.getFamilyName().toUpperCase());
					if (upkAttributes.contains("date-of-birth"))
						upkAttributeSet.add(biographic.getDateOfBirth().toUpperCase());
					if (upkAttributes.contains("gender"))
						upkAttributeSet.add(biographic.getGender().toUpperCase());
					if (upkAttributes.contains("issuing-authority"))
						upkAttributeSet.add(biographic.getIssuingAuthority().toUpperCase());
					if (upkAttributes.contains("expiration-date"))
						upkAttributeSet.add(biographic.getExpiryDate().toUpperCase());
				}
			} else if (upkAttributes.contains("type[o]")) {
				if (biographic.getDocumentType().equalsIgnoreCase("O")) {
					if (upkAttributes.contains("document-number"))
						upkAttributeSet.add(biographic.getDocumentNumber().toUpperCase());
					if (upkAttributes.contains("issuing-state"))
						upkAttributeSet.add(biographic.getIssuingState().toUpperCase());
					if (upkAttributes.contains("given-names"))
						upkAttributeSet.add(biographic.getGivenNames().toUpperCase());
					if (upkAttributes.contains("family-name"))
						upkAttributeSet.add(biographic.getFamilyName().toUpperCase());
					if (upkAttributes.contains("date-of-birth"))
						upkAttributeSet.add(biographic.getDateOfBirth().toUpperCase());
					if (upkAttributes.contains("gender"))
						upkAttributeSet.add(biographic.getGender().toUpperCase());
					if (upkAttributes.contains("issuing-authority"))
						upkAttributeSet.add(biographic.getIssuingAuthority().toUpperCase());
					if (upkAttributes.contains("expiration-date"))
						upkAttributeSet.add(biographic.getExpiryDate().toUpperCase());
				}
			}
		}
		if (upkAttributeSet.size() != (upkAttributes.size() - 1))
			throw new ServiceException("Required unique passenger attributes are missing");
		String upk = "";
		for (String element : upkAttributeSet) {
			upk += element;
		}
		return new DigestUtils("MD5").digestAsHex(upk);
	}

	/**
	 * Extract username from JWT token
	 * 
	 * @param jwt
	 * @return
	 */
	public static String extractUsernameFromJWT(String jwt) {
		if (jwt != null) {
			int secondFieldStartIndex = jwt.indexOf('.');
			if (secondFieldStartIndex > 0) {
				jwt = jwt.substring(secondFieldStartIndex + 1);
				int secondFieldEndIndex = jwt.indexOf('.');
				jwt = jwt.substring(0, secondFieldEndIndex);
				jwt = fromUTF8Bytes(Base64.getDecoder().decode(jwt));
				int subStart = jwt.indexOf("\"sub\":\"");
				if (subStart > 0) {
					jwt = jwt.substring(subStart + 7);
					int usernameEndIndex = jwt.indexOf('"');
					if (usernameEndIndex > 0)
						return jwt.substring(0, usernameEndIndex);
				}
			}
		}
		// assume API key caller which may not be a valid JWT
		return "api";
	}

	public static String padLeftZeros(String inputString, int length) {
		if (inputString.length() >= length) {
			return inputString;
		}
		StringBuilder sb = new StringBuilder();
		while (sb.length() < length - inputString.length()) {
			sb.append('0');
		}
		sb.append(inputString);
		return sb.toString();
	}

	/**
	 * Utility method to avoid NPE's in string comparisons, does an exact
	 * compare on 2 strings.
	 *
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean compare(String s1, String s2) {
		if ((s1 == null) && (s2 == null))
			return true;
		if ((s1 == null) || (s2 == null))
			return false;
		return s1.equals(s2);
	}

	public static boolean compareIgnoreCase(String s1, String s2) {
		if ((s1 == null) && (s2 == null))
			return true;
		if ((s1 == null) || (s2 == null))
			return false;
		return s1.equalsIgnoreCase(s2);
	}

	public static boolean compare(List<String> list1, List<String> list2) {
		if ((list1 == null) && (list2 == null))
			return true;
		if ((list1 == null) || (list2 == null))
			return false;
		if (list1.size() != list2.size())
			return false;
		for (int i = 0; i < list1.size(); i++) {
			if (!compare(list1.get(i), list2.get(i)))
				return false;
		}
		return true;
	}

	/**
	 * Must have written this a thousand times
	 *
	 * @param str
	 * @return true if null or ""
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static String genCSVStr(List<String> fields) {
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		if (fields != null) {
			for (String field : fields) {
				if (!first)
					sb.append(",");
				first = false;
				sb.append(field);
			}
		}
		return sb.toString();
	}

	public static boolean isInList(String str, List<String> strList) {
		if (str == null)
			return false;
		if (strList == null)
			return false;
		for (String tmp : strList) {
			if (compare(tmp, str))
				return true;
		}
		return false;
	}

	public static String notNull(String str) {
		if (str == null)
			str = "";
		return str;
	}

	/**
	 * Part of ensuring UTF-8 used throughout the code base, use this instead of
	 * String.getBytes();
	 *
	 * @param str
	 * @return
	 */
	public static byte[] getUTF8Bytes(String str) {
		byte[] result = null;
		if (str != null) {
			try {
				result = str.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				result = str.getBytes();
			}
		}
		return result;
	}

	/**
	 * Part of ensuring UTF-8 used throughout the code base, use this instead of
	 * new String(byte[]);
	 *
	 * @param bytes
	 * @return
	 */
	public static String fromUTF8Bytes(byte[] bytes) {
		try {
			if (bytes != null)
				return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return new String(bytes);
		}
		return null;
	}

	public static String cleanUppercase(String str) {
		return notNull(str).trim().toUpperCase();
	}

	public static String fullStackTrace(final Throwable th) {
		StringBuffer sb = new StringBuffer();
		sb.append("[ERROR] - \n");
		if (null != th) {
			final StackTraceElement[] traces = th.getStackTrace();
			if (null != traces && traces.length > 0) {
				sb.append("\n");
				sb.append(th.getClass()).append(": ").append(th.getMessage());
				sb.append("\n");
				for (final StackTraceElement trace : traces) {
					sb.append("    at ").append(trace.getClassName()).append(".").append(trace.getMethodName()).append("(").append(trace.getFileName())
							.append(":").append(trace.getLineNumber()).append(");\n");
				}
			}
			Throwable cause = th.getCause();
			while (null != cause) {
				final StackTraceElement[] causeTraces = cause.getStackTrace();
				if (null != causeTraces && causeTraces.length > 0) {
					sb.append("Caused By:\n").append(cause.getClass()).append(": ").append(cause.getMessage()).append("\n");
					for (final StackTraceElement trace : causeTraces) {
						sb.append("    at ").append(trace.getClassName()).append(".").append(trace.getMethodName()).append("(").append(trace.getFileName())
								.append(":").append(trace.getLineNumber()).append(");\n");
					}
				}
				cause = cause.getCause();
			}
		}
		return sb.toString();
	}

	/**
	 * Utility method to avoid NPE's in comparisons
	 *
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean compare(byte[] b1, byte[] b2) {
		if ((b1 == null) && (b2 == null))
			return true;
		if ((b1 == null) || (b2 == null))
			return false;
		if (b1.length != b2.length)
			return false;
		for (int i = 0; i < b1.length; i++) {
			if (b1[i] != b2[i])
				return false;
		}
		return true;
	}

	/**
	 * Check for empty byte array
	 *
	 * @param bytes
	 * @return true if null or ""
	 */
	public static boolean isEmpty(byte[] bytes) {
		if (bytes == null)
			return true;
		return bytes.length == 0;
	}

	/**
	 * Check for non-empty String
	 *
	 * @param str
	 * @return true if null or ""
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * Check for non-empty byte array
	 *
	 * @param bytes
	 * @return true if null or ""
	 */
	public static boolean isNotEmpty(byte[] bytes) {
		return !isEmpty(bytes);
	}

	public static String numAs2DigitString(int num) {
		if ((num <= 9) && (num >= 0))
			return "0" + num;
		return String.valueOf(num);
	}

	public static String getDifferenceInMinutesAsString(long departureTime, long arrivalTime) {
		StringBuffer sb = new StringBuffer();
		if (departureTime > arrivalTime) {
			// for now just swap and flag the error
			sb.append("Err:");
			long tmp = arrivalTime;
			arrivalTime = departureTime;
			departureTime = tmp;
		}
		long diffInSeconds = (arrivalTime - departureTime) / 1000;
		if (diffInSeconds < 60) {
			sb.append(diffInSeconds).append(" seconds.");
			return sb.toString();
		}
		long diffInMinutes = diffInSeconds / 60;
		if (diffInMinutes < 60) {
			sb.append(diffInMinutes).append(" minutes.");
			return sb.toString();
		}
		long diffInHours = diffInMinutes / 60;
		diffInMinutes = diffInMinutes % 60;
		sb.append(diffInHours).append(" hours, ").append(diffInMinutes).append(" minutes.");
		return sb.toString();
	}

	public static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	/**
	 * 
	 * @param isoDateStr
	 *            e.g. "2018-10-29"
	 * @return the LocalDate or null if not parsed
	 */
	public static LocalDate readFromISOLocalDateStr(String isoDateStr) {
		LocalDate result = null;
		try {
			result = LocalDate.parse(isoDateStr, DateTimeFormatter.ISO_DATE);
		} catch (Throwable t) {
		}
		return result;
	}

	public static synchronized ObjectMapper getOrCreateSerializingObjectMapper() {
		if (mObjectMapper != null)
			return mObjectMapper;
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
		javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
		javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
		javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer());
		javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer());
		objectMapper.registerModule(javaTimeModule);
		mObjectMapper = objectMapper;
		return mObjectMapper;
	}
	public static synchronized Gson getOrCreateSerializingGsonBuilder() {
		if (mGson != null)
			return mGson;
		mGson = new GsonBuilder().create();
		return mGson;
	}

	public static String getDateTimeNow() {
		return DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
	}

	public static String getDateTimeNow(ZoneId zoneId) {
		if (zoneId == null)
			return DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
		return DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now().atZone(zoneId));
	}

	public static String getLocalTimeNow() {
		return basicHHMMTimeFormatter.format(LocalDateTime.now());
	}

	public static String getLocalTimeNow(ZoneId zoneId) {
		if (zoneId == null)
			return basicHHMMTimeFormatter.format(LocalDateTime.now());
		return basicHHMMTimeFormatter.format(LocalDateTime.now(zoneId));
	}

	public static byte[] getBytesFromUUID(UUID uuid) {
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());
		return bb.array();
	}

	public static int randomNumber(int min, int max) {
		int range = Math.abs(max - min) + 1;
		return msRand.nextInt(range);
	}

	/**
	 * dateTimeStr = "2020-03-12T02:15:00+0400"
	 * 
	 * @param dateTimeStr
	 * @return
	 */
	public static String[] breakDateTimeStringIntoConstituentParts(String dateTimeStr) {
		if (dateTimeStr.contains(".000")) {
			dateTimeStr = dateTimeStr.replace(".000", "");
		}
		int dateTimeUTC = 0;
		int date = 1;
		int time = 2;
		String[] result = {"", "", ""};
		String[] splitDateTime = dateTimeStr.split("T");
		int splitAt = 19;
		String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
		String offset = dateTimeStr.substring(splitAt);
		if (offset.contains(":")) {
			String originalOffset = offset;
			offset = offset.replace(":", "");
			dateTimeStr = dateTimeStr.replace(originalOffset, offset);
		}
		DateTimeFormatter fmtr = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of(offset));
		ZonedDateTime utcZonedDateTime = ZonedDateTime.parse(dateTimeStr, fmtr).withZoneSameInstant(ZoneId.of("UTC"));
		DateTimeFormatter utcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ").withZone(ZoneId.of("UTC"));
		String formattedString = utcZonedDateTime.format(utcFormatter);
		result[dateTimeUTC] = formattedString;
		result[date] = splitDateTime[0];
		result[time] = splitDateTime[1].substring(0, "HH:mm:ss".length());
		return result;
	}

	/**
	 * Convert ICAO YYMMDD format to ISO YYY-MM-DD format
	 * 
	 * @param biographic
	 */
	public static void emrtdDateToIsoDate(Biographic biographic) {
		if (biographic != null) {
			if (biographic.getDateOfBirth() != null)
				biographic.setDateOfBirth(emrtdDateToIsoDate(true, biographic.getDateOfBirth()));
			if (biographic.getIssueDate() != null)
				biographic.setIssueDate(emrtdDateToIsoDate(false, biographic.getIssueDate()));
			if (biographic.getExpiryDate() != null)
				biographic.setExpiryDate(emrtdDateToIsoDate(false, biographic.getExpiryDate()));
		}
	}

	/**
	 * Convert ICAO eMRTD format (YYMMDD) or standard date format (DD/MM/YY or
	 * DD/MM/YYY) to ISO 8601 format (YYYY-MM-DD)
	 * 
	 * - the birth date wraps at 100 years old back to 19xx
	 * 
	 * - the expiry/issue date wraps to 1900 if the date is greater than 10
	 * years into the future
	 * 
	 * @param isBirthDate
	 * @param date
	 * @return
	 */
	public static String emrtdDateToIsoDate(boolean isBirthDate, String date) {
		if (date.length() == "YYYY-MM-DD".length() && date.contains("-"))
			return date;
		String result = null, yy = null, mm = null, dd = null;
		if (date.length() == 6 && !date.contains("/")) {
			/**
			 * ICAO YYMMDD format
			 */
			yy = date.substring(0, 2);
			mm = date.substring(2, 4);
			dd = date.substring(4, 6);
		} else if (date.contains("/")) {
			/**
			 * Date DD/MM/YY or DD/MM/YYYY format
			 */
			String[] dateArray = date.split("/");
			String tmp = (dateArray[2].length() == 4) ? dateArray[2].substring(2) : dateArray[2];
			yy = padLeftZeros(tmp.replaceAll("/", "").trim(), 2);
			mm = padLeftZeros(dateArray[1].replaceAll("/", "").trim(), 2);
			dd = padLeftZeros(dateArray[0].replaceAll("/", "").trim(), 2);
		} else
			return date;
		int year = Integer.parseInt(yy);
		if (isBirthDate) {
			int now = LocalDate.now().getYear() - 2000;
			if (year >= 00 && year <= now)
				year += 2000;
			else
				year += 1900;
		} else {
			int limit = (LocalDate.now().getYear() - 2000) + 10;
			if (year > limit)
				year += 1900;
			else
				year += 2000;
		}
		result = year + "-" + mm + "-" + dd;
		return result;
	}

	public static Map<String, Object> getPropertiesStartingWith(ConfigurableEnvironment aEnv, String aKeyPrefix) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> map = getAllProperties(aEnv);
		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			if (key.startsWith(aKeyPrefix)) {
				result.put(key, entry.getValue());
			}
		}
		return result;
	}

	public static Map<String, Object> getAllProperties(ConfigurableEnvironment aEnv) {
		Map<String, Object> result = new HashMap<>();
		aEnv.getPropertySources().forEach(ps -> addAll(result, getAllProperties(ps)));
		return result;
	}

	public static Map<String, Object> getAllProperties(PropertySource<?> aPropSource) {
		Map<String, Object> result = new HashMap<>();
		if (aPropSource instanceof CompositePropertySource) {
			CompositePropertySource cps = (CompositePropertySource) aPropSource;
			cps.getPropertySources().forEach(ps -> addAll(result, getAllProperties(ps)));
			return result;
		}
		if (aPropSource instanceof EnumerablePropertySource<?>) {
			EnumerablePropertySource<?> ps = (EnumerablePropertySource<?>) aPropSource;
			Arrays.asList(ps.getPropertyNames()).forEach(key -> result.put(key, ps.getProperty(key)));
			return result;
		}
		return result;
	}

	private static void addAll(Map<String, Object> aBase, Map<String, Object> aToBeAdded) {
		for (Entry<String, Object> entry : aToBeAdded.entrySet()) {
			if (aBase.containsKey(entry.getKey())) {
				continue;
			}
			aBase.put(entry.getKey(), entry.getValue());
		}
	}

}
