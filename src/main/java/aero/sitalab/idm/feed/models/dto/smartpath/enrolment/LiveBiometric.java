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
public class LiveBiometric {

@JsonProperty("biometricType")
public String biometricType;

@JsonProperty("biometricData")
public String biometricData;

}
