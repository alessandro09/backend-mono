package br.com.alessandro.backend.auth.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login() {
		return "custom-login";
	}

	@GetMapping("/")
	public String root() {
		return "redirect:http://localhost:4202";
	}

}
