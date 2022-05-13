package com.example.springlog4j2;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FooService {
	// private final Logger log = LoggerFactory.getLogger(FooController.class);

	public void log(){
		for(int i=0; i<10_000; i++){
			log.info("info message"+i); // default
		}
	}

}
