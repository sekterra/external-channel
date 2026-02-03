package com.skens.tsms.external_channel.controller;

import com.skens.tsms.external_channel.service.JwtService;
import com.skens.tsms.external_channel.service.PassVerificationService;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/bridge")
public class BridgeController {

	private static final Logger log = LoggerFactory.getLogger(BridgeController.class);

	private final PassVerificationService passVerificationService;
	private final JwtService jwtService;

	public BridgeController(PassVerificationService passVerificationService, JwtService jwtService) {
		this.passVerificationService = passVerificationService;
		this.jwtService = jwtService;
	}

	@GetMapping
	public String bridge(@RequestParam(required = false) String token, Model model) {
		model.addAttribute("token", token);
		model.addAttribute("navTitle", "본인인증");
		return "bridge";
	}

	@PostMapping("/verify")
	public String verify(@RequestParam(required = false) String token) {
		String traceId = UUID.randomUUID().toString();
		log.info("[TRACE:{}] BRIDGE_VERIFY_START", traceId);
		log.info("[TRACE:{}] PASS_STUB_CALLED", traceId);
		Map<String, String> result = passVerificationService.verifyIdentity(token != null ? token : "", traceId);
		log.info("[TRACE:{}] JWT_STUB_CALLED", traceId);
		String jwt = jwtService.generateToken(result, traceId);
		log.info("[TRACE:{}] BRIDGE_VERIFY_SUCCESS redirect=/form?jwt=...", traceId);
		return "redirect:/form?jwt=" + jwt + "&traceId=" + traceId;
	}
}
