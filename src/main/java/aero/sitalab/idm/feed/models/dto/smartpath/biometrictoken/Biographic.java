package aero.sitalab.idm.feed.models.dto.smartpath.biometrictoken;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Biographic {

    @JsonProperty("documentType")
    public String documentType;

    @JsonProperty("documentData")
    public DocumentData documentData;

}
