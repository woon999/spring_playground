package com.example.jwt.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class RestApiController {

	@GetMapping("home")
	public String home(){
		return "<h1>home</h1>";
	}
}
