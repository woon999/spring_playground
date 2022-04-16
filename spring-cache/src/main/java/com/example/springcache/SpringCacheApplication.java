package com.example.springcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCacheApplication.class, args);
	}

}
