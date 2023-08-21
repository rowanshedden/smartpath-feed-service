package aero.sitalab.idm.feed.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import aero.sitalab.idm.feed.models.IBiographic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Biographic implements IBiographic {

	@JsonProperty("date-of-birth")
	@ApiModelProperty(example = "740812", value = "Date of birth: YYMMDD")
	private String dateOfBirth;

	@JsonProperty("document-number")
	@ApiModelProperty(example = "L898902C3", value = "Travel document number, e.g. passport number")
	private String documentNumber;

	@JsonProperty("document-type")
	@ApiModelProperty(example = "P", value = "Travel document type: P (passport), V (visa)")
	private String documentType;

	@JsonProperty("expiry-date")
	@ApiModelProperty(example = "280101", value = "Document expiry date: YYMMDD")
	private String expiryDate;

	@JsonProperty("family-name")
	@ApiModelProperty(example = "ERICSSON", value = "First name(s) of traveller")
	private String familyName;

	@JsonProperty("gender")
	@ApiModelProperty(example = "FEMALE", value = "Gender of traveller: MALE, FEMALE, OTHER")
	private String gender;

	@JsonProperty("given-names")
	@ApiModelProperty(example = "ANNA MARIA", value = "Family name of traveller")
	private String givenNames;

	@JsonProperty("issue-date")
	@ApiModelProperty(example = "18231", value = "Document issue date: YYMMDD")
	private String issueDate;

	@JsonProperty("issuing-authority")
	@ApiModelProperty(example = "UTO", value = "Issuing authority: 3 letter ISO country code")
	private String issuingAuthority;

	@JsonProperty("issuing-state")
	@ApiModelProperty(example = "UTO", value = "Issuing state: 3 letter ISO country code")
	private String issuingState;

	@JsonProperty("mrz")
	@ApiModelProperty(example = "P<UTOERIKSSON<<ANNA<MARIA<<<<<<<<<<<<<<<<<<<\nL898902C36UTO7408122F1204159ZE184226B<<<<<10", value = "MRZ string (with carriage return)")
	private String mrz;

	@JsonProperty("nationality")
	@ApiModelProperty(example = "UTO", value = "Nationality: 3 letters code (ISO 3166-1) or country name")
	private String nationality;

	@JsonProperty("place-of-birth")
	@ApiModelProperty(example = "SOMEWHERE, UTOPIA", value = "Place of birth")
	private String placeOfBirth;

	@JsonProperty("secondary-document-type")
	@ApiModelProperty(example = "X", value = "Secondary travel document type")
	private String secondaryDocumentType;

}
