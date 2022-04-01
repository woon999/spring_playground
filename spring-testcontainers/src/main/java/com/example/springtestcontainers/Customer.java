package com.example.springtestcontainers;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
public class Customer {

	@Id @GeneratedValue
	private Long id;
	private String firstName;
	private String lastName;

	@Override
	public String toString() {
		return String.format(
			"Customer[id=%d, firstName='%s', lastName='%s']",
			id, firstName, lastName);
	}

	public Customer(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
