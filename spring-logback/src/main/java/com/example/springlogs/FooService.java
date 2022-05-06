package com.example.springlogs;


import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FooService {
	// private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
	/**
	 * trace < debug < info < warn < error
	 * default : info
	 */
	public void log(){
		for(int i=0; i<10_000; i++){
			// log.debug("debug message"+i);
			log.info("info message"+i); // default
			// log.warn("warn message"+i);
			// log.error("error message"+i);
		}
	}
}
