package aero.sitalab.idm.feed.models.dto.smartpath.biometrictoken;

import aero.sitalab.idm.feed.models.dto.BaseRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BiometricTokenResponse extends BaseRequest {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("messageID")
    private String messageID;

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("errorReasonCode")
    private String errorReasonCode;

    @JsonProperty("errorMessage")
    private String errorMessage;

}
