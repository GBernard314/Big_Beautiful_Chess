package com.bigbeautifulchess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	@GetMapping({ "", "/" })
	public String index(Model model) {
		model.addAttribute("message","hello !");
		return "index";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
}