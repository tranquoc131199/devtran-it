/**
 * 
 */
package com.devtran.configuration;

import java.text.ParseException;
import java.util.Objects;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.devtran.dto.request.IntroSpectRequest;
import com.devtran.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.var;

/**
 * @author pc
 *
 */
@Component
public class CustomJwtDecoder implements JwtDecoder {
	
	@Value("${jwt.signerKey}")
	protected String SIGNER_KEY ;
	private NimbusJwtDecoder nimbusJwtDecoder = null;
	
	@Autowired
	private AuthenticationService authenticationService;

	@Override
	public Jwt decode(String token) throws JwtException {
		
		try {
		var response =authenticationService.iResponse(IntroSpectRequest.builder()
					.token(token)
					.build());
		
		if (!response.isValid()) 
			throw new JwtException("Token invalid");
		
		} catch (JOSEException | ParseException e) {
			throw new JwtException(e.getMessage());
		}
		
		if (Objects.isNull(nimbusJwtDecoder)) {
			 SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
			nimbusJwtDecoder = NimbusJwtDecoder
							.withSecretKey(secretKeySpec)
							.macAlgorithm(MacAlgorithm.HS512)
							.build();
		}
		
		return  nimbusJwtDecoder.decode(token);
	}

}
