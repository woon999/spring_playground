package com.example.springlogs;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class FooController {
	// private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
	/**
	 * trace < debug < info < warn < error
	 * default : info
	 */
	@GetMapping("/")
	public void log(){
		log.trace("trace message");
		log.debug("debug message");
		log.info("info message"); // default
		log.warn("warn message");
		log.error("error message");
	}
}
