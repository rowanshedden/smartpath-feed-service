package aero.sitalab.idm.feed.models.dto.smartpath;

import aero.sitalab.idm.feed.models.dto.BaseResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OauthTokenResponse extends BaseResponse {

    @JsonProperty("token_type")
    public String tokenType;

    @JsonProperty("expires_in")
    public int expiresIn;

    @JsonProperty("ext_expires_in")
    public int extExpiresIn;

    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("scope")
    public String scope;

    @JsonProperty("jti")
    public String jti;

}