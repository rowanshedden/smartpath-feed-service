package aero.sitalab.idm.feed.models.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import aero.sitalab.idm.feed.models.IBiographic;
import aero.sitalab.idm.feed.utils.MiscUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DigitalTravelCredential implements IBiographic {

	@JsonProperty("typ")
	@ApiModelProperty(example = "P", value = "Travel document type: P (passport), V (visa)")
	private String documentType;

	@JsonProperty("doc")
	@ApiModelProperty(example = "L898902C3", value = "Travel document number, e.g. passport number")
	private String documentNumber;

	@JsonProperty("sec")
	@ApiModelProperty(example = "X", value = "Secondary travel document type")
	private String secondaryDocumentType;

	@JsonProperty("iau")
	@ApiModelProperty(example = "UTO", value = "Issuing authority: 3 letter ISO country code")
	private String issuingAuthority;

	@JsonProperty("sta")
	@ApiModelProperty(example = "UTO", value = "Issuing state: 3 letter ISO country code")
	private String issuingState;

	@JsonProperty("iss")
	@ApiModelProperty(example = "201231", value = "Document issue date: YYMMDD")
	private String issueDate;

	@JsonProperty("exp")
	@ApiModelProperty(example = "280101", value = "Document expiry date: YYMMDD")
	private String expiryDate;

	@JsonProperty("gnm")
	@ApiModelProperty(example = "ANNA MARIA", value = "First name(s) of traveller")
	private String givenNames;

	@JsonProperty("fnm")
	@ApiModelProperty(example = "ERICSSON", value = "Family name of traveller")
	private String familyName;

	@JsonProperty("dob")
	@ApiModelProperty(example = "740812", value = "Date of birth: YYMMDD")
	private String dateOfBirth;

	@JsonProperty("gen")
	@ApiModelProperty(example = "FEMALE", value = "Gender of traveller: MALE, FEMALE, OTHER")
	private String gender;

	@JsonProperty("pob")
	@ApiModelProperty(example = "SOMEWHERE, UTOPIA", value = "Place of birth")
	private String placeOfBirth;

	@JsonProperty("nat")
	@ApiModelProperty(example = "UTO", value = "Nationality: 3 letters code (ISO 3166-1) or country name")
	private String nationality;

	@JsonProperty("mrz")
	@ApiModelProperty(example = "P<UTOERIKSSON<<ANNA<MARIA<<<<<<<<<<<<<<<<<<<\nL898902C36UTO7408122F1204159ZE184226B<<<<<10", value = "MRZ string (with carriage return)")
	private String mrz;

	@JsonProperty("liv")
	@ApiModelProperty(value = "Base64 encoded live face image of traveller (selfie)")
	private String liveImage;

	@JsonProperty("viz")
	@ApiModelProperty(value = "Base64 encoded viz image of traveller (visible passport page image)")
	private String vizImage;

	@JsonProperty("chp")
	@ApiModelProperty(value = "Base64 encoded passport face image of traveller (passport chip)")
	private String chipImage;

	@JsonProperty("lds")
	@ApiModelProperty(value = "Logical Data Structure from passport chip (byte array)")
	private Byte[] lds;

	@JsonProperty("sod")
	@ApiModelProperty(value = "Digital Signature from passport chip (byte array)")
	private Byte[] sod;

	@JsonProperty("upk")
	@ApiModelProperty(example = "0357cdf9363daf60dc5dddd6f4c3de60b844b05ceaae56a28b36ed1c693b61a2", value = "Unique Passenger Key of traveller")
	private String upk;

	@JsonProperty("keySHA1")
	@ApiModelProperty(value = "SHA1 key to read chip")
	private String keySHA1;

	@JsonProperty("keySHA256")
	@ApiModelProperty(value = "SHA256 key to read chip")
	private String keySHA256;

	public Biographic extractBiographic(boolean includeMRZ) {
		Biographic biographic = new Biographic();
		biographic.setDocumentType(this.documentType);
		biographic.setDocumentNumber(this.documentNumber);
		biographic.setSecondaryDocumentType(this.secondaryDocumentType);
		biographic.setIssuingState(this.issuingState);
		biographic.setIssuingAuthority(this.issuingAuthority);
		biographic.setIssueDate(this.issueDate);
		biographic.setExpiryDate(this.expiryDate);
		biographic.setGivenNames(this.givenNames);
		biographic.setFamilyName(this.familyName);
		biographic.setDateOfBirth(this.dateOfBirth);
		biographic.setGender(this.gender);
		biographic.setNationality(this.nationality);
		biographic.setPlaceOfBirth(this.placeOfBirth);
		if (includeMRZ)
			biographic.setMrz(this.mrz);
		return biographic;
	}

	public List<Biometric> extractBiometrics() {
		List<Biometric> biometrics = new ArrayList<Biometric>();
		biometrics.add(new Biometric(Biometric.BIOMETRIC_TYPE_LIVE, this.liveImage, null, null));
		biometrics.add(new Biometric(Biometric.BIOMETRIC_TYPE_CHIP, this.chipImage, null, null));
		biometrics.add(new Biometric(Biometric.BIOMETRIC_TYPE_VIZ, this.vizImage, null, null));
		return biometrics;
	}

	public Biometric extractBiometricWithBiographic(String biometricType) {
		Biometric biometric = null;
		if (biometricType.equals(Biometric.BIOMETRIC_TYPE_LIVE))
			biometric = new Biometric(Biometric.BIOMETRIC_TYPE_LIVE, this.liveImage, null, null);
		else if (biometricType.equals(Biometric.BIOMETRIC_TYPE_CHIP))
			biometric = new Biometric(Biometric.BIOMETRIC_TYPE_CHIP, this.chipImage, null, null);
		else if (biometricType.equals(Biometric.BIOMETRIC_TYPE_VIZ))
			biometric = new Biometric(Biometric.BIOMETRIC_TYPE_VIZ, this.vizImage, null, null);
		return biometric != null ? biometric : new Biometric(biometricType, null, null, null);
	}

	public String toJson() {
		return MiscUtil.toJson(this);
	}

	public String toJsonMinified() {
		return MiscUtil.toJson(this);
	}

}
