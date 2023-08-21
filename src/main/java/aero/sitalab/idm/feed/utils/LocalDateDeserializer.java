package aero.sitalab.idm.feed.utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		return LocalDate.parse(p.getValueAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
	}
}
