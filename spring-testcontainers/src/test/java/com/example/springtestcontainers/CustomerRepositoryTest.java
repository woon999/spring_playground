package com.example.springtestcontainers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CustomerRepositoryTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	void create_customer(){
		Customer customer = new Customer("123", "lee");
		Customer save = customerRepository.save(customer);
		System.out.println("save = " + save.getId());
	}
}