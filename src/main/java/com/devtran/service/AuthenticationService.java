/**
 * 
 */
package com.devtran.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devtran.dto.request.AuthenticationRequest;
import com.devtran.dto.request.IntroSpectRequest;
import com.devtran.dto.response.AuthenticationResponse;
import com.devtran.dto.response.IntrospectResponse;
import com.devtran.exception.AppException;
import com.devtran.exception.ErrorCode;
import com.devtran.repository.AuthenticationRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

/**
 * @author pc
 *
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

	AuthenticationRepository authenticationRepository;
	
	@NonFinal
	@Value("${jwt.signerKey}")
	protected String SIGNER_KEY ;

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		var user = authenticationRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
		
		if (!authenticated) {
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		}
		
		var token = generateToken(request.getUsername());
		
		return AuthenticationResponse.builder()
				.token(token)
				.authencated(true)
				.build();
	}
	
	public String generateToken(String username) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
				.subject(username).issuer("devtran.com.vnn")
				.issueTime(new Date()).expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
				.build();

		Payload payload = new Payload(jwtClaimsSet.toJSONObject());

		JWSObject jwsObject = new JWSObject(header, payload);

		try {
			jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
			return jwsObject.serialize();
		} catch (JOSEException e) {
			throw new RuntimeException(e);
		}
	}
	
	public IntrospectResponse iResponse(IntroSpectRequest request) throws ParseException, JOSEException {
		var token = request.getToken();
		JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
		SignedJWT signedJWT = SignedJWT.parse(token);

		Date expityTime  = signedJWT.getJWTClaimsSet().getExpirationTime();
		var verified = signedJWT.verify(verifier);

		return IntrospectResponse.builder()
				.valid(verified && expityTime.after(new Date()))
				.build();
	}
}
