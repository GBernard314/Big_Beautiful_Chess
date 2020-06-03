package com.bigbeautifulchess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import com.bigbeautifulchess.domain.User;
import com.bigbeautifulchess.form.RegisterForm;
import com.bigbeautifulchess.repository.UserRepository;

@Controller
public class IndexController {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/register")
	public String showRegisterForm(WebRequest request, Model model) {
		model.addAttribute("register", new User());
		return "register";
	}
	
	@PostMapping("/register")
	public String addForm(@ModelAttribute RegisterForm form) {
		User user = new User();
		user.setUsername(form.getUsername());
		user.setEmail(form.getEmail());
		user.setPassword(passwordEncoder.encode(form.getPassword()));
		userRepository.save(user);
		
		return "redirect:/login";
	}
	
}