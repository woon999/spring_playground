package com.example.springlog4j2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class FooController {
	// private final Logger log = LoggerFactory.getLogger(FooController.class);

	@GetMapping("")
	public void log() {
		log.info("log info");
	}

}
