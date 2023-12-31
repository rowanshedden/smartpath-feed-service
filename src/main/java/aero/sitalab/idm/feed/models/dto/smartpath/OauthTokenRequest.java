package aero.sitalab.idm.feed.models.dto.smartpath;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import aero.sitalab.idm.feed.models.dto.BaseRequest;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OauthTokenRequest extends BaseRequest {

	@JsonProperty("grant_type")
	public String grantType;

	@JsonProperty("client_secret")
	public String clientSecret;

	@JsonProperty("client_id")
	public String clientId;

	@JsonProperty("scope")
	public String scope;

	@JsonProperty("username")
	public String username;

	@JsonProperty("password")
	public String password;

	public String toFormUrlEncoded() {
		String body = "grant_type=" + this.grantType;
		body += "&client_secret=" + this.clientSecret;
		body += "&client_id=" + this.clientId;
		body += "&scope=" + this.scope;
		return body;
	}

}