package com.example.springaop;

import java.util.Base64;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringAopApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAopApplication.class, args);
		System.out.println("Base64 encode = " + Base64.getEncoder().encodeToString("test@naver.com".getBytes()));
	}

}
