/**
 * 
 */
package com.devtran.exception;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devtran.dto.request.ApiReponse;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pc
 *
 */

@ControllerAdvice
@Slf4j
public class GolbalExcroption {
	private static final String MIN_ATTRIBUTE = "min";

	@ExceptionHandler(value = Exception.class)
	ResponseEntity<ApiReponse> handlingRuntimeException(Exception ex) {
		  log.error("An error occurred: ", ex);
		
		ApiReponse response = new ApiReponse<>();
		response.setCode(ErrorCode.UNCATGORIZED_EXCEPTION.getCode());
		response.setMessge(ErrorCode.UNCATGORIZED_EXCEPTION.getMessage());

		return ResponseEntity.ok().body(response);
	}

//	@ExceptionHandler(value = MethodArgumentNotValidException.class)
//	ResponseEntity<String> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
//		return ResponseEntity.ok().body(exception.getFieldError().getDefaultMessage());
//	}
	
	@ExceptionHandler(value = AppException.class)
	ResponseEntity<ApiReponse> handlingAppException(AppException exception) {
		ErrorCode errorCode = exception.getErrorCode();
		ApiReponse response = new ApiReponse<>();
		response.setCode(errorCode.getCode());
		response.setMessge(errorCode.getMessage());

		return ResponseEntity.status(errorCode.getStatusCode()).body(response);
	}
	
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	ResponseEntity<ApiReponse> handlingValidation(MethodArgumentNotValidException exception) {
		String enumKey = exception.getFieldError().getDefaultMessage();
		
		ErrorCode errorCode = ErrorCode.INVALID_KEY;
		Map<String, Object> arrtributes = null;
		try {
			errorCode = ErrorCode.valueOf(enumKey);
			
			var constraintViolation = exception.getBindingResult()
					.getAllErrors().getFirst().unwrap(ConstraintViolation.class);
			
			 arrtributes =  constraintViolation.getConstraintDescriptor().getAttributes();
			log.info(arrtributes.toString());
			
		} catch (IllegalArgumentException e) {
			
		}
		
		ApiReponse response = new ApiReponse<>();
		response.setCode(errorCode.getCode());
		response.setMessge(Objects.nonNull(arrtributes) ? mapAttribute(errorCode.getMessage(), arrtributes) : errorCode.getMessage());
		
		return ResponseEntity.status(errorCode.getStatusCode()).body(response);
	}
	
	@ExceptionHandler(value = AccessDeniedException.class)
	ResponseEntity<ApiReponse> handlingAccessDeniedException() {
		ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
		
		return ResponseEntity.status(errorCode.getStatusCode()).body(
				ApiReponse.builder()
				.code(errorCode.getCode())
				.messge(errorCode.getMessage())
				.build()
				);
	}
	
	private String mapAttribute(String message, Map<String, Object> attributes) {
		String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
		return message.replace("{"+MIN_ATTRIBUTE+"}", minValue);
	}
	
}
