package aero.sitalab.idm.feed.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import aero.sitalab.idm.feed.utils.MiscUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonInclude(Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseRequest {

	public String toJson() {
		return MiscUtil.toJson(this);
	}

}
