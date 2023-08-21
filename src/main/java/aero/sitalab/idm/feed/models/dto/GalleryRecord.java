package aero.sitalab.idm.feed.models.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import aero.sitalab.idm.feed.utils.MiscUtil;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GalleryRecord {

	@JsonProperty("transaction-id")
	@ApiModelProperty(example = "0357cdf9363daf60dc5dddd", value = "Transaction id assigned to the entry")
	private String transactionId;

	@JsonProperty("updated")
	@ApiModelProperty(example = "2022-02-08T15:30:00", value = "Date and time record was updated")
	private String updated;

	@JsonProperty("staff")
	@ApiModelProperty(example = "sita", value = "staff")
	private String staff;

	@JsonProperty("accessAllowed")
	@ApiModelProperty(example = "true", value = "Indicator for whether traveller is allowed access or not")
	private Boolean accessAllowed;

	@JsonProperty("dtc")
	@ApiModelProperty(value = "The Digital Tavel Credential assigned to the traveller")
	private DigitalTravelCredential dtc;

	@JsonProperty("itinerary")
	@ApiModelProperty(value = "The travel itinerary of the traveller")
	private TravelItinerary itinerary;

	@JsonProperty("checks")
	@Nullable
	@ApiModelProperty(value = "Risk assessment checks")
	private List<RiskCheck> checks = new ArrayList<RiskCheck>();

	@JsonProperty("accepted")
	@ApiModelProperty(example = "2022-02-08T15:30:00", value = "Date and time record was user accepted the Ts and Cs")
	private String accepted;

	public GalleryRecord(DigitalTravelCredential dtc) {
		this.dtc = dtc;
	}

	public String toJson() {
		return MiscUtil.toJson(this);
	}

	public String toJsonMinified() {
		return MiscUtil.toJson(this);
	}

	public boolean isAccessAllowed() {
		if (accessAllowed == null || !accessAllowed.booleanValue()) {
			return false;
		}else {
			return true;
		}
	}

}
