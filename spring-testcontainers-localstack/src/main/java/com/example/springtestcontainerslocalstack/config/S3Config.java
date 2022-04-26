package com.example.springtestcontainerslocalstack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Profile("!local")
@Configuration
public class S3Config {
	@Bean
	public AmazonS3 amazonS3() {
		return AmazonS3ClientBuilder.standard()
			.build();
	}
}
