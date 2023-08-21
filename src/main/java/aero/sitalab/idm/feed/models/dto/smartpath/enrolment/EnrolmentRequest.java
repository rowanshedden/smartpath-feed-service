package aero.sitalab.idm.feed.models.dto.smartpath.enrolment;

import com.fasterxml.jackson.annotation.JsonProperty;
import aero.sitalab.idm.feed.models.dto.BaseRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EnrolmentRequest extends BaseRequest {
    @JsonProperty("messageId")
    public String messageId;

    @JsonProperty("correlationId")
    public String correlationId;

    @JsonProperty("messageTs")
    public String messageTs;

    @JsonProperty("clientId")
    public String clientId;

    @JsonProperty("groupInfo")
    public GroupInfo groupInfo;

    @JsonProperty("liveBiometric")
    public List<LiveBiometric> liveBiometric;

    @JsonProperty("travelDocumentInfo")
    public List<TravelDocumentInfo> travelDocumentInfo;

    @JsonProperty("BCBP")
    public String bcbp;

    @JsonProperty("MRZ")
    public String mrz;

}
