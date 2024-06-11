/**
 * 
 */
package com.devtran.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.devtran.dto.request.AuthenticationRequest;
import com.devtran.dto.request.IntroSpectRequest;
import com.devtran.dto.request.LogoutRequest;
import com.devtran.dto.request.RefreshRequest;
import com.devtran.dto.response.AuthenticationResponse;
import com.devtran.dto.response.IntrospectResponse;
import com.devtran.entity.InvalidatedToken;
import com.devtran.entity.User;
import com.devtran.exception.AppException;
import com.devtran.exception.ErrorCode;
import com.devtran.repository.AuthenticationRepository;
import com.devtran.repository.InvalidatedTokenRepository;
import com.devtran.repository.UserRepository;
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
import lombok.var;
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
	
	InvalidatedTokenRepository invalidatedTokenRepository;
	
	UserRepository userRepository;

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		var user = authenticationRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
		
		if (!authenticated) {
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		}
		
		var token = generateToken(user);
		
		return AuthenticationResponse.builder()
				.token(token)
				.authencated(true)
				.build();
	}
	
	public AuthenticationResponse refreshToken(RefreshRequest request) throws JOSEException, ParseException {
		
		var signJWT = verifyToken(request.getToken());
		
		var jit = signJWT.getJWTClaimsSet().getJWTID();
		var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
		
		InvalidatedToken invalidatedToken = InvalidatedToken.builder()
				.id(jit)
				.expiryTime(expiryTime)
				.build();
		invalidatedTokenRepository.save(invalidatedToken);
		
		var username = signJWT.getJWTClaimsSet().getSubject();
		var user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
		
		var token = generateToken(user);
		
		return AuthenticationResponse.builder()
				.token(token)
				.authencated(true)
				.build();
	}
	
	public String generateToken(User user) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
				.subject(user.getUsername()).issuer("devtran.com.vnn")
				.issueTime(new Date()).expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
				.jwtID(UUID.randomUUID().toString())
				.claim("scope", buildScope(user))
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
		boolean isValid = true;
		try {
			verifyToken(token);
		} catch (Exception e) {
			isValid =false;
		}

		return IntrospectResponse.builder()
				.valid(isValid)
				.build();
	}
	
	private String buildScope(User user) {
		StringJoiner stringJoiner = new StringJoiner(" ");
		
		if (!CollectionUtils.isEmpty(user.getRoles())) {
			user.getRoles().forEach(role -> {
				stringJoiner.add("ROLE_"+role.getName());
				
				if(!CollectionUtils.isEmpty(role.getPermissions()))
				role.getPermissions().forEach(permission -> {
					stringJoiner.add( permission.getName());
				});
			});
		}

		return stringJoiner.toString();
	}

	public void logout(LogoutRequest request) throws JOSEException, ParseException {
		var signToken = verifyToken(request.getToken());
		
		String jwtId = signToken.getJWTClaimsSet().getJWTID();
		Date expiryTime =  signToken.getJWTClaimsSet().getExpirationTime();
		
		InvalidatedToken invalidatedToken = InvalidatedToken.builder()
				.id(jwtId)
				.expiryTime(expiryTime)
				.build();
		
		invalidatedTokenRepository.save(invalidatedToken);
		
	}
	
	private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
		
		
		JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
		SignedJWT signedJWT = SignedJWT.parse(token);

		Date expityTime  = signedJWT.getJWTClaimsSet().getExpirationTime();
		var verified = signedJWT.verify(verifier);
		
		// token is exprited
		if (!verified && expityTime.after(new Date())) {
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		}
		
		if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		}
		
		return signedJWT;
	}
}
