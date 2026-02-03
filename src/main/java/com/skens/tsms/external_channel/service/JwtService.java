package com.skens.tsms.external_channel.service;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private static final Logger log = LoggerFactory.getLogger(JwtService.class);

	// STUB: JWT generation always succeeds
	public String generateToken(Map<String, String> claims) {
		return generateToken(claims, null);
	}

	public String generateToken(Map<String, String> claims, String traceId) {
		String token = "stub_jwt_token_" + System.currentTimeMillis();
		if (traceId != null) {
			log.info("[TRACE:{}] JWT_STUB_TOKEN_ISSUED", traceId);
		}
		return token;
	}

	// STUB: JWT validation always succeeds
	public boolean validateToken(String token) {
		return validateToken(token, null);
	}

	public boolean validateToken(String token, String traceId) {
		boolean valid = token != null && token.startsWith("stub_jwt_token_");
		if (traceId != null) {
			log.info("[TRACE:{}] JWT_STUB_VALIDATE_OK", traceId);
		}
		return valid;
	}

	// STUB: return dummy claims (ci, name, birthDate, phoneNumber)
	public Map<String, String> getClaims(String token) {
		return Map.of(
			"ci", "dummy_ci_12345",
			"name", "홍길동",
			"birthDate", "19900101",
			"phoneNumber", "01012345678"
		);
	}
}
