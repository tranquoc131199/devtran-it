/**
 * 
 */
package com.devtran.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devtran.dto.request.ApiReponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author pc
 *
 */

@ControllerAdvice
@Slf4j
public class GolbalExcroption {

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
		try {
			errorCode = ErrorCode.valueOf(enumKey);
		} catch (IllegalArgumentException e) {
			
		}
		
		ApiReponse response = new ApiReponse<>();
		response.setCode(errorCode.getCode());
		response.setMessge(errorCode.getMessage());
		
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
	
}
