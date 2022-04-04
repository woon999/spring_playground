package com.example.springaop.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springaop.annotation.Timer;
import com.example.springaop.dto.Member;

@RestController
@RequestMapping("/api")
public class RestApiController {

	@GetMapping("/get/{id}")
	public String get(@PathVariable Long id, @RequestParam String name){
		return id +" " + name;
	}

	@PostMapping("/post")
	public Member post(@RequestBody Member member){
		System.out.println("post Method [member] = " + member);
		return member;
	}


	@Timer
	@DeleteMapping("/delete")
	public String delete() throws InterruptedException {
		Thread.sleep(1000*2);

		return "delete";
	}
}
