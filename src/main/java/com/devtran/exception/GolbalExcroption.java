/**
 * 
 */
package com.devtran.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devtran.dto.request.ApiReponse;

/**
 * @author pc
 *
 */

@ControllerAdvice
public class GolbalExcroption {

	@ExceptionHandler(value = Exception.class)
	ResponseEntity<ApiReponse> handlingRuntimeException() {
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

		return ResponseEntity.ok().body(response);
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
		
		return ResponseEntity.ok().body(response);
	}
	
}
