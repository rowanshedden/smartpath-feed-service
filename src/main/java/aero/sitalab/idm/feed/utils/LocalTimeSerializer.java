package aero.sitalab.idm.feed.utils;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalTimeSerializer extends JsonSerializer<LocalTime> {

	public static final DateTimeFormatter HHMMFORMATTER = DateTimeFormatter.ofPattern("HH:mm"); 
	@Override
	public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeString(value.format(HHMMFORMATTER));
	}
}
