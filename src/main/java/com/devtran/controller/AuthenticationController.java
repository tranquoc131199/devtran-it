/**
 * 
 */
package com.devtran.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devtran.dto.request.ApiReponse;
import com.devtran.dto.request.AuthenticationRequest;
import com.devtran.dto.request.IntroSpectRequest;
import com.devtran.dto.request.LogoutRequest;
import com.devtran.dto.response.AuthenticationResponse;
import com.devtran.dto.response.IntrospectResponse;
import com.devtran.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author pc
 *
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

	AuthenticationService authenticationService;

	@PostMapping("/login")
	ApiReponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {

		var result = authenticationService.authenticate(request);
		
		
		return ApiReponse.<AuthenticationResponse>builder()
				.result(result)
				.build();

	}
	
	@PostMapping("/introspect")
	ApiReponse<IntrospectResponse> introspect(@RequestBody IntroSpectRequest request) throws ParseException, JOSEException {

		var result = authenticationService.iResponse(request);
		
		return ApiReponse.<IntrospectResponse>builder()
				.result(result)
				.build();

	}
	
	@PostMapping("/logout")
	ApiReponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {

		authenticationService.logout(request);
		
		return ApiReponse.<Void>builder()
				.build();

	}
	

}
