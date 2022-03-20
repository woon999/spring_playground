package com.example.jwt.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt.config.jwt.JwtProperties;
import com.example.jwt.model.User;
import com.example.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// @CrossOrigin
@RestController
@RequiredArgsConstructor
public class RestApiController {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final JwtProperties jwtProperties;

	@GetMapping("home")
	public String home(){
		return "<h1>home</h1>";
	}

	@PostMapping("token")
	public String token(){
		return jwtProperties.getExpirationTime() + "<h1>token</h1>";
	}

	@PostMapping("join")
	public String join(@RequestBody User user){
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles("ROLE_USER");
		userRepository.save(user);
		return user.getId()+ "<h1>회원가입 완료 </h1>";
	}

	// USER , MANAGER, ADMIN
	@GetMapping("/api/v1/user")
	public String user(){
		return "<h1> user </h1>";
	}

	// MANAGER, ADMIN
	@GetMapping("/api/v1/manager")
	public String manager(){
		return "<h1> manager </h1>";
	}

	// ADMIN
	@GetMapping("/api/v1/admin")
	public String admin(){
		return "<h1> admin </h1>";
	}
}

