package com.example.springtestcontainers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
class CustomerIntegrationTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Container
	private static MySQLContainer container = new MySQLContainer("mysql:8")
		.withDatabaseName("testdb")
		.withPassword("1234");

	@Test
	void schema_script_data_should_be_two() {
		List<Customer> customers = customerRepository.findAll();
		customers.forEach(System.out::println);
		assertEquals(customers.size(), 3);
	}

	@Test
	void when_using_a_clean_db_this_should_be_empty() {
		customerRepository.deleteAll();
		List<Customer> customers = customerRepository.findAll();
		System.out.println(customers.size());
		assertEquals(customers.size(), 0);
	}


}

