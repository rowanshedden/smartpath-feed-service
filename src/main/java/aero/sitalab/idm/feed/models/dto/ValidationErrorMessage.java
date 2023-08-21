package aero.sitalab.idm.feed.models.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel(value = "ErrorMessage", description = "An error message containing more info on why an operation failed")
@Data
@AllArgsConstructor
public class ValidationErrorMessage {

	private long timestamp;
	private int status;
	private String error;
	private String message;
	private String field;

}