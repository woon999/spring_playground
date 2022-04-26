package com.example.springtestcontainerslocalstack.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.amazonaws.services.s3.AmazonS3;
import com.example.springtestcontainerslocalstack.config.LocalStackS3Config;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LocalStackS3Config.class)
class S3ControllerTest {
	@Autowired
	AmazonS3 amazonS3;
	@Autowired
	TestRestTemplate testRestTemplate;

	@Test
	public void test(){
		String id = "1";
		String content = "test content";

		String uploadResponse = testRestTemplate.postForObject("/s3/{id}", content, String.class, id);
		System.out.println("upload response = " + uploadResponse);

		String readResponse = testRestTemplate.getForObject("/s3/{id}", String.class, id);
		System.out.println("read response = " + readResponse);
	}
}
