/**
 * 
 */
package com.devtran.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devtran.dto.request.ApiReponse;
import com.devtran.dto.request.AuthenticationRequest;
import com.devtran.dto.response.AuthenticationResponse;
import com.devtran.service.AuthenticationService;

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

		boolean result = authenticationService.authenticate(request);
		
		
		return ApiReponse.<AuthenticationResponse>builder()
				.result(AuthenticationResponse.builder()
						.authencated(result)
						.build())
				.build();

	}

}
