package com.skens.tsms.external_channel.service;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PassVerificationService {

	private static final Logger log = LoggerFactory.getLogger(PassVerificationService.class);

	// STUB: PASS verification always succeeds
	public Map<String, String> verifyIdentity(String token) {
		return verifyIdentity(token, null);
	}

	public Map<String, String> verifyIdentity(String token, String traceId) {
		if (traceId != null) {
			log.info("[TRACE:{}] PASS_STUB_OK", traceId);
		}
		return Map.of(
			"ci", "dummy_ci_12345",
			"name", "홍길동",
			"birthDate", "19900101",
			"phoneNumber", "01012345678"
		);
	}
}
