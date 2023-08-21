package aero.sitalab.idm.feed.controllers;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import aero.sitalab.idm.feed.models.dto.ValidationErrorMessage;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1.0")
public class Api_1_0 {

	@Value("${app.name:SITA Identity Management Credential Services}")
	String app;

	@Value("${app.version:1.0}")
	String version;

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ValidationErrorMessage handleValidationException(MethodArgumentNotValidException ex) {
		FieldError fieldError = ex.getBindingResult().getFieldError();
		return new ValidationErrorMessage(Instant.now().getEpochSecond(), 400, fieldError.getCode(), fieldError.getDefaultMessage(), fieldError.getField());
	}

}
