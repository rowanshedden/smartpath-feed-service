package aero.sitalab.idm.feed.models.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import aero.sitalab.idm.feed.utils.MiscUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Identity wrapper
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Identity {

	@Nullable
	@JsonProperty("biographic")
	@ApiModelProperty(value = "Biographical data")
	private Biographic biographic;

	@Nullable
	@JsonProperty("biometrics")
	@ApiModelProperty(value = "Biometric images")
	private List<Biometric> biometrics;

	@Nullable
	@JsonProperty("itinerary")
	@ApiModelProperty(value = "Optional travel itinerary")
	private TravelItinerary travelItinerary;

	@Nullable
	@JsonProperty("dtc")
	@ApiModelProperty(value = "Optional ASN1 DER encoded DTC")
	private String dtc;

	@Nullable
	@JsonProperty("additional-properties")
	@ApiModelProperty(hidden = true)
	private String additionalProperties;

	@Nullable
	@JsonProperty("applicant-id")
	@ApiModelProperty(value = "applicant identifier")
	private String applicantId;

	@Nullable
	@JsonProperty("associated-applicant-id")
	@ApiModelProperty(value = "Optional associated applicant identifier")
	private String associatedApplicantId;

	public String getApplicantId() {
		if (applicantId == null) {
			try {
				return MiscUtil.generateUPK(this.biographic);
			} catch (Exception e) {
			}
		}
		return applicantId;
	}

	public List<Biometric> getBiometrics() {
		if (biometrics == null)
			biometrics = new ArrayList<Biometric>();
		return biometrics;
	}

	public String toJson() {
		return MiscUtil.toJson(this);
	}

}