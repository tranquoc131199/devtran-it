/**
 * 
 */
package com.devtran.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author pc
 *
 */
@Getter
@Setter
public class AppException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ErrorCode errorCode;
	
	public AppException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public AppException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AppException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public AppException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
