package aero.sitalab.idm.feed.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import aero.sitalab.idm.feed.models.dto.BaseResponse;
import aero.sitalab.idm.feed.models.dto.ConfigurationRequest;
import aero.sitalab.idm.feed.models.dto.ConfigurationResponse;
import aero.sitalab.idm.feed.services.ConfigurationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@Api(tags = {"Configuration Management API"}, description = "Manage configuration updates (Administrator access)")
public class ConfigurationController extends Api_1_0 {

	@Autowired
	ConfigurationService configurationService;

	@ApiOperation(value = "Update the configuration information", authorizations = {@Authorization(value = "Bearer")}, response = BaseResponse.class)
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Configuration table has been updated")})
	@RequestMapping(value = "configuration", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> configuration(@RequestBody ConfigurationRequest request) {
		BaseResponse response = configurationService.addConfiguration(request);
		if (response.getError() != null) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	@ApiOperation(value = "Get the latest configuration", authorizations = {@Authorization(value = "Bearer")}, response = ConfigurationResponse.class)
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Configuration details")})
	@GetMapping(value = "configuration/{cfg}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getConfiguration(@PathVariable final String cfg) {
		ConfigurationResponse response = configurationService.getConfiguration(cfg);
		if (!response.isSuccess()) {
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

}
