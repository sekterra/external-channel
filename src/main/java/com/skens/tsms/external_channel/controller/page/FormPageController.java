package com.skens.tsms.external_channel.controller.page;

import com.skens.tsms.external_channel.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/forms")
public class FormPageController {

	private static final Logger log = LoggerFactory.getLogger(FormPageController.class);

	private final JwtService jwtService;

	public FormPageController(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	@GetMapping
	public String formPage(
		@RequestParam(required = false) String jwt,
		@RequestParam(required = false) String traceId,
		Model model
	) {
		if (jwt == null || !jwtService.validateToken(jwt, traceId)) {
			return "redirect:/bridge?error=invalid";
		}
		if (traceId != null) {
			log.info("[TRACE:{}] FORM_OPENED", traceId);
		}
		model.addAttribute("claims", jwtService.getClaims(jwt));
		model.addAttribute("traceId", traceId);
		model.addAttribute("traceStatus", traceId != null ? "PASS: OK (STUB), JWT: OK (STUB)" : "");
		model.addAttribute("navTitle", "신청서");
		return "form";
	}
}
