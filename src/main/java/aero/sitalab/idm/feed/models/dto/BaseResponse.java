package aero.sitalab.idm.feed.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import aero.sitalab.idm.feed.utils.MiscUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseResponse {

	private boolean success;

	private Error error;

	public String toJson() {
		return MiscUtil.toJson(this);
	}

}
