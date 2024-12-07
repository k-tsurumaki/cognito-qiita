package com.example.cognito_qiita.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@GetMapping("/greeting")
	public String greeting(){
		return "Hello!!";
	}
}
