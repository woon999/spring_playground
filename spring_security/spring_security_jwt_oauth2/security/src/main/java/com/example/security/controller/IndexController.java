package com.example.security.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.security.repository.UserRepository;
import com.example.security.model.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping({"","/"})
	public String index(){
		// src/main/resources/{prefix}/name.{suffix}
		return "index";
	}


	@GetMapping("/user")
	public @ResponseBody String user(){
		return "user";
	}

	@GetMapping("/admin")
	public @ResponseBody String admin(){
		return "admin";
	}

	@GetMapping("/manager")
	public @ResponseBody String manager(){
		return "manager";
	}

	@GetMapping("/loginForm")
	public String loginForm(){
		return "loginForm";
	}

	@GetMapping("/joinForm")
	public String joinForm(){
		return "joinForm";
	}

	@PostMapping("/join")
	public String join(User user) {
		user.setRole("ROLE_USER");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.save(user);

		return "redirect:/loginForm";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/data")
	public @ResponseBody String data(){
		return "data only admin";
	}

	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/users")
	public @ResponseBody String userList(){
		return "user list only manager, admin";
	}
}
