package com.skens.tsms.external_channel.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ComponentDemoController {

	@GetMapping("/component-demo")
	public String componentDemo(Model model) {
		model.addAttribute("navTitle", "폼 컴포넌트 카탈로그");
		model.addAttribute("readonlyName", "홍길동");
		model.addAttribute("applicationTypes", List.of("유형1", "유형2", "유형3"));
		return "component-demo";
	}
}
