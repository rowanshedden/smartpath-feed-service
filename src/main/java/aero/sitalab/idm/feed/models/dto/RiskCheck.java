package aero.sitalab.idm.feed.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import aero.sitalab.idm.feed.utils.MiscUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RiskCheck {

	@JsonProperty("check")
	@ApiModelProperty(value = "Biographic Watchlist Check", name = "check", dataType = "String", example = "Biographic Watchlist Check")
	private String check;

	@JsonProperty("passed")
	@ApiModelProperty(value = "true", name = "passed", dataType = "boolean", example = "Indicator true/false if the check has passed risk assessment")
	private boolean passed;

	public String toJson() {
		return MiscUtil.toJson(this);
	}

}
