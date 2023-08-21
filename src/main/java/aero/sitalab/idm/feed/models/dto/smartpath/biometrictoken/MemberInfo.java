package aero.sitalab.idm.feed.models.dto.smartpath.biometrictoken;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MemberInfo {

    @JsonProperty("id")
    public String id;

    @JsonProperty("biographic")
    public List<Biographic> biographic;

    @JsonProperty("biometric")
    public Biometric biometric;

    @JsonProperty("confirmation")
    public List<Confirmation> confirmation;

}