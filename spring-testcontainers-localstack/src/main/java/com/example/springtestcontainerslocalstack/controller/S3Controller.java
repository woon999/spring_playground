package com.example.springtestcontainerslocalstack.controller;

import static com.example.springtestcontainerslocalstack.config.Constant.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.AmazonS3;

@RestController
public class S3Controller {

	@Autowired
	private AmazonS3 amazonS3;

	@PostMapping("/s3/{id}")
	public String upload(@PathVariable Long id) {
		return amazonS3.putObject(BUCKET_NAME, String.valueOf(id), "test content").getContentMd5();
	}

	@GetMapping("/s3/{id}")
	public String read(@PathVariable Long id) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(amazonS3.getObject(BUCKET_NAME, String.valueOf(id)).getObjectContent(), "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line+"\n");
		}
		return sb.toString();
	}
}
