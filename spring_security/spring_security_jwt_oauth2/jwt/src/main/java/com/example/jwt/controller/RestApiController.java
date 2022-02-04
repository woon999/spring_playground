package com.example.jwt.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt.model.User;
import com.example.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// @CrossOrigin
@RestController
@RequiredArgsConstructor
public class RestApiController {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;


	@GetMapping("home")
	public String home(){
		return "<h1>home</h1>";
	}

	@PostMapping("token")
	public String token(){
		return "<h1>token</h1>";
	}

	@PostMapping("join")
	public String join(@RequestBody User user){
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles("ROLE_USER");
		userRepository.save(user);
		return user.getId()+ "<h1>회원가입 완료 </h1>";
	}
}

