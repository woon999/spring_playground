package com.example.springtestcontainers.integrationtest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springtestcontainers.AbstractContainerBaseTest;
import com.example.springtestcontainers.Customer;
import com.example.springtestcontainers.CustomerRepository;

@IntegrationTest
class FooIntegrationTest1 extends AbstractContainerBaseTest {

	@Autowired
	private CustomerRepository customerRepository;

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


}

