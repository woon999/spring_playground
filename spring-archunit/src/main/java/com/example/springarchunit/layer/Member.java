package com.example.springarchunit.layer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Member {

	@Id @GeneratedValue
	private Long id;
	private String name;
}
