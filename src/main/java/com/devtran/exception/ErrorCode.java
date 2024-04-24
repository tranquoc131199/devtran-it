/**
 * 
 */
package com.devtran.exception;

import lombok.Getter;

/**
 * @author pc
 *
 */
@Getter
public enum ErrorCode {

	USER_EXISTED(1001, "User existed"),
	UNCATGORIZED_EXCEPTION(999, "ERROR SYSTEM"),
	USER_NAME_INVALID(1003,"Username must be at least 3 charater"),
	PASSWORD_INVALID(1004,"Password must be at least 8 charater"),
	INVALID_KEY(0001,"Invalid Key"),
	USER_NOT_FOUND(1001, "User not found"),
	USER_NOT_EXIST(1001, "User not found"),
	;

	private ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	private int code;
	private String message;
}
