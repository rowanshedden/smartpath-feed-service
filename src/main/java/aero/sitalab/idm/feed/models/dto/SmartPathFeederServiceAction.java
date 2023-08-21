package aero.sitalab.idm.feed.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SmartPathFeederServiceAction {

	public final static String FETCH = "FETCH";
	public final static String SEND_UPDATE = "SEND_UPDATE";

	@JsonProperty("action")
	private String action;

	@JsonProperty("data")
	private String data;

	@JsonProperty("id")
	private String id;

}
