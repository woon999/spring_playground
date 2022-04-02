package com.example.springtestcontainers.usetcinfoinspringtest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = UseTcInfoInSpringTest.ContainerPropertyInitializer.class)
class UseTcInfoInSpringTest {

	@Value("${container.port}") int port;

	@Container
	private static GenericContainer container = new GenericContainer("mysql:8")
		.withExposedPorts(3308);

	@Test
	void get_container_mapped_port_by_3307() {
		System.out.println("port = " + port);
	}

	static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{
		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			TestPropertyValues.of("container.port=" + container.getMappedPort(3308))
				.applyTo(applicationContext);
		}
	}
}

