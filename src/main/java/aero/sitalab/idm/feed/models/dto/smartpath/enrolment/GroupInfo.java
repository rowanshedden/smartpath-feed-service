package aero.sitalab.idm.feed.models.dto.smartpath.enrolment;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GroupInfo {

@JsonProperty("airportCode")
public String airportCode;

@JsonProperty("terminal")
public String terminal;

@JsonProperty("location")
public String location;

}
