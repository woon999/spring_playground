package com.example.springtestcontainerslocalstack.config;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.*;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@TestConfiguration
public class LocalStackS3Config {
	private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack");

	// GenericContainer start(), stop() 메서드로 생명주기 설정
	@Bean(initMethod = "start", destroyMethod = "stop")
	public LocalStackContainer localStackContainer(){
		return new LocalStackContainer(LOCALSTACK_IMAGE)
			.withServices(S3);
	}

	@Bean
	public AmazonS3 amazonS3(LocalStackContainer localStackContainer){
		return  AmazonS3ClientBuilder
			.standard()
			.withEndpointConfiguration(localStackContainer.getEndpointConfiguration(S3))
			.withCredentials(localStackContainer.getDefaultCredentialsProvider())
			.build();
	}

}
