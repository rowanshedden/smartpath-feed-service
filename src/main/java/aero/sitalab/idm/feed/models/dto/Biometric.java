package aero.sitalab.idm.feed.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Biometric {

	public static final String BIOMETRIC_TYPE_VIZ = "PASSPORT_VISIBLE";
	public static final String BIOMETRIC_TYPE_CHIP = "PASSPORT_PHOTO";
	public static final String BIOMETRIC_TYPE_LIVE = "LIVE_PHOTO";

	@NotNull(message = "Biometric Type is missing")
	@ApiModelProperty(example = "LIVE_PHOTO", value = "Biometric Type - PASSPORT_VISIBLE, PASSPORT_PHOTO, LIVE_PHOTO", position = 1)
	@JsonProperty("biometric-type")
	String biometricType;

	@NotNull(message = "Biometric is missing")
	@ApiModelProperty(example = "/9j/4AAQSkZJRgABAQAAAQABAAD/.....", value = "Biometric - MIME or Standard Base64 encode image", position = 2)
	@JsonProperty("biometric")
	String biometric;

	@Null
	@ApiModelProperty(hidden = true) // @ApiModelProperty(value = "Additional
										// properties associated with the
										// biometric", position = 3)
	@JsonProperty("additionalProperties")
	String additionalProperties;

	@Null
	@ApiModelProperty(hidden = true) // @ApiModelProperty(value = "Quality score
										// of image capture", position = 4)
	@JsonProperty("quality-score")
	Integer qualityScore;

	@ApiModelProperty(hidden = true)
	public boolean isMimeEncoded() {
		return this.biometric.contains("\n");
	}

	/**
	 * Convert MIME encoded Base64 to Standard encoded Base64
	 */
	public Biometric convertMIMEtoStandardBase64() {
		this.biometric = this.biometric.replaceAll("\n", "");
		return this;
	}

}
