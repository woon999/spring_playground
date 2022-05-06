package com.example.springlogs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FooController {

	private final FooService fooService;

	@GetMapping("/")
	public String log(){
		fooService.log();
		return "logging success";
	}
}
