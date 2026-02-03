package com.skens.tsms.external_channel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping("/")
	public String root() {
		return "redirect:/bridge";
	}

	@GetMapping("/sample")
	public String sample(Model model) {
		model.addAttribute("navTitle", "샘플");
		return "sample";
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("navTitle", "로그인");
		return "login";
	}
}
