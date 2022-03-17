package com.example.springdockermysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringDockerMysqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDockerMysqlApplication.class, args);
	}

}
