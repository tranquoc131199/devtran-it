/**
 * 
 */
package com.devtran.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

/**
 * @author pc
 *
 */
@Getter
public enum ErrorCode {

	USER_EXISTED(1005, "User existed", HttpStatus.BAD_REQUEST),
	UNCATGORIZED_EXCEPTION(999, "ERROR SYSTEM", HttpStatus.INTERNAL_SERVER_ERROR),
	USER_NAME_INVALID(1003,"Username must be at least 3 charater", HttpStatus.BAD_REQUEST),
	PASSWORD_INVALID(1004,"Password must be at least {min} charater", HttpStatus.BAD_REQUEST),
	INVALID_KEY(0001,"Invalid Key",  HttpStatus.BAD_REQUEST),
	USER_NOT_FOUND(1001, "User not found", HttpStatus.NOT_FOUND),
	USER_NOT_EXIST(1001, "User not found", HttpStatus.NOT_FOUND),
	UNAUTHENTICATED(1002, "Unauthenticated",  HttpStatus.UNAUTHORIZED),
	UNAUTHORIZED(1003, "you do not have permission",  HttpStatus.FORBIDDEN),
	INVALID_DOB(1004, "Your age must be at least {min}", HttpStatus.BAD_REQUEST)
	;

	private ErrorCode(int code, String message, HttpStatusCode statusCode) {
		this.code = code;
		this.message = message;
		this.statusCode = statusCode;
	}

	private int code;
	private String message;
	private HttpStatusCode statusCode;
}
