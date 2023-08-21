package aero.sitalab.idm.feed.models.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "ErrorMessage", description = "An error message containing more info on why an operation failed")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error {

	private long timestamp = System.currentTimeMillis();
	private int status = -1;
	private String error = "unsupported service";
	private String message = "the service is not yet implemented";
	private String field = "n/a";

}