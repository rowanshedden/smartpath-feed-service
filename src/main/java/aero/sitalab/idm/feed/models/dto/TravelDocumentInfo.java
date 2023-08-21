package aero.sitalab.idm.feed.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TravelDocumentInfo {

	@JsonProperty("travelDocumentNumber")
	public String travelDocumentNumber;

	@JsonProperty("documentCode")
	public String documentCode;

	@JsonProperty("primaryName")
	public String primaryName;

	@JsonProperty("secondaryName")
	public String secondaryName;

	@JsonProperty("expiryDate")
	public String expiryDate;

	@JsonProperty("issuingState")
	public String issuingState;

	@JsonProperty("nationality")
	public String nationality;

	@JsonProperty("dateOfBirth")
	public String dateOfBirth;

	@JsonProperty("sex")
	public String sex;

}