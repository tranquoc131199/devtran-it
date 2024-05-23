/**
 * 
 */
package com.devtran.configuration;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.devtran.dto.request.ApiReponse;
import com.devtran.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
		
		response.setStatus(errorCode.getStatusCode().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ApiReponse<?> apiReponse = ApiReponse.builder()
				.code(errorCode.getCode())
				.messge(errorCode.getMessage())
				.build();
		
		ObjectMapper mapper = new ObjectMapper();
		
		response.getWriter().write(mapper.writeValueAsString(apiReponse));
		response.flushBuffer();
	}

}