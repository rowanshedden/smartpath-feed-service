package aero.sitalab.idm.feed.models.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RawHttpResult extends BaseResponse {

	private String httpStatus;
	
	@JsonRawValue
	private String body;

}
