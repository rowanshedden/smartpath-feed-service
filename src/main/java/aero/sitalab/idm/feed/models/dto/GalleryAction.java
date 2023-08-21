package aero.sitalab.idm.feed.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import aero.sitalab.idm.feed.models.Action;
import aero.sitalab.idm.feed.utils.MiscUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GalleryAction {

	@JsonProperty("action")
	private Action action;

	@JsonProperty("data")
	private GalleryRecord data;

	@JsonProperty("upk")
	private String upk;

	public String toJson() {
		return MiscUtil.toJson(this);
	}

}
