package aero.sitalab.idm.feed.utils;

import java.io.IOException;
import java.time.LocalTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
	@Override
	public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		return LocalTime.parse(p.getValueAsString(), LocalTimeSerializer.HHMMFORMATTER);
	}
}
