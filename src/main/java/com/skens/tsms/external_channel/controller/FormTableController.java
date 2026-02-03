package com.skens.tsms.external_channel.controller;

import com.skens.tsms.external_channel.service.DataTableService;
import com.skens.tsms.external_channel.service.JwtService;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FormTableController {

	private final JwtService jwtService;
	private final DataTableService dataTableService;

	public FormTableController(JwtService jwtService, DataTableService dataTableService) {
		this.jwtService = jwtService;
		this.dataTableService = dataTableService;
	}

	@GetMapping("/form-table")
	public String formTable(@RequestParam(required = false) String jwt, Model model) {
		if (jwt == null || !jwtService.validateToken(jwt)) {
			return "redirect:/bridge?error=invalid";
		}
		Map<String, String> claims = jwtService.getClaims(jwt);
		List<Map<String, Object>> tableData = dataTableService.getSampleData();
		model.addAttribute("claims", claims);
		model.addAttribute("tableData", tableData);
		model.addAttribute("navTitle", "신청 내역 조회");
		return "form-table";
	}
}
