package com.example.springtestcontainers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
// @Testcontainers
class CustomerIntegrationTest {

	@Autowired
	private CustomerRepository customerRepository;

	// @Container
	// private static MySQLContainer mysqlContainer = new MySQLContainer("mysql:8");


	// @DynamicPropertySource
	// public static void overrideProps(DynamicPropertyRegistry registry){
	// 	registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
	// 	registry.add("spring.datasource.username", mysqlContainer::getUsername);
	// 	registry.add("spring.datasource.password", mysqlContainer::getPassword);
	// }


	@Test
	void schema_script_data_should_be_three() {
		List<Customer> customers = customerRepository.findAll();
		customers.forEach(System.out::println);
		assertEquals(customers.size(), 3);
	}

	@Test
	void schema_script_data_should_be_two() {
		List<Customer> customers = customerRepository.findAll();
		customerRepository.deleteById(customers.get(0).getId());
		customers = customerRepository.findAll();
		customers.forEach(System.out::println);
		assertEquals(customers.size(), 2);
	}

	@Test
	void when_using_a_clean_db_this_should_be_empty() {
		customerRepository.deleteAll();
		List<Customer> customers = customerRepository.findAll();
		System.out.println(customers.size());
		assertEquals(customers.size(), 0);
	}


}

