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
public class TransactionInfo {

    @JsonProperty("messageId")
    public String messageId;

    @JsonProperty("messageTs")
    public String messageTs;

    @JsonProperty("airportCode")
    public String airportCode;

    @JsonProperty("clientChannel")
    public String clientChannel;

}