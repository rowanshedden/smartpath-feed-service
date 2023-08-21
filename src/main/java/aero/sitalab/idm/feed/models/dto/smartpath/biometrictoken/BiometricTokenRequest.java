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
public class BiometricTokenRequest extends BaseRequest {

    @JsonProperty("transactionInfo")
    public TransactionInfo transactionInfo;

    @JsonProperty("memberInfo")
    public MemberInfo memberInfo;

}
