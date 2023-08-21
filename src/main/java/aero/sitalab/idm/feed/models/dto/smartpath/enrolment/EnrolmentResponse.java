package aero.sitalab.idm.feed.models.dto.smartpath.enrolment;

import com.fasterxml.jackson.annotation.JsonProperty;
import aero.sitalab.idm.feed.models.dto.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

public class EnrolmentResponse extends BaseResponse {
    private String s;
}
