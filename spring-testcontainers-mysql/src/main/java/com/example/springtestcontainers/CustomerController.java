package com.example.springtestcontainers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
	private final CustomerRepository customerRepository;

	@GetMapping("/")
	public List<Customer> customers() {
		List<Customer> all = customerRepository.findAll();
		all.forEach(s -> log.info("Found a customer: {}", s));
		return all;
	}

}
