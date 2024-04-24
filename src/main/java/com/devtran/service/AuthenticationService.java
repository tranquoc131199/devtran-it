/**
 * 
 */
package com.devtran.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devtran.dto.request.AuthenticationRequest;
import com.devtran.exception.AppException;
import com.devtran.exception.ErrorCode;
import com.devtran.repository.AuthenticationRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author pc
 *
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

	AuthenticationRepository authenticationRepository;

	public boolean authenticate(AuthenticationRequest request) {
		var user = authenticationRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		return passwordEncoder.matches(request.getPassword(), user.getPassword());
	}
}
