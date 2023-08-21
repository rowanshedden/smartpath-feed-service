package aero.sitalab.idm.feed.models.dto;

import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * TravelDocument
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TravelDocument {

	@NotNull(message = "TravelDocument document type is missing")
	@SerializedName("type")
	@ApiModelProperty(example = "P", value = "document type (P=Passport, I=Identity Card, O=Other)")
	private String type;

	@Nullable
	@SerializedName("issuingState")
	@ApiModelProperty(example = "USA", value = "ISO2 or ISO3 country code who issued the document")
	private String issuingState;

	@NotNull(message = "TravelDocument document number is missing")
	@SerializedName("number")
	@ApiModelProperty(example = "N1542330", value = "document number")
	private String number;

	@NotNull(message = "TravellerDetails expiry date is missing")
	@SerializedName("expiryDate")
	@ApiModelProperty(example = "2025-01-21", value = "The expiry date of the document (yyyy-mm-dd)")
	private String expiryDate;

}
